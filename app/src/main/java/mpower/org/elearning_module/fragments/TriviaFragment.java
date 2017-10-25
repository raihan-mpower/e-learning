package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;


public class TriviaFragment extends Fragment {
    @BindView(R.id.content_description)
    TextView tvTrivia;
    Question question;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_trivia,container,false);
        ButterKnife.bind(this,view);

        if (question!=null){
            tvTrivia.setText(question.getDescriptionText());
        }
        return view;
    }

}
