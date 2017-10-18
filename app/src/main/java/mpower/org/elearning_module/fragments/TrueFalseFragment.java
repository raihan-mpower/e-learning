package mpower.org.elearning_module.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Question;


public class TrueFalseFragment extends Fragment {
    private Button trueButton,falseButton;
    private TextView tvQuestionText,tvRightAnswer;
    private OnFragmentInteractionListener mListener;
    private Question question;
    public TrueFalseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_true_false, container, false);
        tvQuestionText= (TextView) view.findViewById(R.id.tv_question);
        tvRightAnswer= (TextView) view.findViewById(R.id.tv_right_answer);
        trueButton= (Button) view.findViewById(R.id.btn_true);
        falseButton= (Button) view.findViewById(R.id.btn_false);
        question = (Question) getArguments().getSerializable("question");

        tvQuestionText.setText(question.getDescriptionText());

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

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static TrueFalseFragment newInstance(Question question) {
        TrueFalseFragment trueFalseFragment=new TrueFalseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        trueFalseFragment.setArguments(bundle);
        return trueFalseFragment;

    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
