package mpower.org.elearning_module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mpower.org.elearning_module.activities.LogInActivity;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.fragments.HomeFragment;
import mpower.org.elearning_module.handlers.MyExceptionHandler;
import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.tasks.ExamCurriculamParserTask;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.LocaleHelper;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;
import mpower.org.elearning_module.utils.Utils;

/**
 * @author sabbir
 * */

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserType userType;
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

       // Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        userType = (UserType) getIntent().getSerializableExtra(AppConstants.USER_TYPE);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if (userType !=null){
            editor.putInt(AppConstants.USER_TYPE, userType.ordinal());
            editor.apply();
            CurrentUserProgress.getInstance().setUserType(userType);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkForPermission();

        TextView langTv= (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_lang));
        String lang=sharedPreferences.getString(AppConstants.KEY_APP_LANGUAGE,"");
        switch (lang){
            case "bn":
                langTv.setText(getResources().getText(R.string.bangla));
                break;
            case "en":
                langTv.setText(getResources().getText(R.string.english));
                break;
            default:
                langTv.setText(getResources().getText(R.string.english));
                break;
        }
        langTv.setGravity(Gravity.CENTER_VERTICAL);

        Button logOutButton = findViewById(R.id.btn_logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutButtonClicked();
            }
        });
    }

    private void logOutButtonClicked() {
        Intent logoutIntent = new Intent(this, LogInActivity.class);
        startActivity(logoutIntent);
        finish();
    }


    public void crashMe(View view){
        throw new NullPointerException();
    }

    private void getUserData(){
        if (databaseHelper!=null && userType!=null){
            HashMap<String,String> progressMap= databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(), CurrentUserProgress.getInstance().getUserType());
            Log.d("TAG",progressMap.toString());
            if (progressMap.size() > 0){

                String module=progressMap.get(AppConstants.KEY_MODULE_ID);
                String course=progressMap.get(AppConstants.KEY_COURSE_ID);
                String question=progressMap.get(AppConstants.KEY_QUESTION_ID);

                //was crashing with the constant feilds

                CurrentUserProgress.getInstance().setProgressCourse(course);
                CurrentUserProgress.getInstance().setProgressModule(module);
                CurrentUserProgress.getInstance().setProgressQuestion(question);

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
            //Helper.CopyAssets(this, ELearningApp.IMAGES_FOLDER_NAME);
            getUserData();
            new JsonParserTask().execute();

        }
    }

    private void copyAssets() {
        if (isAlreadyCopied()){

        }else {
            @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> copyTask=new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Helper.CopyAssets(getApplicationContext(), ELearningApp.IMAGES_FOLDER_NAME);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putBoolean(AppConstants.DATA_COPIED,true);
                    editor.commit();
                }
            };
          copyTask.execute();
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
                //Helper.CopyAssets(this, ELearningApp.IMAGES_FOLDER_NAME);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_lang:
                showChangeLangDialog();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_about_us:
                break;
            case R.id.nav_images:
                break;
            case R.id.nav_doc:
                break;
            case R.id.nav_videos:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle(getResources().getString(R.string.lang_dialog_title));
        dialogBuilder.setMessage(getResources().getString(R.string.lang_dialog_message));
        dialogBuilder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(AppConstants.KEY_APP_LANGUAGE, "en").commit();
                        setLangRecreate("en");
                        return;
                    case 1:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(AppConstants.KEY_APP_LANGUAGE, "bn").commit();
                        setLangRecreate("bn");
                        return;
                    default: //By default set to english
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(AppConstants.KEY_APP_LANGUAGE, "bn").commit();
                        setLangRecreate("bn");
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void setLangRecreate(String langval) {
        new LocaleHelper().updateLocale(this,langval);
        /*Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
        this.recreate();
        getUserData();
    }

   @SuppressLint("StaticFieldLeak")
   private class JsonParserTask extends AsyncTask<Void,Integer,Void>{


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
                   databaseHelper.insertModule(module,userType);
               }
           }
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           progressDialog.dismiss();
           ExamCurriculamParserTask examCurriculamParserTask=new ExamCurriculamParserTask(MainActivity.this, new ExamCurriculamParserTask.ExamCurriculamParserTaskListener() {
               @Override
               public void onParsingComplete(List<Exam> exams) {
                   if (exams!=null){
                       Log.d("TAG",""+exams.size());
                       for (Exam exam:exams){
                           databaseHelper.insertExam(exam,userType);
                       }
                   }

                   callModuleFragment();
               }
           });
           examCurriculamParserTask.execute("");

       }

       @Override
       protected void onProgressUpdate(Integer... values) {
           super.onProgressUpdate(values);
       }
   }

    private void callModuleFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
}
