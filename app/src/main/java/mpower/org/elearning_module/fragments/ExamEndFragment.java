package mpower.org.elearning_module.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.CourseActivity;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 11/13/17.
 */

public class ExamEndFragment extends BaseFragment {

    @BindView(R.id.button_start__next_course)
    Button startNewCurse;
    @BindView(R.id.textView2)
    TextView headerTv;
    @BindView(R.id.textView4)
    TextView tottalQTV;
    @BindView(R.id.textView5)
    TextView correctAnsTv;

    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;

    @Override
    protected int getFragmentLayout() {
        return R.layout.exam_end_fragment;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Progress...Please Wait");
        databaseHelper=new DatabaseHelper(getContext());
        startNewCurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                saveCurrentProgress();
                startCourseActivity();
            }
        });
    }

    private void startCourseActivity() {
        Intent intent=new Intent(getContext(), CourseActivity.class);
        progressDialog.dismiss();
        startActivity(intent);
    }

    private void saveCurrentProgress() {
        //TODO for testing only,remove it !!!! the USERNAME
        String userName= UserCollection.getInstance().getUserData().getUsername();
        String moduleId= CurrentUserProgress.getInstance().getCurrentUserModuleProgress();
        String courseId=CurrentUserProgress.getInstance().getCurrentUserCourseProgress();
        String questionId=CurrentUserProgress.getInstance().getCurrentUserQuestionProgress();
        UserType userType=CurrentUserProgress.getInstance().getUserType();

        int module=Integer.valueOf(moduleId);

        int course=Integer.valueOf(courseId)+1;

        int noOfCourses=databaseHelper.getNoOfCoursesForThisModule(moduleId);
        if (course>noOfCourses){
            module+=1;
            //  course=1;
        }

        databaseHelper.updateProgressTable(userName, String.valueOf(module), String.valueOf(course),questionId,userType);
    }
}
