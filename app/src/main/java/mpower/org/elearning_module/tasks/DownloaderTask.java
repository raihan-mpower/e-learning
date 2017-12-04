package mpower.org.elearning_module.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import mpower.org.elearning_module.utils.Helper;

/**
 * Created by sabbir on 11/28/17.
 * @author sabbir
 */

public class DownloaderTask extends AsyncTask<String,Integer,Boolean> {

    public interface DownloaderTaskListener{
        void onFinished(boolean result);
    }

    private DownloaderTaskListener taskListener;
    private ArrayList<String> mFileNameList;
    private Context mContext;
    private ProgressDialog progressDialog;

    public DownloaderTask(Context context, ArrayList<String > fileNames,DownloaderTaskListener listener){
        mContext=context;
        mFileNameList=fileNames;
        taskListener=listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        for (int i=0;i<mFileNameList.size();i++){
            String url=strings[0];
            File file=new File(mFileNameList.get(i));
            try {
                Helper.downLoadFileWithJavaIO(file.getName(),url);
                publishProgress(i);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        return true;
    }

    private String getCurrentFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        taskListener.onFinished(aBoolean);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(10);
        progressDialog.setMax(mFileNameList.size());
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}
