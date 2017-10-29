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
        TextView description = (TextView)view.findViewById(R.id.content_description);
        description.setText(question.getDescriptionText());
        ImageView contentImage = (ImageView)view.findViewById(R.id.content_image);
        contentImage.setImageDrawable(Utils.loadDrawableFromAssets(getContext(),"images/"+question.getImage()));
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
                        audiobutton.setImageResource(R.drawable.mute_small);
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
