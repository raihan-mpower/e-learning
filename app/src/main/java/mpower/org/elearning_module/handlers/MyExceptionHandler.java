package mpower.org.elearning_module.handlers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import mpower.org.elearning_module.activities.WelcomeActivity;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.utils.AppConstants;

/**
 * Created by sabbir on 11/20/17.
 */

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private AppCompatActivity appCompatActivity;
    private Context mContext;
    public MyExceptionHandler(Context context){
        this.mContext=context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent=new Intent(mContext, WelcomeActivity.class);
        intent.putExtra(AppConstants.KEY_CRAHSED,true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
        Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent=PendingIntent.getActivity(ELearningApp.getInstance().getBaseContext(),
                0,intent,PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager= (AlarmManager) ELearningApp.getInstance().getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+1,pendingIntent);
        System.exit(2);
    }
}
