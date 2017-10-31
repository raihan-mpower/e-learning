package mpower.org.elearning_module;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.activities.LogInActivity;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.fragments.HomeFragment;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;
import mpower.org.elearning_module.utils.Utils;

/**
 * @author sabbir
 * */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserType userType = (UserType) getIntent().getSerializableExtra(AppConstants.USER_TYPE);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (userType !=null){
            editor.putInt(AppConstants.USER_TYPE, userType.ordinal());
            editor.apply();
            CurrentUserProgress.getInstance().setUserType(userType);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..Please Wait");
        progressDialog.setCancelable(false);

        //databaseHelper=new DatabaseHelper(this);
        //databaseHelper.getWritableDatabase();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkForPermission();

       TextView mTvLanguage = findViewById(R.id.tv_lang);

        Button logOutButton = (Button) findViewById(R.id.btn_logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"LogOut Button Clicked2",Toast.LENGTH_SHORT).show();
                logOutButtonClicked();
            }
        });


    }

    private void logOutButtonClicked() {
        Intent logoutIntent = new Intent(this, LogInActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    private void getUserData(){
        if (databaseHelper!=null){
            HashMap<String,String> progressMap= databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(), CurrentUserProgress.getInstance().getUserType());
            Log.d("TAG",progressMap.toString());
            if (progressMap.size() > 0){

                String module=progressMap.get(AppConstants.KEY_MODULE_ID);
                String course=progressMap.get(AppConstants.KEY_COURSE_ID);
                String question=progressMap.get(AppConstants.KEY_QUESTION_ID);

                AppConstants.USER_PROGRESS_MODULE_ID=module;
                AppConstants.USER_PROGRESS_COURSE_ID=course;
                AppConstants.USER_PROGRESS_QUESTION_ID=question;

            }
        }else {
            checkForPermission();
        }
    }


    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=23){
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
            }
        }else {
            ELearningApp.createDirectory();
            databaseHelper=new DatabaseHelper(this);
            databaseHelper.getWritableDatabase();
            copyAssets();
            getUserData();
            new JsonParserTask().execute();

        }
    }

    private void copyAssets() {
        if (isAlreadyCopied()){

        }else {
           new AsyncTask<Void,Void,Void>(){
               @Override
               protected Void doInBackground(Void... voids) {
                   Helper.CopyAssets(MainActivity.this, ELearningApp.IMAGES_FOLDER_NAME);
                   return null;
               }

               @Override
               protected void onPostExecute(Void aVoid) {
                   super.onPostExecute(aVoid);
                   SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                   SharedPreferences.Editor editor=prefs.edit();
                   editor.putBoolean(AppConstants.DATA_COPIED,true);
                   editor.apply();
               }
           }.execute();
        }

    }

    private boolean isAlreadyCopied() {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(AppConstants.DATA_COPIED,false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==23){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ELearningApp.createDirectory();
                databaseHelper=new DatabaseHelper(this);
                databaseHelper.getWritableDatabase();
                getUserData();
                copyAssets();
               // Helper.CopyAssets(this, ELearningApp.IMAGES_FOLDER_NAME);
                new JsonParserTask().execute();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_videos) {

        } else if (id == R.id.nav_doc) {

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_images) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   private class JsonParserTask extends AsyncTask<Void,Void,Void>{

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog.show();
       }

       @Override
       protected Void doInBackground(Void... voids) {
           ArrayList<Module> moduleArrayList = (ArrayList<Module>) CurriculumParser.returnCurriculum(Utils.readAssetContents("curriculum.json", MainActivity.this)).getModules();
           if (moduleArrayList !=null && moduleArrayList.size()>0){
               for (Module module: moduleArrayList){
                   databaseHelper.insertModule(module);
               }
           }
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           progressDialog.dismiss();

           callModuleFragment();
       }
   }

    private void callModuleFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
}
