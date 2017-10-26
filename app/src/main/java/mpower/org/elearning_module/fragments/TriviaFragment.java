package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;


public class TriviaFragment extends BaseFragment {
    @BindView(R.id.content_description)
    TextView tvTrivia;
    Question question;
    @BindView(R.id.audio)
    ImageButton audiobutton;

    boolean isPlaying=false;
    private boolean isPaused;

    public TriviaFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TriviaFragment newInstance(Question question) {
        TriviaFragment fragment = new TriviaFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question= (Question) getArguments().getSerializable("question");
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_trivia;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        if (question!=null){
            tvTrivia.setText(question.getDescriptionText());
        }

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
    public void isLastPage(boolean isLastPage) {

    }
}
