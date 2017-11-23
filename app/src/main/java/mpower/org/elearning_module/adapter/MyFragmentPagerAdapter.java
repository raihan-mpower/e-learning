package mpower.org.elearning_module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import mpower.org.elearning_module.fragments.ExamEndFragment;
import mpower.org.elearning_module.fragments.MultipleChoiceFragment;
import mpower.org.elearning_module.model.ExamQuestion;

/**
 * Created by sabbir on 11/23/17.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ExamQuestion> examQuestions;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<ExamQuestion> examQuestions) {
        super(fm);
        this.examQuestions=examQuestions;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        if (position==examQuestions.size()){
            fragment=new ExamEndFragment();
            return fragment;
        }else {
            switch (examQuestions.get(position).getType()) {
                case "multiple-choice":
                    fragment= MultipleChoiceFragment.newInstance(examQuestions.get(position));
                    break;
                default:
                    return MultipleChoiceFragment.newInstance(examQuestions.get(position));
            }


        }

        return fragment;
    }

    @Override
    public int getCount() {
        //+1 for detecting end of list
        return this.examQuestions.size()+1;
    }
}
