package mpower.org.elearning_module.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.Utils;


public class TriviaFragment extends BaseFragment {
    @BindView(R.id.content_description)
    TextView tvTrivia;
    Question question;
    @BindView(R.id.audio)
    ImageButton audiobutton;
    @BindView(R.id.content_image)
    ImageView imageView;
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
            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"SutonnyOMJ.ttf");
            tvTrivia.setTypeface(typeface);
            tvTrivia.setText(question.getDescriptionText());

            String imageName=question.getImage();
            if (imageName!=null && !imageName.equalsIgnoreCase("")){
                imageView.setImageDrawable(Utils.loadDrawableFromAssets(getContext(),imageName));
            }
        }

        audiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",question.getAudio());
                if (isPlaying){
                    getAudioPlayerListener().pausePlayer();
                        audiobutton.setImageResource(R.drawable.mute_small);
                        isPlaying=false;
                }else {
                    getAudioPlayerListener().playAudio(question.getAudio());
                    isPlaying=true;
                }

            }
        });
    }

    @Override
    public void isLastPage(boolean isLastPage) {

    }
}
