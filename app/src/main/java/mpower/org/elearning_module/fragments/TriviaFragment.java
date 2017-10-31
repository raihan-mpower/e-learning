package mpower.org.elearning_module.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.application.ELearningApp;
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
    @BindView(R.id.linear_image_container)
    LinearLayout imageLayout;
    @BindView(R.id.tv_q_title)
            TextView tvTitle;
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
            /*Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"SutonnyOMJ.ttf");
            tvTrivia.setTypeface(typeface);*/
            tvTitle.setText(question.getTitleText());
            tvTrivia.setText(question.getDescriptionText());
            tvTrivia.setMovementMethod(new ScrollingMovementMethod());
            String imageName=question.getImage();
            if (imageName!=null && !imageName.equalsIgnoreCase("")){
                Glide.with(this).load(Uri.fromFile(new File(ELearningApp.IMAGES_FOLDER_NAME+ File.separator+imageName)))
                        .into(imageView);
               // imageView.setImageDrawable(Utils.loadDrawableFromAssets(getContext(),imageName));
            }else {
                imageLayout.setVisibility(View.GONE);
            }

            String audioName=question.getAudio();
            if (audioName!=null && !audioName.isEmpty()){
                getAudioPlayerListener().playAudio(audioName);
                audiobutton.setImageResource(R.drawable.mute_small);
                isPlaying=true;
            }
        }

        audiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",question.getAudio());
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
                    isPlaying=true;
                    isPaused=false;
                }

            }
        });
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
