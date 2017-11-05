package mpower.org.elearning_module.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabbir on 10/24/17.
 */

public class SaveFilesTask extends AsyncTask<List<String>,Integer,Boolean> {
    private FileSavingListener listener;
    private Context mContext;

    public interface  FileSavingListener{
        void onFileSaved();
    }


   public SaveFilesTask(Context context, @Nullable FileSavingListener listener ){
        mContext=context;
        if(listener!=null) this.listener=listener;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(List<String>... lists) {

        return false;
    }

}
