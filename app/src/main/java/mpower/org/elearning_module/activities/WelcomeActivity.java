package mpower.org.elearning_module.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Locale;

import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.tasks.DownloaderTask;
import mpower.org.elearning_module.tasks.FileListParserTask;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.LocaleHelper;
import mpower.org.elearning_module.utils.NetUtils;

public class WelcomeActivity extends BaseActivity implements FileListParserTask.FileListParserTaskListener, DownloaderTask.DownloaderTaskListener {

    private Locale locale;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

        checkForPermission();

        setLocale();




    }

    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=23){
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
            }
        }else {
            ELearningApp.createDirectory();
            if (NetUtils.isConnected(this) && !isFilesAlreadyDownloaded()){
                //getFileUrlList();
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
                if (NetUtils.isConnected(this) && !isFilesAlreadyDownloaded()){
                    getFileUrlList();
                }
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

    private void getFileUrlList() {
        FileListParserTask fileListParserTask=new FileListParserTask(this,this);
        fileListParserTask.execute();
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
}
