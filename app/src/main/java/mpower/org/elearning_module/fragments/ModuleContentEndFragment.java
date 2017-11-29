package mpower.org.elearning_module.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.ExamActivity;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 10/22/17.
 */

public class ModuleContentEndFragment extends BaseFragment {
    @BindView(R.id.btn_start_exam)
    Button btnStartExam;
    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;
    Exam exam;
    @Override
    protected int getFragmentLayout() {
        return R.layout.course_end_fragment;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Progress...Please Wait");
        databaseHelper=new DatabaseHelper(getContext());
        btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                //saveCurrentProgress();
                String moduleId=CurrentUserProgress.getInstance().getCurrentUserModuleProgress();
                if (databaseHelper.isExamAvailableForCourse(moduleId)){
                    Log.d("TAG","-------");
                    exam=databaseHelper.getExam(moduleId);
                }
                progressDialog.dismiss();
                startExamActivity();
               // showLongToast("Exam will Start here");
            }
        });
    }

    private void startExamActivity() {
        //TODO
        if (exam!=null){
            CurrentUserProgress.getInstance().setCurrentExamId(exam.getId());
            Intent intent=new Intent(getContext(),ExamActivity.class);
            intent.putExtra(AppConstants.DATA,(ArrayList<ExamQuestion>)exam.getExamQuestions());
            startActivity(intent);
        }
    }

    private void saveCurrentProgress() {
        //TODO for testing only,remove it !!!! the USERNAME
        String userName=UserCollection.getInstance().getUserData().getUsername();
        String moduleId=CurrentUserProgress.getInstance().getCurrentUserModuleProgress();
        String courseId=CurrentUserProgress.getInstance().getCurrentUserCourseProgress();
        String questionId=CurrentUserProgress.getInstance().getCurrentUserQuestionProgress();
        UserType userType=CurrentUserProgress.getInstance().getUserType();

        int module=Integer.valueOf(moduleId);

        int course=Integer.valueOf(courseId)+1;

        int noOfModules=databaseHelper.getNoOfModulesForThisCourse(courseId);
        if (module>noOfModules){
            module+=1;
          //  course=1;
        }

        databaseHelper.updateProgressTable(userName, String.valueOf(module), String.valueOf(course),questionId,userType);
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }
}
