package mpower.org.elearning_module.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.parser.ExamCurriculumParser;
import mpower.org.elearning_module.utils.Utils;

/**
 * Created by sabbir on 11/12/17.
 */

public class ExamCurriculamParserTask extends AsyncTask<String,Void,List<Exam>> {
    private Context context;

    @Override
    protected List<Exam> doInBackground(String... strings) {
        return ExamCurriculumParser.returnCurriculum(Utils.readAssetContents("exam_json.json",context)).getExams();
    }

    public interface ExamCurriculamParserTaskListener{
        void onParsingComplete(List<Exam> exams);
    }

    private ExamCurriculamParserTaskListener listener;
    ProgressDialog progressDialog;

 public ExamCurriculamParserTask(Context context,ExamCurriculamParserTaskListener listener){
        this.context=context;
        this.listener=listener;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Parsing data...Please Wait");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Exam> exams) {
        super.onPostExecute(exams);
        progressDialog.dismiss();
        listener.onParsingComplete(exams);
    }
}
