package mpower.org.elearning_module.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.fragments.ModuleContentFragment;
import mpower.org.elearning_module.fragments.ModuleContentEndFragment;
import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.interfaces.AudioPlayerListener;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.services.MediaPlayerService;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UsageTime;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

public class ModuleContentActivity extends BaseActivity implements AudioPlayerListener {

    public static String CURRENT_MODULE_TITLE = "ModuleName";
    private ViewPager mPager;
    private ArrayList<Question> questions;
    private TextView tvCounter;
    private MediaPlayerService mediaPlayerService;
    boolean isServiceBound;
    private PagerAdapter mPagerAdapter;

    public static ArrayList<Question> sQuestions;

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.sabbir.android.music.PlayNewAudio";

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

   private void playMusic(String media){
        if (!isServiceBound){
            Intent playerIntet=new Intent(this,MediaPlayerService.class);
            playerIntet.putExtra(AppConstants.AUDIO_FILE_NAME,media);
            startService(playerIntet);
            bindService(playerIntet,mServiceConnection,BIND_AUTO_CREATE);
        }else {
            getMediaPlayerService().playAudio(media);
        }
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_course_content;
    }


    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCounter=toolbar.findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pager);
        startMusicSercive();
        if (getIntent().getExtras()!=null){
            questions= (ArrayList<Question>) getIntent().getExtras().get(AppConstants.DATA);
        }

        setUpPagerAdapter();

       /* if (questions==null){
            //setUpPagerAdapter();
        }else {
            showToast("Something Wrong,Please Restart the app");

            setUpStaticPagerAdapter();
        }*/

      //  setUpStaticPagerAdapter();


    }

    private void setUpStaticPagerAdapter() {

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),sQuestions);
        mPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        UsageTime.getInstance().start();

        setTitle(CURRENT_MODULE_TITLE);
        final int totalQues=sQuestions.size();
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==sQuestions.size()){
                    tvCounter.setText(R.string.last_page);
                }else if (position==0){
                    String text=getString(R.string.of_page,1,totalQues);
                    tvCounter.setText(text);
                }
                else {
                    int p=position+1;
                    String text=getString(R.string.of_page,p,totalQues);
                    tvCounter.setText(text);
                }

                if (getMediaPlayerService().isPlaying()){
                    getMediaPlayerService().stopMedia();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpPagerAdapter() {
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),questions);
        mPager.setAdapter(mPagerAdapter);

        UsageTime.getInstance().start();

        setTitle(CURRENT_MODULE_TITLE);
        final int totalQues=questions.size();
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==questions.size()){
                    tvCounter.setText(R.string.last_page);
                }else if (position==0){
                    String text=getString(R.string.of_page,1,totalQues);
                    tvCounter.setText(text);
                }
                else {
                    int p=position+1;
                    String text=getString(R.string.of_page,p,totalQues);
                    tvCounter.setText(text);
                }

                if (getMediaPlayerService().isPlaying()){
                    getMediaPlayerService().stopMedia();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusicService();
    }

    public void jumpToPage(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
    }

    public void jumpToPagePrev(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
    }

    @Override
    public void playAudio(String name) {
        playMusic(name);
    }

    @Override
    public void stopPlayer() {
        getMediaPlayerService().stopMedia();
    }



    @Override
    public void mutePlayer(boolean flag) {
       getMediaPlayerService().muteAudio(flag);
    }

    @Override
    public void pausePlayer() {
        getMediaPlayerService().pauseMedia();
    }

    @Override
    public void resume() {
        getMediaPlayerService().resumeMedia();
    }




    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

       private ArrayList<Question> questions;

        ScreenSlidePagerAdapter(FragmentManager fm,ArrayList<Question> questions) {
            super(fm);
            this.questions=questions;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if (position==questions.size()){
                    fragment=new ModuleContentEndFragment();
                return fragment;
            }else {
                switch (questions.get(position).getQuestionType()) {

                    case TRUE_FALSE:
                        fragment = TrueFalseFragment.newInstance(questions.get(position));
                        break;
                    case COURSE_CONTENT:
                        fragment = ModuleContentFragment.newInstance(questions.get(position));
                        break;
                    case TRIVIA:
                        fragment = TriviaFragment.newInstance(questions.get(position));
                        break;
                    case NOT_DEFINED:
                        fragment= ModuleContentFragment.newInstance(questions.get(position));
                        break;
                    default:
                        return ModuleContentFragment.newInstance(questions.get(position));
                }


            }

            CurrentUserProgress.getInstance().setProgressQuestion(questions.get(position).getId());

            return fragment;
        }
        //plus one is for detecting the last fragment ,Sabbir

        @Override
        public int getCount() {
            return questions.size()+1;
        }
    }


    public MediaPlayerService getMediaPlayerService() {
        return mediaPlayerService;
    }

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MediaPlayerService.AudioBinder musicBinder=(MediaPlayerService.AudioBinder) iBinder;
            mediaPlayerService=musicBinder.getService();
            isServiceBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound=false;
        }
    };

    public boolean isServiceBound(){
        return isServiceBound;
    }

    public void startMusicSercive(){
        Intent playerIntent = new Intent(this, MediaPlayerService.class);
        startService(playerIntent);
        bindService(playerIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopMusicService(){
        if (isServiceBound()) unbindService(mServiceConnection);
    }

}
