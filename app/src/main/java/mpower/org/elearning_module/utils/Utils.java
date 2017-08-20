package mpower.org.elearning_module.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by raihan on 8/20/17.
 */

public class Utils {
    public static String readAssetContents(String path, Context context) {
        String fileContents = null;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            fileContents = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            android.util.Log.e("utils",ex.getMessage());
        }

        return fileContents;
    }
}
