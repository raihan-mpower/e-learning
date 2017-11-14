package mpower.org.elearning_module.fragments;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseContentActivityFragment extends BaseFragment {

    @BindView(R.id.audio)
    ImageButton audiobutton;
    @BindView(R.id.linear_image_container)
    LinearLayout imageLayout;
    @BindView(R.id.tv_q_title)
    TextView tvTitle;
    private Question question;
    boolean isPlaying=false;
    private boolean isPaused;

    public CourseContentActivityFragment() {
    }

    public static CourseContentActivityFragment newInstance(Question question) {
        CourseContentActivityFragment fragment = new CourseContentActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_course_content;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        question = (Question) getArguments().getSerializable("question");
        TextView description = view.findViewById(R.id.content_description);
        description.setText(question.getDescriptionText());
        description.setMovementMethod(new ScrollingMovementMethod());
        ImageView contentImage = view.findViewById(R.id.content_image);
        tvTitle.setText(question.getTitleText());
        String imageName=question.getImage();
        if (imageName!=null && !imageName.isEmpty()){
            Glide.with(this).load(Uri.fromFile(new File(ELearningApp.IMAGES_FOLDER_NAME+ File.separator+imageName)))
                    .into(contentImage);
        }else {
            imageLayout.setVisibility(View.GONE);
        }
        /*final String audioName=question.getAudio();
        if (audioName!=null && !audioName.isEmpty()){
            getAudioPlayerListener().playAudio(audioName);
            audiobutton.setImageResource(R.drawable.audio);
            isPlaying=true;
        }


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
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();

        final String audioName=question.getAudio();
        if (audioName!=null && !audioName.isEmpty()){
           // getAudioPlayerListener().playAudio(audioName);
            //audiobutton.setImageResource(R.drawable.audio);
            //isPlaying=true;
        }


        /*audiobutton.setOnClickListener(new View.OnClickListener() {
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
        });*/

        audiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioName!=null && !audioName.isEmpty()){
                    if (isPlaying){

                            getAudioPlayerListener().stopPlayer();
                            audiobutton.setImageResource(R.drawable.audio);
                            isPlaying=false;

                    }else {
                        getAudioPlayerListener().playAudio(question.getAudio());
                        audiobutton.setImageResource(R.drawable.mute_small);
                        isPlaying=true;
                        isPaused=false;
                    }
                }


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        getAudioPlayerListener().stopPlayer();
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
       // getAudioPlayerListener().stopPlayer();
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
               /* String audioName=question.getAudio();
                if (audioName!=null && !audioName.isEmpty()){
                    getAudioPlayerListener().playAudio(audioName);
                    audiobutton.setImageResource(R.drawable.audio);
                    isPlaying=true;
                }*/
            }

        }

    }
}
