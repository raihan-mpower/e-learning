package mpower.org.elearning_module.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import mpower.org.elearning_module.R;

/**
 * Created by sabbir on 10/22/17.
 */

public class CourseEndFragment extends BaseFragment {
    @BindView(R.id.btn_start_exam)
    Button btnStartExam;
    @Override
    public void isLastPage(boolean isLastPage) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.course_end_fragment;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {

        btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Exam will open here");
            }
        });
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }
}
