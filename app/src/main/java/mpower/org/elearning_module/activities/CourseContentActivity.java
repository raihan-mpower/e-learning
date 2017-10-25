package mpower.org.elearning_module.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rd.PageIndicatorView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.fragments.CourseContentActivityFragment;
import mpower.org.elearning_module.fragments.CourseEndFragment;
import mpower.org.elearning_module.fragments.MultipleChoiceFragment;
import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.interfaces.LastPageListener;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.CurrentUserProgress;

public class CourseContentActivity extends BaseActivity {
    private ViewPager mPager;
    public static ArrayList<Question> questions;
    private TextView tvCounter;
    LastPageListener lastPageListener;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    boolean isLast=false;
    private PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_course_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCounter=toolbar.findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(mPager);
       DotsIndicator dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(mPager);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
               /* if (position==questions.size()-1){
                    isLast=true;
                }
                if (isLast) lastPageListener.isLastPage(true);*/
                tvCounter.setText(""+(position+1)+" of "+questions.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_course_content;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

    }

    public void jumpToPage(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
    }

    public void jumpToPagePrev(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
//            Toast.makeText(CourseContentActivity.this,""+position,Toast.LENGTH_SHORT).show();
//            tvCounter.setText(""+position+" of "+questions.size());
            return super.getPageTitle(position);
        }




        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //slick bit of code :D ,sabbir
            Fragment fragment;

            if (position==questions.size()){
                    fragment=new CourseEndFragment();
                return fragment;
            }else {
                switch (questions.get(position).getQuestionType()) {

                    case TRUE_FALSE:
                        fragment = TrueFalseFragment.newInstance(questions.get(position));
                        break;
                    case SELECT_ONE:
                        fragment = CourseContentActivityFragment.newInstance(questions.get(position));
                        break;
                    case TRIVIA:
                        fragment = TriviaFragment.newInstance(questions.get(position));
                        break;
                    case NOT_DEFINED:
                        fragment=CourseContentActivityFragment.newInstance(questions.get(position));
                        break;
                    case MULTIPLE_SELECT:
                        fragment = MultipleChoiceFragment.newInstance(questions.get(position));
                        break;
                    default:
                        return CourseContentActivityFragment.newInstance(questions.get(position));
                }


            }
           /* if (isLast) {
                Bundle bundle=new Bundle();
                bundle.putBoolean("isLast",isLast);
                fragment.setArguments(bundle);

            }*/

           /* Log.d("TAG",""+isLast);
            if (fragment!=null && fragment instanceof LastPageListener){
                lastPageListener= (LastPageListener) fragment;
            }*/

            CurrentUserProgress.getInstance().setProgressQuestion(questions.get(position).getId());

            return fragment;
        }
        //plus one is for detecting the last fragment
        @Override
        public int getCount() {
            return questions.size()+1;
        }
    }

}
