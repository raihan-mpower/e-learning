package mpower.org.elearning_module.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mpower.org.elearning_module.application.ELearningApp;

/**
 * Created by sabbir on 12/4/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public final class SharedPrefUtils {
    private Context mContext;
    private SharedPreferences mPrefs;

    private static final String NULL_STRING=null;

    public SharedPrefUtils(Context context){
        mContext=context;
        mPrefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveString(String key,String value){
        if (mPrefs==null){
            setUpPrefs();
        }
        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return mPrefs.getString(key,NULL_STRING);
    }

    private void setUpPrefs() {
        if (mContext==null){
            mContext= ELearningApp.getInstance().getApplicationContext();
            //throw new RuntimeException("You have to provide a context for SharePrefs");
        }
        mPrefs= PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
