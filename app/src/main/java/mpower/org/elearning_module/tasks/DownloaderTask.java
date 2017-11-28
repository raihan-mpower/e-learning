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
    private ArrayList<String> mUrlList;
    private Context mContext;
    private ProgressDialog progressDialog;

    public DownloaderTask(Context context, ArrayList<String > urlLst,DownloaderTaskListener listener){
        mContext=context;
        mUrlList=urlLst;
        taskListener=listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        for (int i=0;i<mUrlList.size();i++){
            String url=mUrlList.get(i);
            File file=new File(getCurrentFileName(url)) ;
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
        progressDialog.setMax(mUrlList.size());
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}
