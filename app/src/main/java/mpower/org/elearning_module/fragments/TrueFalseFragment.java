package mpower.org.elearning_module.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.interfaces.AudioPlayerListener;
import mpower.org.elearning_module.interfaces.LastPageListener;
import mpower.org.elearning_module.model.Question;


public class TrueFalseFragment extends BaseFragment implements LastPageListener {
    @BindView(R.id.btn_audio)
    ImageButton audiobutton;
    private Button trueButton,falseButton;
    private TextView tvQuestionText,tvRightAnswer;
    private Question question;
    boolean isLast;
    boolean isPlaying=false;
    private boolean isPaused;
    public TrueFalseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            isLast=getArguments().getBoolean("isLast");
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
        trueButton= view.findViewById(R.id.btn_true);
        falseButton= view.findViewById(R.id.btn_false);
        question = (Question) getArguments().getSerializable("question");

        if (question != null) {
            tvQuestionText.setText(question.getDescriptionText());
        }

        trueButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (question.getRightAnswer().equalsIgnoreCase("true")){
                    tvRightAnswer.setText("Correct Answer!! "+question.getRightAnswer());
                }else {
                    tvRightAnswer.setText("Wrong Answer!! "+question.getRightAnswer());
                }
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (question.getRightAnswer().equalsIgnoreCase("false")){
                    tvRightAnswer.setText("Correct Answer!! "+question.getRightAnswer());
                }else {
                    tvRightAnswer.setText("Wrong Answer!! "+question.getRightAnswer());
                }
            }
        });

        audiobutton.setOnClickListener(new View.OnClickListener() {
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
                        audiobutton.setImageResource(R.drawable.ic_action_muted_audio);
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
    public void isLastPage(boolean isLastPage) {


    }
}
