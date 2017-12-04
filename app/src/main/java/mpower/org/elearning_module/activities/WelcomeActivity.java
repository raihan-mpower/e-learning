package mpower.org.elearning_module.activities;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.MainActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.tasks.DownloaderTask;
import mpower.org.elearning_module.tasks.ExamCurriculamParserTask;
import mpower.org.elearning_module.tasks.FileListParserTask;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.LocaleHelper;
import mpower.org.elearning_module.utils.NetUtils;
import mpower.org.elearning_module.utils.SharedPrefUtils;
import mpower.org.elearning_module.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity implements FileListParserTask.FileListParserTaskListener, DownloaderTask.DownloaderTaskListener, ExamCurriculamParserTask.ExamCurriculamParserTaskListener {

    private Locale locale;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> courseImageNames;
    private ArrayList<String> moduleImageNames;
    private ArrayList<String> questionImageNames;
    private ArrayList<String> questionAudioNames;

    private ArrayList<String> fileNameList;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

        fileNameList=new ArrayList<>();
        checkForPermission();

      //  setLocale();

      //new MediaFileDownLoaderTask().execute();


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
            if (NetUtils.isConnected(this) && !isFilesAlreadyDownloaded()){
                //getFileUrlList();
                getDataFromServer();
            }
        }
    }

    private boolean isFilesAlreadyDownloaded() {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(AppConstants.FILES_DOWNLOADED,false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==23){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ELearningApp.createDirectory();
                databaseHelper=new DatabaseHelper(this);
                databaseHelper.getWritableDatabase();
                if (NetUtils.isConnected(this) && !isFilesAlreadyDownloaded()){
                   // getFileUrlList();
                    getDataFromServer();
                }
            }else {
                showSimpleDialog("Error","Need to have permissions to save files");
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

    private void getDataFromServer() {
        new JsonParserTask(this).execute();
    }

    private void getFileUrlList() {
       /*
        FileListParserTask fileListParserTask=new FileListParserTask(this,this);
        fileListParserTask.execute();*/

       DownloaderTask downloaderTask=new DownloaderTask(this,fileNameList,this);
       downloaderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,getServerUrl());
    }

    private String getServerUrl() {
      return new SharedPrefUtils(this).getString(AppConstants.KEY_SERVER_URL);
    }

    private void setLocale() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString(AppConstants.KEY_APP_LANGUAGE, "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    //individual onclick handler for buttons ,using just to not use button ids and other view referencing
    public void callLogin(View view) {
        Intent intent=new Intent(this,LogInActivity.class);
        startActivity(intent);
    }

    public void callSignUp(View view) {
        Intent intent=new Intent(this,UserTypeSelectionActivity.class);
        startActivity(intent);
    }

    public void showChangeLangDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle(getResources().getString(R.string.lang_dialog_title));
        dialogBuilder.setMessage(getResources().getString(R.string.lang_dialog_message));
        dialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int langpos = spinner1.getSelectedItemPosition();
                switch(langpos) {
                    case 0: //English
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        setLangRecreate("en");
                        return;
                    case 1:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "bn").commit();
                        setLangRecreate("bn");
                        return;
                    default: //By default set to english
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "bn").commit();
                        setLangRecreate("bn");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
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
        recreate();
    }

    @Override
    public void onFileListParsed(ArrayList<String> fileUrlList) {
        if (fileUrlList!=null){
            downLoadFiles(fileUrlList);
        }
    }

    private void downLoadFiles(ArrayList<String> fileUrlList) {
        DownloaderTask downloaderTask=new DownloaderTask(this,fileUrlList,this);
        downloaderTask.execute();
    }

    @Override
    public void onFinished(boolean result) {
        if (!result){
            showToast("Error while downloading files from server");
        }
        showToast("Files Downloaded from Server");
    }

    @Override
    public void onParsingComplete(List<Exam> exams) {
        insertExams((ArrayList<Exam>) exams);
    }


    class MediaFileDownLoaderTask extends AsyncTask<String,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String url="http://192.168.22.114:3000/files";
            String json=getJson(url);
            try {
                ArrayList<String > list=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(json);
                for(int i=0;i<jsonArray.length();i++){
                    list.add(jsonArray.getString(i));
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            super.onPostExecute(arrayList);

            DownloaderTask downloaderTask=new DownloaderTask(WelcomeActivity.this,arrayList,WelcomeActivity.this);
            downloaderTask.execute();
        }
    }

    private String getJson(String url) {
        OkHttpClient okHttpClient=new OkHttpClient();

        Request request=new Request.Builder().url(url).build();

        try {
            Response response=okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class JsonParserTask extends AsyncTask<Void,Integer,Void>{

        private WeakReference<WelcomeActivity> activityWeakReference;
        private ProgressDialog progressDialog;

        JsonParserTask(WelcomeActivity activity){
            this.activityWeakReference= new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(activityWeakReference.get());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(1);
            progressDialog.setMax(4);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Course> courseArrayList =
                    (ArrayList<Course>) CurriculumParser.returnCurriculum(Utils.readAssetContents("curriculum.json", this.activityWeakReference.get()),false)
                            .getCourses();
            String url="";

            //  ArrayList<Course> courseArrayList = (ArrayList<Course>) CurriculumParser.returnCurriculum(getJson(url),true).getCourses();
            if (courseArrayList !=null && courseArrayList.size()>0){

                for (int i=0;i<courseArrayList.size();i++){
                    Course course= courseArrayList.get(i);
                    activityWeakReference.get().databaseHelper.insertCourse(course);
                    activityWeakReference.get().fileNameList.add(course.getIconImage());
                    for (Module module:course.getModules()){
                        activityWeakReference.get().fileNameList.add(module.getIconImage());
                        for (Question question:module.getQuestions()){
                            activityWeakReference.get().fileNameList.add(question.getImage());
                            activityWeakReference.get().fileNameList.add(question.getAudio());
                        }
                    }
                    publishProgress(i);
                }
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            ExamCurriculamParserTask examCurriculamParserTask=new ExamCurriculamParserTask(this.activityWeakReference.get(), this.activityWeakReference.get());
            examCurriculamParserTask.execute("");

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

    }

   public void insertExams(ArrayList<Exam> exams){
        if (exams!=null){
            Log.d("TAG",""+exams.size());
            for (Exam exam:exams){
                databaseHelper.insertExam(exam);
            }

            //getFileUrlList();
        }
    }
}
