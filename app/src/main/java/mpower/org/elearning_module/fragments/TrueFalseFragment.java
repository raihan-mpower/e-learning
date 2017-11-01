package mpower.org.elearning_module.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.interfaces.AudioPlayerListener;
import mpower.org.elearning_module.interfaces.LastPageListener;
import mpower.org.elearning_module.model.Question;


public class TrueFalseFragment extends BaseFragment {
    @BindView(R.id.btn_audio)
    ImageButton audiobutton;
    @BindView(R.id.tv_q_title)
    TextView tvTitle;
    @BindView(R.id.tv_q_description)
    TextView tvDescription;
    private RadioButton radioYes,radioNo;
    private TextView tvQuestionText,tvRightAnswer;
    private Question question;

    boolean isPlaying=false;
    private boolean isPaused;
    public TrueFalseFragment() {
        // Required empty public constructor
    }

    private String audioName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){

        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_true_false;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        tvQuestionText= view.findViewById(R.id.tv_question);
        tvRightAnswer= view.findViewById(R.id.tv_right_answer);
        radioYes= view.findViewById(R.id.radio_btn_yes);
        radioNo= view.findViewById(R.id.radio_btn_no);
        question = (Question) getArguments().getSerializable("question");

        if (question != null) {
            /*Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"SutonnyOMJ.ttf");
            tvQuestionText.setTypeface(typeface);*/
            tvQuestionText.setText(question.getTrueFalse());
            tvDescription.setText(question.getDescriptionText());
            tvTitle.setText(question.getTitleText());
            String audioName=question.getAudio();
            if (audioName!=null && !audioName.isEmpty()){
                this.audioName=audioName;
                getAudioPlayerListener().playAudio(audioName);
                audiobutton.setImageResource(R.drawable.mute_small);
                isPlaying=true;
            }

            radioYes.setText(question.getRightAnswer());
            radioNo.setText(question.getWrongAnswer());

        }

        radioYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tvRightAnswer.setText("Correct Answer !! "+question.getAnswer());
                /*if (question.getRightAnswer().trim().equalsIgnoreCase(getResources().getString(R.string.yes))){
                    tvRightAnswer.setText("Correct Answer !! "+question.getAnswer());
                }else {
                    tvRightAnswer.setText("Wrong Answer !! "+question.getAnswer());
                }*/
            }
        });

        radioNo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tvRightAnswer.setText("Wrong Answer !! "+question.getAnswer());
               /* if (question.getRightAnswer().trim().equalsIgnoreCase(getResources().getString(R.string.no))){
                    tvRightAnswer.setText("Correct Answer !! "+question.getAnswer());
                }else {
                    tvRightAnswer.setText("Wrong Answer !! "+question.getAnswer());
                }*/
            }
        });

        audiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioName!=null && !audioName.isEmpty()){
                    if (isPlaying){
                        if (isPaused){
                            getAudioPlayerListener().resume();
                            audiobutton.setImageResource(R.drawable.audio);
                            isPaused=false;
                        }else {
                            getAudioPlayerListener().pausePlayer();
                            audiobutton.setImageResource(R.drawable.mute_small);
                            isPaused=true;
                        }

                    }else {
                        getAudioPlayerListener().playAudio(question.getAudio());
                        audiobutton.setImageResource(R.drawable.audio);
                        isPlaying=true;
                        isPaused=false;
                    }
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static TrueFalseFragment newInstance(Question question) {
        TrueFalseFragment trueFalseFragment=new TrueFalseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        trueFalseFragment.setArguments(bundle);
        return trueFalseFragment;

    }

    @Override
    public void onPause() {
        super.onPause();
        getAudioPlayerListener().stopPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        getAudioPlayerListener().stopPlayer();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (question!=null){
                String audioName=question.getAudio();
                if (audioName!=null && !audioName.isEmpty()){
                    getAudioPlayerListener().playAudio(audioName);
                    audiobutton.setImageResource(R.drawable.audio);
                    isPlaying=true;
                }
            }

        }

    }

}
