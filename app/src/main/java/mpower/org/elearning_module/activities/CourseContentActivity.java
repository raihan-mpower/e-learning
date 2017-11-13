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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.fragments.CourseContentActivityFragment;
import mpower.org.elearning_module.fragments.CourseEndFragment;
import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.interfaces.AudioPlayerListener;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.services.MediaPlayerService;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;

public class CourseContentActivity extends BaseActivity implements AudioPlayerListener {

    public static String CURRENT_COURSE_TITLE = "CourseName";
    private ViewPager mPager;
    private ArrayList<Question> questions;
    private TextView tvCounter;
    private MediaPlayerService mediaPlayerService;
    boolean isServiceBound;
    private PagerAdapter mPagerAdapter;

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.sabbir.android.music.PlayNewAudio";

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras()!=null){
            questions= (ArrayList<Question>) getIntent().getExtras().get(AppConstants.DATA);
        }
        startMusicSercive();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCounter=toolbar.findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        setTitle(CURRENT_COURSE_TITLE);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==questions.size()){
                    tvCounter.setText("Last Page");
                }else {
                    tvCounter.setText(""+(position+1)+" of "+questions.size());
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


        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if (position==questions.size()){
                    fragment=new CourseEndFragment();
                return fragment;
            }else {
                switch (questions.get(position).getQuestionType()) {

                    case TRUE_FALSE:
                        fragment = TrueFalseFragment.newInstance(questions.get(position));
                        break;
                    case COURSE_CONTENT:
                        fragment = CourseContentActivityFragment.newInstance(questions.get(position));
                        break;
                    case TRIVIA:
                        fragment = TriviaFragment.newInstance(questions.get(position));
                        break;
                    case NOT_DEFINED:
                        fragment=CourseContentActivityFragment.newInstance(questions.get(position));
                        break;
                    default:
                        return CourseContentActivityFragment.newInstance(questions.get(position));
                }


            }

            CurrentUserProgress.getInstance().setProgressQuestion(questions.get(position).getId());

            return fragment;
        }
        //plus one is for detecting the last fragment

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
