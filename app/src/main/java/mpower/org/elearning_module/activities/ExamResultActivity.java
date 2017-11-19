package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;

public class ExamResultActivity extends BaseActivity {

    @BindView(R.id.detail_listview)
    ListView listView;
    @BindView(R.id.try_again_button)
    Button tryAgainButton;

    private Exam exam;
    private DatabaseHelper databaseHelper;

    boolean isUserDumb=false;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_exam_result_acitivity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

        if (getIntent().getExtras()!=null){
            isUserDumb=getIntent().getBooleanExtra(AppConstants.IS_USER_DUMB,false);
        }
        TextView tvEmpty = findViewById(android.R.id.empty);
        databaseHelper=new DatabaseHelper(this);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView.setEmptyView(tvEmpty);
        final ArrayList<String> qustionList=new ArrayList<>();
        final ArrayList<String> answerList=new ArrayList<>();
        for (Map.Entry<String ,String> entry:ExamActivity.sExamAnswerMap.entrySet()){
            qustionList.add(entry.getKey());
            answerList.add(entry.getValue());
        }

        int res=android.R.layout.simple_list_item_2;

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,res,android.R.id.text1,qustionList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(qustionList.get(position));
                text2.setText(answerList.get(position));

                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exam=databaseHelper.getExambyId(CurrentUserProgress.getInstance().getCurrentExamId());
                callExamActivity();
            }
        });

    }

    private void callExamActivity() {
        Intent intent=new Intent(this,ExamActivity.class);
        intent.putExtra(AppConstants.DATA,(ArrayList<ExamQuestion>)exam.getExamQuestions());
        intent.putExtra(AppConstants.IS_USER_DUMB,isUserDumb);
        startActivity(intent);
        finish();
    }
}
