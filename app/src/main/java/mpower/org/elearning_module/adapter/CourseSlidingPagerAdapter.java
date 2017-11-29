package mpower.org.elearning_module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import mpower.org.elearning_module.fragments.ModuleContentEndFragment;
import mpower.org.elearning_module.fragments.ModuleContentFragment;
import mpower.org.elearning_module.fragments.TriviaFragment;
import mpower.org.elearning_module.fragments.TrueFalseFragment;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.CurrentUserProgress;

/**
 * Created by sabbir on 11/16/17.
 */

public class CourseSlidingPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Question> questions;

    public CourseSlidingPagerAdapter(FragmentManager fm,ArrayList<Question> questions) {
        super(fm);
        this.questions=questions;
    }

    @Override
    public int getCount() {
        return questions.size()+1;
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
}
