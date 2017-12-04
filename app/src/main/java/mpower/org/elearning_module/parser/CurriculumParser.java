package mpower.org.elearning_module.parser;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Curriculum;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.AppConstants;

/**
 * Created by raihan on 8/20/17.
 */

public class CurriculumParser {

    public static String getTimeStamp(String json) throws Exception{
        JSONArray jsonArray=new JSONArray(json);
        return jsonArray.getJSONObject(0).getString(AppConstants.KEY_TIME_STAMP);
    }

    public static void saveTimeStampInPrefs(SharedPreferences preferences,String timeStamp){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(AppConstants.KEY_TIME_STAMP,timeStamp);
        editor.commit();
    }
    

    public static Curriculum returnCurriculum (String toParse,boolean isApi){
        Gson gson = new Gson();
        Curriculum curriculum=null;
        try {
            JSONArray jsonArray=new JSONArray(toParse);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            JSONObject curriculuObject=jsonObject.getJSONObject("curriculum");
            curriculum = gson.fromJson(curriculuObject.toString(),Curriculum.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject curriculumjson = new JSONObject(toParse);
            //curriculumjson = curriculumjson.getJSONObject("curriculum");
            JSONArray courseArray;
            if (isApi){
                courseArray=curriculumjson.getJSONArray("courses");
            }else {
                curriculumjson = curriculumjson.getJSONObject("curriculum");
                courseArray = curriculumjson.getJSONArray("courses");
            }


            ArrayList<Course> courses = getCourses(gson,courseArray);

            curriculum.setCourses(courses);
        }catch (Exception e){

        }
        return curriculum;
    }

    private static ArrayList<Module> getModules(Gson gson, JSONArray moduleArray) {
        ArrayList<Module> modules = new ArrayList<>();
        for(int i = 0;i<moduleArray.length();i++){
            try {
                Module module = gson.fromJson(moduleArray.getJSONObject(i).toString(), Module.class);
                module.setQuestions(getQuestions(gson,moduleArray.getJSONObject(i).getJSONArray("questions")));
                modules.add(module);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return modules;
    }

    private static ArrayList<Course> getCourses(Gson gson, JSONArray courses) {
        ArrayList<Course> courseslist = new ArrayList<>();
        for(int i = 0;i<courses.length();i++){
            try {
                Course course = gson.fromJson(courses.getJSONObject(i).toString(), Course.class);
                course.setModules(getModules(gson, courses.getJSONObject(i).getJSONArray("modules")));
                courseslist.add(course);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return courseslist;
    }

    private static ArrayList<Question> getQuestions(Gson gson, JSONArray questions) {
        ArrayList<Question> questionslist = new ArrayList<>();
        for(int i = 0;i<questions.length();i++){
            try {
                Question question = gson.fromJson(questions.getJSONObject(i).toString(), Question.class);
                questionslist.add(question);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return questionslist;
    }
}
