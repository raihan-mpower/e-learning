package mpower.org.elearning_module.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;

import mpower.org.elearning_module.application.ELearningApp;

/**
 * Created by sabbir on 7/11/17.
 */

public class Helper {
    private static final String TAG="Helper";

    public static void CopyAssets(Context context,String folderName) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        if (files != null) {
            for(String filename : files) {
                Log.d(TAG,"File name => "+filename);
                InputStream in;
                OutputStream out;
                try {
                    in = assetManager.open(filename);   // if files resides inside the "Files" directory itself
                    out = new FileOutputStream(folderName +"/" + filename);
                    Log.d(TAG,"CopyAssets "+ Environment.getExternalStorageDirectory().toString()+folderName +"/" + filename);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                } catch(Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (String file : files) {
                    String src1 = (new File(src, file).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static void showToast(Context context,String message,int duration) {
        Toast.makeText(context, message, duration).show();
    }

    private class CityName{
        Double lat,lon;
        Context mContext;
        CityName(Context context, String lat, String lon){
            this.lat= Double.valueOf(lat);
            this.lon= Double.valueOf(lon);
            this.mContext=context;
        }

        public String getCityName(){
            String cityName="";
            Geocoder geocoder=new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses=geocoder.getFromLocation(lat,lon,1);
                cityName=addresses.get(0).getAddressLine(0);
                String statename=addresses.get(0).getAddressLine(1);
                String countryName=addresses.get(0).getAddressLine(2);
                //  Log.i("GeoCoder",cityName+statename+countryName);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return cityName;
        }

    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static void MakeLog(Class c,String message){
        Log.d(c.getSimpleName(),message);
    }
}
