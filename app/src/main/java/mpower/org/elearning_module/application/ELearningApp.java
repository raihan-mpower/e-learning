package mpower.org.elearning_module.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import java.io.File;
import java.util.Locale;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.handlers.MyExceptionHandler;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.LocaleHelper;

/**
 * Created by sabbir on 10/12/17.
 */

public class ELearningApp extends Application {

    public static String defaultSysLanguage;
    public static ELearningApp instance=null;
    public static final String ROOT_FOLDER_NAME= Environment.getExternalStorageDirectory()+File.separator+"Tb_eLearning";
    public static final String DATABASE_FOLDER_NAME=ROOT_FOLDER_NAME+ File.separator+"databases";
    public static final String IMAGES_FOLDER_NAME=ROOT_FOLDER_NAME+ File.separator+"images";


    @Override
    public void onCreate() {
        super.onCreate();
        defaultSysLanguage = Locale.getDefault().getLanguage();
        new LocaleHelper().updateLocale(this,"bn");
        instance=this;
        //TODO DISABLE it ,for crash handling but for testing enable it
       // Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    @SuppressLint("StringFormatInvalid")
   static synchronized private void createDirs() throws Exception {
       String cardstatus = Environment.getExternalStorageState();
       if (!cardstatus.equals(Environment.MEDIA_MOUNTED)) {
           throw new RuntimeException(
                   ELearningApp.getInstance().getString(R.string.sdcard_unmounted, cardstatus));
       }

       String[] dirs = {
               ROOT_FOLDER_NAME,DATABASE_FOLDER_NAME,IMAGES_FOLDER_NAME
       };

       for (String dirName : dirs) {
           File dir = new File(dirName);
           if (!dir.exists()) {
               if (!dir.mkdirs()) {
                   throw new RuntimeException("Cannot create directory: "
                           + dirName);
               }
           } else {
               if (!dir.isDirectory()) {
                   throw new RuntimeException("E-LearningAPP " + dirName
                           + " exists, but is not a directory");
               }
           }
       }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static ELearningApp getInstance(){
        return instance;
    }

    public static void createDirectory() {
        try {
            createDirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        defaultSysLanguage = newConfig.locale.getLanguage();
        boolean isUsingSysLanguage = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(AppConstants.KEY_APP_LANGUAGE, "").equals("");
        if (!isUsingSysLanguage) {
            new LocaleHelper().updateLocale(this);
        }
    }
}
