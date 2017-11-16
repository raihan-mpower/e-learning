package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.ExamActivity;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.AppConstants;

/**
 * @author sabbir
 */
public class MultipleChoiceFragment extends BaseFragment {

    ExamQuestion question;

    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.btn_audio)
    ImageButton audioButton;
    @BindView(R.id.ll_radiogroup_container)
    LinearLayout layoutForRadiogroup;
    @BindView(R.id.answer_tv)
    TextView tvAnswer;
    @BindView(R.id.btn_confirm_answer)
    Button answerButton;
    @BindView(R.id.answer_tv_status)
    TextView answerStatus;

    boolean isPlaying=false;
    private boolean isPaused;

    private boolean isPlaying(){
        return isPlaying;
    }

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

 public static  MultipleChoiceFragment newInstance(ExamQuestion question){
        MultipleChoiceFragment fragment=new MultipleChoiceFragment();
     Bundle bundle = new Bundle();
     bundle.putSerializable(AppConstants.DATA, question);
     fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            question = (ExamQuestion) getArguments().getSerializable(AppConstants.DATA);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_multiple_choice;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        tvQuestion.setText(question.getDescriptionText());

        /*String answer=question.();
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
        }*/
        final RadioGroup radioGroup=new RadioGroup(getContext());

        RadioGroup.LayoutParams rprms;

        for (String answer:question.getAnswer()){
            RadioButton radioButton=new RadioButton(getContext());
            radioButton.setText(answer);
            radioButton.setTextSize(17);
            radioButton.setId(new Random().nextInt(Integer.SIZE-1));
            radioButton.setTag(answer);
            rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton,rprms);
        }

        layoutForRadiogroup.addView(radioGroup);
       /* ViewGroup viewGroup= (ViewGroup) view;
        for (RadioButton radioButton:radioButtons){
            viewGroup.addView(radioButton);
        }*/

       answerButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(radioGroup.getCheckedRadioButtonId()!=-1){
                   int selectedId=radioGroup.getCheckedRadioButtonId();
                   Log.d("TAG",""+selectedId);
                   RadioButton radioButton=getActivity().findViewById(selectedId);
                   if (radioButton!=null){
                       if (radioButton.getText().toString().equalsIgnoreCase(question.getRightAnswer())){
                           answerStatus.setText(R.string.right);
                           ExamActivity.sExamAnswerMap.put(question.getDescriptionText(),"Correct");
                       }else {
                           answerStatus.setText( R.string.wrong);
                           ExamActivity.sExamAnswerMap.put(question.getDescriptionText(),"Wrong");
                       }
                       tvAnswer.setText(question.getRightAnswer());
                   }else {
                       showToast("Invalid Answer");
                   }
               }else {
                   showLongToast(getString(R.string.please_select_answer));
               }
           }
       });

       /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               //RadioButton radioButton=getActivity().findViewById(checkedId);
               RadioButton radioButton=getActivity().findViewById(checkedId);

               Toast.makeText(getContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();

               tvAnswer.setText(question.getRightAnswer());
           }
       });*/




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
                        audioButton.setImageResource(R.drawable.mute_small);
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
    }

}
