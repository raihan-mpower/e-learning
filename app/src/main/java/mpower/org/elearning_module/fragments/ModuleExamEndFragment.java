package mpower.org.elearning_module.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.ExamEndActivity;
import mpower.org.elearning_module.activities.FeedbackActivity;
import mpower.org.elearning_module.activities.ModuleActivity;
import mpower.org.elearning_module.activities.ExamActivity;
import mpower.org.elearning_module.activities.ExamResultActivity;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.interfaces.FragmentLifecycle;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.UsageTime;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 11/13/17.
 */

public class ModuleExamEndFragment extends BaseFragment implements FragmentLifecycle{

    @BindView(R.id.button_start__next_course)
    Button startNewCurse;
    @BindView(R.id.textView2)
    TextView headerTv;
    @BindView(R.id.textView4)
    TextView tottalQTV;
    @BindView(R.id.textView5)
    TextView correctAnsTv;
    @BindView(R.id.button)
    Button detailResultButton;

    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;

    boolean isUserDumb=false;
    private int totalNoOfQuestion;
    private int score;
    private boolean isRatingScreenShown=false;

    @Override
    protected int getFragmentLayout() {
        return R.layout.exam_end_fragment;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Progress...Please Wait");
        databaseHelper=new DatabaseHelper(getContext());


    }

    public void callExamEndActivity(){
        startActivity(new Intent(getContext(), ExamEndActivity.class));
        getActivity().finish();
    }

    public void showresults(){
        final int totalQuestions= ExamActivity.sExamAnswerMap.size();
        totalNoOfQuestion=totalQuestions;
        tottalQTV.append(String.valueOf(totalQuestions));
        int rightAnswer=0;
        for (Map.Entry<String,String> entry:ExamActivity.sExamAnswerMap.entrySet()){
            if (entry.getValue().equalsIgnoreCase("Correct")){
                rightAnswer++;
            }
        }

        final int finalRightAnswer = rightAnswer;
        correctAnsTv.append(String.valueOf(finalRightAnswer));
        score=rightAnswer;
        startNewCurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserDumb(totalQuestions, finalRightAnswer)){
                    showSimpleDialog(getString(R.string.failed),getString(R.string.failed_try_again));
                }else {
                    progressDialog.show();
                    saveCurrentProgress();
                    long time=UsageTime.getInstance().getUsageTime();
                    Long t=time;
                    int seconds=t.intValue()/1000;
                    startCourseActivity();
                }

            }
        });

        detailResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callExamResultDetailActivity();
            }
        });

        if (!isRatingScreenShown){
           // getChildFragmentManager().beginTransaction().replace(R.id.fragment_container,new FeedbackActivity()).commit();
            isRatingScreenShown=true;
        }

    }

    private boolean isUserDumb(int totalQuestions, int totalRightAns) {
        return totalQuestions != totalRightAns;
    }

    private void callExamResultDetailActivity() {
        Intent intent=new Intent(getContext(), ExamResultActivity.class);
        startActivity(intent);
    }

    private void startCourseActivity() {
        Intent intent=new Intent(getContext(), ModuleActivity.class);
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

        int noOfModules=databaseHelper.getNoOfModulesForThisCourse(courseId);
        if (module>noOfModules){
          //  module+=1;
              course+=1;
        }else {
            module+=1;
        }
        String examId=CurrentUserProgress.getInstance().getCurrentExamId();
        databaseHelper.saveExamProgress(userName,examId,totalNoOfQuestion,score,null);
        databaseHelper.updateProgressTable(userName, String.valueOf(module), String.valueOf(course),questionId,userType);
    }

    @Override
    public void onResume() {
        super.onResume();
//        showresults();
    }

    @Override
    public void onPauseFragment() {
        Log.i("TAG", "ModuleExamEndFragment+onPauseFragment()");
       // Toast.makeText(getActivity(), "onPauseFragment():" + "TAG", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Log.i("TAG", "ModuleExamEndFragment+onResumedFragment()");
       // tottalQTV.setText(totalNoOfQuestion);
       // correctAnsTv.setText(score);
       // Toast.makeText(getActivity(), "onResumedFragment():" + "TAG", Toast.LENGTH_SHORT).show();
    }
}
