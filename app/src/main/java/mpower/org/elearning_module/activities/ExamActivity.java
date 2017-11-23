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
import mpower.org.elearning_module.adapter.MyFragmentPagerAdapter;
import mpower.org.elearning_module.fragments.ExamEndFragment;
import mpower.org.elearning_module.fragments.MultipleChoiceFragment;
import mpower.org.elearning_module.interfaces.FragmentLifecycle;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.services.MediaPlayerService;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.view.LockableViewPager;

public class ExamActivity extends BaseActivity implements MultipleChoiceFragment.CallBack{

    public static String CURRENT_COURSE_TITLE = "ExamName";
    private LockableViewPager mPager;
    private ArrayList<ExamQuestion> questions;
    private TextView tvCounter;
    private MediaPlayerService mediaPlayerService;
    boolean isServiceBound;
    private PagerAdapter mPagerAdapter;

    private ScreenSlidePagerAdapter adapter;

    public static HashMap<String,String> sExamAnswerMap;
    boolean isBackFromExamResult;
    private int reviewPosition=0;

    boolean skippedQuestion=false;
    private Fragment examEndFragment;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_exam;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        Log.d("TAG","******");
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            isBackFromExamResult=bundle.getBoolean(AppConstants.IS_BACK_FROM_RESULT,false);
            if (isBackFromExamResult){
                reviewPosition=bundle.getInt(AppConstants.REVIEW_QUESTION_POSITION);
            }
            questions= (ArrayList<ExamQuestion>) bundle.get(AppConstants.DATA);
        }
      //  startMusicSercive();

        sExamAnswerMap=new HashMap<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCounter=toolbar.findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pager);
        mPager.setSwipeAble(true);
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

       /* MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),questions);
        mPager.setAdapter(adapter);*/


        setTitle(CURRENT_COURSE_TITLE);

        final int totalQues=questions.size();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                /*FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(position);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();*/

                currentPosition = position;

                if (position==questions.size()){
                    tvCounter.setText(R.string.last_page);
                    ((ExamEndFragment)examEndFragment).showresults();
                }else {
                    int p=position+1;
                    String text=getString(R.string.of_page,p,totalQues);
                    tvCounter.setText(text);
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

    //TODO ,need more work so disabling it for now ,i.e setSwipeAble(true) ,is always true.
    @Override
    public void skippied(boolean skipped) {
        skippedQuestion=skipped;
        if (skipped) {
            Log.d("TAG","Skipped");
            mPager.setSwipeAble(true);
        }else {
            mPager.setSwipeAble(true);
        }
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
                examEndFragment = fragment;
                return fragment;
            }else {
                if (isBackFromExamResult) position=reviewPosition;
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
        if (!skippedQuestion){
            mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
        }

    }

    public void jumpToPagePrev(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
    }
}
