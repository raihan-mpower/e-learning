package mpower.org.elearning_module;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.model.Question;

public class CourseContentActivity extends AppCompatActivity {
    private ViewPager mPager;
    public static ArrayList<Question> questions;
    private TextView tvCounter;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCounter=toolbar.findViewById(R.id.toolbar_title);
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            tvCounter.setText(""+position+" of "+questions.size());

            Fragment fragment = null;
            switch (questions.get(position).getQuestionType()){

                case TRUE_FALSE:
                    fragment= TrueFalseFragment.newInstance(questions.get(position));
                    break;
                case SELECT_ONE:
                    fragment= CourseContentActivityFragment.newInstance(questions.get(position));
                    break;
                case TRIVIA:
                    fragment= TriviaFragment.newInstance(questions.get(position));
                case NOT_DEFINED:
                    break;
                case MULTIPLE_SELECT:
                    break;
                default:
                   return CourseContentActivityFragment.newInstance(questions.get(position));
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }

}
