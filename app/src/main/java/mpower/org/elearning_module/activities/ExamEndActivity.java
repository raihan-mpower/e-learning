package mpower.org.elearning_module.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.UsageTime;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 11/30/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public class ExamEndActivity extends BaseActivity {

    private static final int CODE_FEEDBACK =990 ;
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
    @BindView(R.id.exam_content)
    LinearLayout examLayout;
    @BindView(R.id.my_container)
    FrameLayout frameLayout;

    ProgressDialog progressDialog;
    DatabaseHelper databaseHelper;

    boolean isUserDumb=false;
    private int totalNoOfQuestion;
    private int score;
    private volatile boolean isRatingScreenShown=false;

    private static final int RATING_SCREEN_SHOW_DELAY=2000;

    @Override
    protected int getResourceLayout() {
        return R.layout.exam_end_activity_alyout;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving Progress...Please Wait");
        databaseHelper=new DatabaseHelper(this);
       // showresults();

        if (!isRatingScreenShown){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callFeedbackActivity();
                    isRatingScreenShown=true;
                }
            },RATING_SCREEN_SHOW_DELAY);
        }
    }

    public void showresults(){
        final int totalQuestions= ExamActivity.sExamAnswerMap.size();
        totalNoOfQuestion=totalQuestions;
        tottalQTV.setText(String.valueOf(totalQuestions));
        int rightAnswer=0;
        for (Map.Entry<String,String> entry:ExamActivity.sExamAnswerMap.entrySet()){
            if (entry.getValue().equalsIgnoreCase("Correct")){
                rightAnswer++;
            }
        }

        final int finalRightAnswer = rightAnswer;
        correctAnsTv.setText(String.valueOf(finalRightAnswer));
        score=rightAnswer;
        startNewCurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserDumb(totalQuestions, finalRightAnswer)){
                    showSimpleDialog(getString(R.string.failed),getString(R.string.failed_try_again));
                }else {
                    progressDialog.show();
                    saveCurrentProgress();
                    long time= UsageTime.getInstance().getUsageTime();
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

        /*if (!isRatingScreenShown){
            //frameLayout.setVisibility(View.VISIBLE);
            //examLayout.setVisibility(View.GONE);
            callFeedbackActivity();
            isRatingScreenShown=true;
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        showresults();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showresults();
    }

    private void callFeedbackActivity() {
        Intent intent=new Intent(this,FeedbackActivity.class);
        startActivityForResult(intent,CODE_FEEDBACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODE_FEEDBACK && resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            if (bundle!=null){
                int rating=bundle.getInt(AppConstants.FEEDBACK_RATING);
                showToast(""+rating);
            }
        }
    }

    private boolean isUserDumb(int totalQuestions, int totalRightAns) {
        return totalQuestions != totalRightAns;
    }

    private void callExamResultDetailActivity() {
        Intent intent=new Intent(this, ExamResultActivity.class);
        startActivity(intent);
    }

    private void startCourseActivity() {
        Intent intent=new Intent(this, ModuleActivity.class);
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
}
