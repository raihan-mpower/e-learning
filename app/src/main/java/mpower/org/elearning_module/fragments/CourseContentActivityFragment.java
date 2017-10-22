package mpower.org.elearning_module.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseContentActivityFragment extends Fragment {

    private Question question;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_content, container, false);
        question = (Question) getArguments().getSerializable("question");
        TextView description = (TextView)view.findViewById(R.id.content_description);
        description.setText(question.getDescriptionText());
        ImageView contentImage = (ImageView)view.findViewById(R.id.content_image);
        contentImage.setImageDrawable(Utils.loadDrawableFromAssets(getContext(),"images/"+question.getImage()));
        return view;
    }
}
