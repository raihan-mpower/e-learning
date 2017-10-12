package mpower.org.elearning_module.application;

import android.app.Application;

/**
 * Created by sabbir on 10/12/17.
 */

public class ELearningApp extends Application {
    public static ELearningApp instance=null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

  public static ELearningApp getInstance(){
        return instance;
    }
}
