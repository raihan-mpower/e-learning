package mpower.org.elearning_module.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
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
import mpower.org.elearning_module.fragments.CourseContentActivityFragment;
import mpower.org.elearning_module.fragments.CourseEndFragment;
import mpower.org.elearning_module.fragments.ExamEndFragment;
import mpower.org.elearning_module.fragments.MultipleChoiceFragment;
import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.services.MediaPlayerService;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;

public class ExamActivity extends BaseActivity {
    public static String CURRENT_COURSE_TITLE = "ExamName";
    private ViewPager mPager;
    private ArrayList<ExamQuestion> questions;
    private TextView tvCounter;
    private MediaPlayerService mediaPlayerService;
    boolean isServiceBound;
    private PagerAdapter mPagerAdapter;

    public static HashMap<Integer,Integer> sExamAnswerMap;



    @Override
    protected int getResourceLayout() {
        return R.layout.activity_exam;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        Log.d("TAG","******");
        if (getIntent().getExtras()!=null){
            questions= (ArrayList<ExamQuestion>) getIntent().getExtras().get(AppConstants.DATA);
        }
      //  startMusicSercive();
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

                /*if (getMediaPlayerService().isPlaying()){
                    getMediaPlayerService().stopMedia();
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;

            if (position==questions.size()){
                fragment=new ExamEndFragment();
                return fragment;
            }else {
                switch (questions.get(position).getType()) {
                    case "multiple-choice":
                        fragment=MultipleChoiceFragment.newInstance(questions.get(position));
                        break;
                        default:
                            return MultipleChoiceFragment.newInstance(questions.get(position));
                }


            }

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

    public void jumpToPage(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
    }

    public void jumpToPagePrev(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
    }
}
