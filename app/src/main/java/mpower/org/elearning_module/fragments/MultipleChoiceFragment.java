package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    @BindView(R.id.btn_audio)
    ImageButton audioButton;

    boolean isPlaying=false;
    private boolean isPaused;

    private boolean isPlaying(){
        return isPlaying;
    }

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

        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",question.getAudio());
                if (isPlaying){
                    if (isPaused){
                        getAudioPlayerListener().resume();
                        isPaused=false;
                        isPlaying=true;
                    }else {
                        getAudioPlayerListener().pausePlayer();
                        audioButton.setImageResource(R.drawable.ic_action_muted_audio);
                        isPlaying=false;
                        isPaused=true;
                    }
                }else {
                    getAudioPlayerListener().playAudio(question.getAudio());
                    isPlaying=true;
                    isPaused=false;
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAudioPlayerListener().stopPlayer();
    }

    @Override
    public void isLastPage(boolean isLastPage) {
        //showToast("You are in Last Page");
       // getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CourseEndFragment()).commit();
        //showDialog();
    }
}
