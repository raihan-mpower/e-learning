package mpower.org.elearning_module.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

import mpower.org.elearning_module.application.ELearningApp;

/**
 *
 *
 * @author sabbir
 */

public class LocaleHelper {

    public void updateLocale(Context context, String localeCode) {
        Locale locale = getLocale(localeCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        context.getResources().updateConfiguration(configuration, displayMetrics);
        context.getApplicationContext().getResources().updateConfiguration(configuration, displayMetrics);
    }

    public void updateLocale(Context context) {
        String localeCode = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(AppConstants.KEY_APP_LANGUAGE, "");
        boolean isUsingSysLanguage = localeCode.equals("");
        if (isUsingSysLanguage) {
            localeCode = ELearningApp.defaultSysLanguage;
        }
        updateLocale(context, localeCode);
    }


    private Locale getLocale(String splitLocaleCode) {
        if (splitLocaleCode.contains("_")) {
            String[] arg = splitLocaleCode.split("_");
            return new Locale(arg[0], arg[1]);
        } else {
            return new Locale(splitLocaleCode);
        }
    }
}
