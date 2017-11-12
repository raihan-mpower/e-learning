package mpower.org.elearning_module.parser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.ExamCurriculum;
import mpower.org.elearning_module.model.ExamQuestion;

/**
 * Created by sabbir on 11/12/17.
 */

public class ExamCurriculumParser {

    public static ExamCurriculum returnCurriculum (String toParse){
        Gson gson = new Gson();
        ExamCurriculum curriculum = gson.fromJson(toParse,ExamCurriculum.class);
        try {
            JSONObject curriculumjson = new JSONObject(toParse);
            curriculumjson = curriculumjson.getJSONObject("exam_curriculum");
            JSONArray exameArray = curriculumjson.getJSONArray("exams");

            ArrayList<Exam> exams = getExams(gson,exameArray);

            curriculum.setExams(exams);
        }catch (Exception e){

        }
        return curriculum;
    }

    private static ArrayList<Exam> getExams(Gson gson, JSONArray moduleArray) {
        ArrayList<Exam> exams = new ArrayList<>();
        for(int i = 0;i<moduleArray.length();i++){
            try {
                Exam exam = gson.fromJson(moduleArray.getJSONObject(i).toString(), Exam.class);
                exam.setExamQuestions(getExamQuestions(gson,moduleArray.getJSONObject(i).getJSONArray("exam_questions")));
                exams.add(exam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return exams;
    }

    private static ArrayList<ExamQuestion> getExamQuestions(Gson gson, JSONArray courses) {
        ArrayList<ExamQuestion> examQuestions = new ArrayList<>();
        for(int i = 0;i<courses.length();i++){
            try {
                ExamQuestion examQuestion = gson.fromJson(courses.getJSONObject(i).toString(), ExamQuestion.class);
                examQuestion.setAnswer(getAnswer(gson, courses.getJSONObject(i).getJSONArray("answer")));
                examQuestions.add(examQuestion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return examQuestions;
    }

    private static ArrayList<String> getAnswer(Gson gson, JSONArray questions) {
        ArrayList<String> answerList = new ArrayList<>();
        for(int i = 0;i<questions.length();i++){
            try {

                String answer=questions.getString(i);
                answerList.add(answer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return answerList;
    }
}
