package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.adapter.CourseGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserCollection;

public class CourseActivity extends AppCompatActivity {

    public static String CURRENT_MODULE_TITLE="";
    private GridView gridView;
    private ArrayList<Course> courses;
    public static String CURRENT_MODULE_ID="";
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        Toolbar toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = findViewById(R.id.gridView1);

        if (getIntent().getExtras()!=null){
            courses= (ArrayList<Course>) getIntent().getExtras().get(AppConstants.DATA);
        }
        setTitle(CURRENT_MODULE_TITLE);

        for (Course course:courses){
            if (course.getId().equalsIgnoreCase(AppConstants.USER_PROGRESS_COURSE_ID)){
                course.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(course.getId())<Integer.valueOf(AppConstants.USER_PROGRESS_COURSE_ID)){
                course.setStatus(Status.UNLOCKED);
            }else {
                course.setStatus(Status.LOCKED);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAdapter();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG","Restarted");
        if (databaseHelper==null){
            databaseHelper=new DatabaseHelper(this);
        }

        HashMap<String,String> progress=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),
                CurrentUserProgress.getInstance().getUserType());
        Log.d("TAG",progress.toString());

        String courseId=progress.get(AppConstants.KEY_COURSE_ID);

        for (Course course:courses){
            if (course.getId().equalsIgnoreCase(courseId)){
                course.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(course.getId())<Integer.valueOf(courseId)){
                course.setStatus(Status.UNLOCKED);
            }else {
                course.setStatus(Status.LOCKED);
            }
        }

    }

    private void setUpAdapter() {

        gridView.setAdapter(new CourseGridViewAdapter(this,courses));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (courses.get(position).isLocked()){
                    Helper.showToast(getApplicationContext(),getResources().getString(R.string.complete_other_courses), Toast.LENGTH_LONG);
                    return;
                }
                Intent intent = new Intent(CourseActivity.this,CourseContentActivity.class);
                CourseContentActivity.CURRENT_COURSE_TITLE=courses.get(position).getTitle();
                intent.putExtra(AppConstants.DATA,(ArrayList<Question>)courses.get(position).getQuestions());
                CurrentUserProgress.getInstance().setProgressCourse(courses.get(position).getId());
               // CourseContentActivity.questions = (ArrayList<Question>) courses.get(position).getQuestions();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
