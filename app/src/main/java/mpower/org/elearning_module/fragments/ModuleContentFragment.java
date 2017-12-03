package mpower.org.elearning_module.fragments;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class ModuleContentFragment extends BaseFragment {

    @BindView(R.id.audio)
    ImageButton audiobutton;
    @BindView(R.id.linear_image_container)
    LinearLayout imageLayout;
    @BindView(R.id.tv_q_title)
    TextView tvTitle;
    private Question question;
    boolean isPlaying=false;
    private boolean isPaused;

    public ModuleContentFragment() {
    }

    public static ModuleContentFragment newInstance(Question question) {
        ModuleContentFragment fragment = new ModuleContentFragment();
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
        String desc=question.getDescriptionText();
        /*if (desc.contains("-")){
            description.setText(Html.fromHtml(desc));
        }else {
            description.setText(desc);
        }*/
        description.setText(desc);
        //description.setText(Html.fromHtml(desc));

        description.setMovementMethod(new ScrollingMovementMethod());
        ImageView contentImage = view.findViewById(R.id.content_image);
        String title=question.getTitleText();
        if (title==null || title.isEmpty()){
            tvTitle.setVisibility(View.GONE);
        }else tvTitle.setText(question.getTitleText());

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

         final boolean isFileAvailable = new File(ELearningApp.IMAGES_FOLDER_NAME, audioName).exists();

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
                    if (isFileAvailable(audioName))
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

    private boolean isFileAvailable(String audioName) {
        return new File(ELearningApp.IMAGES_FOLDER_NAME,audioName).exists();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
       // getAudioPlayerListener().stopPlayer();
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
