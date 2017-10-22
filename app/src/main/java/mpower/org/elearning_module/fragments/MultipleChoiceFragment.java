package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;

/**
 * @author sabbir
 */
public class MultipleChoiceFragment extends BaseFragment {
    Question question;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

 public static  MultipleChoiceFragment newInstance(Question question){
        MultipleChoiceFragment fragment=new MultipleChoiceFragment();
     Bundle bundle = new Bundle();
     bundle.putSerializable("question", question);
     fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            question = (Question) getArguments().getSerializable("question");
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_multiple_choice;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        tvQuestion.setText(question.getDescriptionText());
        String answer=question.getAnswer();
        ArrayList<CheckBox> checkBoxes;
        if (answer.contains(",")){
            String[] answers=answer.split(",");
            if (answers.length>0) {

                checkBoxes=new ArrayList<>();
                for (String a : answers) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(a);
                    checkBox.setId(new Random().nextInt());
                    checkBoxes.add(checkBox);

                }
                ViewGroup viewGroup= (ViewGroup) view;
                for (CheckBox checkBox:checkBoxes){
                    viewGroup.addView(checkBox);
                }
            }
        }

    }

    @Override
    public void isLastPage(boolean isLastPage) {
        //showToast("You are in Last Page");
       // getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CourseEndFragment()).commit();
        //showDialog();
    }
}
