package mpower.org.elearning_module.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sabbir on 11/28/17.
 */

public class FileListParserTask extends AsyncTask<String,Void,ArrayList<String>> {

    public interface FileListParserTaskListener{
        void onFileListParsed(ArrayList<String> fileUrlList);
    }

    private FileListParserTaskListener listener;
    private Context mContext;

   public FileListParserTask(Context context,FileListParserTaskListener taskListener){
        listener=taskListener;
        mContext=context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
       ArrayList<String> urlList;
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(strings[0])
                .build();

        try {
            Response response=okHttpClient.newCall(request).execute();
            String json=response.body().string();
            JSONArray jsonArray=new JSONArray(json);
            urlList=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                String url=jsonArray.getString(i);
                urlList.add(url);
            }
            return urlList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<String> arrayList) {
        super.onPostExecute(arrayList);
        listener.onFileListParsed(arrayList);
    }
}
