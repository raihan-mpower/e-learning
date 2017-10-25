package mpower.org.elearning_module.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Environment;

import java.io.File;

import mpower.org.elearning_module.R;

/**
 * Created by sabbir on 10/12/17.
 */

public class ELearningApp extends Application {
    public static ELearningApp instance=null;
    public static final String ROOT_FOLDER_NAME= Environment.getExternalStorageDirectory()+File.separator+"TB_ELEARNING";
    public static final String DATABASE_FOLDER_NAME=ROOT_FOLDER_NAME+ File.separator+"databases";
    public static final String IMAGES_FOLDER_NAME=ROOT_FOLDER_NAME+ File.separator+"images";
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;


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
}
