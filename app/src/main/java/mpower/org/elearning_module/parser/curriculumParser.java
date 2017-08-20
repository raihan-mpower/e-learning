package mpower.org.elearning_module.parser;

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

/**
 * Created by raihan on 8/20/17.
 */

public class curriculumParser {

    public static Curriculum returnCurriculum (String toParse){
        Gson gson = new Gson();
        Curriculum curriculum = gson.fromJson(toParse,Curriculum.class);
        try {
            JSONObject curriculumjson = new JSONObject(toParse);
            curriculumjson = curriculumjson.getJSONObject("curriculum");
            JSONArray moduleArray = curriculumjson.getJSONArray("modules");

            ArrayList<Module> modules = getModules(gson,moduleArray);

            curriculum.setModules(modules);
        }catch (Exception e){

        }
        return curriculum;
    }

    private static ArrayList<Module> getModules(Gson gson, JSONArray moduleArray) {
        ArrayList<Module> modules = new ArrayList<Module>();
        for(int i = 0;i<moduleArray.length();i++){
            try {
                Module module = gson.fromJson(moduleArray.getJSONObject(i).toString(), Module.class);
                module.setCourses(getCourses(gson,moduleArray.getJSONObject(i).getJSONArray("courses")));
                modules.add(module);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return modules;
    }

    private static ArrayList<Course> getCourses(Gson gson, JSONArray courses) {
        ArrayList<Course> courseslist = new ArrayList<Course>();
        for(int i = 0;i<courses.length();i++){
            try {
                Course course = gson.fromJson(courses.getJSONObject(i).toString(), Course.class);
                course.setQuestions(getQuestions(gson, courses.getJSONObject(i).getJSONArray("questions")));
                courseslist.add(course);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return courseslist;
    }

    private static ArrayList<Question> getQuestions(Gson gson, JSONArray questions) {
        ArrayList<Question> questionslist = new ArrayList<Question>();
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
