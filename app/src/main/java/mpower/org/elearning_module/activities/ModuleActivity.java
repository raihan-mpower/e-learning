package mpower.org.elearning_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.MainActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserCollection;

public class ModuleActivity extends BaseActivity {

    public static String CURRENT_MODULE_TITLE="";
    private GridView gridView;
    private ArrayList<Module> modules;
    public static String CURRENT_MODULE_ID="";
    DatabaseHelper databaseHelper;

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = findViewById(R.id.gridView1);

        if (getIntent().getExtras()!=null){
            modules= (ArrayList<Module>) getIntent().getExtras().get(AppConstants.DATA);
        }
        setTitle(CURRENT_MODULE_TITLE);
        if (modules==null){
            databaseHelper=new DatabaseHelper(this);
            HashMap<String,String> progress=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),
                    CurrentUserProgress.getInstance().getUserType());
            Log.d("TAG",progress.toString());

            String courseId=progress.get(AppConstants.KEY_COURSE_ID);
            String moduleId=progress.get(AppConstants.KEY_MODULE_ID);

            modules=databaseHelper.getAllModules(courseId);

            if (modules==null){
                // there are no modules for this module go back to module fragment or MainActivity
                goBackToMainActivity();
            }else {
                for (Module module:modules){
                    if (module.getId().equalsIgnoreCase(moduleId)){
                        module.setStatus(Status.UNLOCKED);
                    }else if (Integer.valueOf(module.getId())<Integer.valueOf(moduleId)){
                        module.setStatus(Status.UNLOCKED);
                    }else {
                        module.setStatus(Status.LOCKED);
                    }
                }
            }



        }else {
            for (Module module:modules){
                if (module.getId().equalsIgnoreCase(CurrentUserProgress.getInstance().getCurrentUserModuleProgress())){
                    module.setStatus(Status.UNLOCKED);
                }else if (Integer.valueOf(module.getId())<Integer.valueOf(CurrentUserProgress.getInstance().getCurrentUserModuleProgress())){
                    module.setStatus(Status.UNLOCKED);
                }else {
                    module.setStatus(Status.LOCKED);
                }
            }
        }
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_course;
    }

    private void goBackToMainActivity() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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

        String moduelId=progress.get(AppConstants.KEY_COURSE_ID);

        for (Module module:modules){
            if (module.getId().equalsIgnoreCase(moduelId)){
                module.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(module.getId())<Integer.valueOf(moduelId)){
                module.setStatus(Status.UNLOCKED);
            }else {
                module.setStatus(Status.LOCKED);
            }
        }

    }

    private void setUpAdapter() {

        gridView.setAdapter(new ModuleGridViewAdapter(this,modules));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (modules.get(position).isLocked()){
                    //Helper.showToast(getApplicationContext(),getResources().getString(R.string.complete_other_modules), Toast.LENGTH_LONG);
                    showSimpleDialog(getString(R.string.locked),getString(R.string.complete_other_modules));
                    return;
                }
                Intent intent = new Intent(ModuleActivity.this,ModuleContentActivity.class);
                ModuleContentActivity.CURRENT_MODULE_TITLE=modules.get(position).getTitle();
                intent.putExtra(AppConstants.DATA,(ArrayList<Question>)modules.get(position).getQuestions());
                CurrentUserProgress.getInstance().setProgressCourse(modules.get(position).getId());
               // ModuleContentActivity.questions = (ArrayList<Question>) modules.get(position).getQuestions();
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
