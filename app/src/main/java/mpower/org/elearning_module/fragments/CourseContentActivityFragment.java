package mpower.org.elearning_module.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import mpower.org.elearning_module.R;
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
        ImageView contentImage = view.findViewById(R.id.content_image);
        String imageName=question.getImage();
        if (imageName!=null && !imageName.isEmpty()){
            contentImage.setImageDrawable(Utils.loadDrawableFromAssets(getContext(),"images/"+question.getImage()));
        }else {
            imageLayout.setVisibility(View.GONE);
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
}
