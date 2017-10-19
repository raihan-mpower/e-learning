package mpower.org.elearning_module.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 10/17/17.
 */

public class DatabaseHelper extends CustomDbOpenHelper {

    private static final String TAG="DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="tb_e_learn.db";

    private static final String USER_NAME="user_name";
    private static final String USER_TYPE="user_type";
    private static final String PROGRESS_TABLE="progress_table";

    private static final String MODULE_TABLE="module_table";
    private static final String MODULE_ID="module_id";
    private static final String MODULE_TITLE="module_title";
    private static final String MODULE_ICON_IMAGE_NAME="module_icon_image";
    private static final String MODULE_STATUS="module_status";



    private static final String COURSE_TABLE="course_table";
    private static final String COURSE_ID="course_id";
    private static final String COURSE_TITLE="course_title";
    private static final String COURSE_ICON_IMAGE_NAME="course_icon_image";
    private static final String COURSE_STATUS="course_status";

    private static final String QUESTION_TABLE="question_table";
    private static final String QUESTION_ID="question_id";
    private static final String QUESTION_TITLE="question_title";
    private static final String QUESTION_IMAGE_NAME="question_icon_image";
    private static final String QUESTION_TYPE="question_type";
    private static final String QUESTION_DESCRIPTION="question_description";
    private static final String QUESTION_ANSWER="question_answer";
    private static final String QUESTION_RIGHT_ANSWER="question_right_answer";
    private static final String QUESTION_TRUE_FALSE="question_true_false";


   public DatabaseHelper(Context context)  {
       super(ELearningApp.DATABASE_FOLDER_NAME,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        createModuleTable(db);
        createCourseTable(db);
        createQuestionsTable(db);
        createProgressTable(db);
    }

    private void createProgressTable(SQLiteDatabase db) {
        String sql="CREATE TABLE IF NOT EXISTS "+PROGRESS_TABLE+" ( "+USER_NAME+" TEXT ,"+MODULE_ID+" TEXT, "+
                COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT,"+USER_TYPE+" INTEGER "+");";
        db.execSQL(sql);
    }

    private void createQuestionsTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+QUESTION_TABLE+" ( "+COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT, "+QUESTION_TITLE+" TEXT, "+
                QUESTION_DESCRIPTION+" TEXT, "+QUESTION_TYPE+" INTEGER, "+QUESTION_IMAGE_NAME+" TEXT, "+
                QUESTION_ANSWER+" TEXT, "+QUESTION_RIGHT_ANSWER+" TEXT, "+QUESTION_TRUE_FALSE+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);

    }

    private void createCourseTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+COURSE_TABLE+" ( "+MODULE_ID+" TEXT, "+COURSE_ID+" TEXT, "+COURSE_TITLE+" TEXT, "+
                COURSE_STATUS+" INTEGER ,"+COURSE_ICON_IMAGE_NAME+" TEXT "+QUESTION_ID+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);
    }

    private void createModuleTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+MODULE_TABLE+" ( "+MODULE_ID+" TEXT, "+MODULE_TITLE+" TEXT, "+
                MODULE_STATUS+" TEXT, "+MODULE_ICON_IMAGE_NAME+" TEXT"+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS "+MODULE_TABLE+","+COURSE_TABLE+","+QUESTION_TABLE+";";
        db.execSQL(sql);
        onCreate(db);
    }

    public void saveInProgressTable(String userName,String moduleId,String courseId,String questionId,UserType userType){
        ContentValues cv=new ContentValues();
        cv.put(USER_NAME,userName);
        cv.put(MODULE_ID,moduleId);
        cv.put(COURSE_ID,courseId);
        cv.put(QUESTION_ID,questionId);
        cv.put(USER_TYPE,userType.ordinal());

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(PROGRESS_TABLE,null,cv);
    }

    public HashMap<String,String> getProgressForUser(String userName, UserType userType){
        String s="SELECT * FROM "+PROGRESS_TABLE+" WHERE "+USER_NAME+" = '"+userName+"'"+" AND "+USER_TYPE+" = '"+userType.ordinal()+"'";
        Cursor cursor=this.getWritableDatabase().rawQuery(s,null);
        HashMap<String,String> dataMap=new HashMap<>();
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();

            do {
                dataMap.put("moduleId",cursor.getString(cursor.getColumnIndex(MODULE_ID)));
                dataMap.put("courseId",cursor.getString(cursor.getColumnIndex(COURSE_ID)));
                dataMap.put("questionId",cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
            }while (cursor.moveToNext());
            cursor.close();
            return dataMap;
        }
        dataMap.put("moduleId","1");
        dataMap.put("courseId","1");
        dataMap.put("questionId","1");
        return dataMap;
    }

    public void insertModule(Module module) {
        if (checkIsDataAlreadyInDBorNot(MODULE_TABLE,MODULE_ID,module.getId())){
            return;
        }
        ContentValues cv=new ContentValues();
        cv.put(MODULE_ID,module.getId());
        cv.put(MODULE_TITLE,module.getTitle());
        cv.put(MODULE_ICON_IMAGE_NAME,module.getIconImage());

        for (Course course:module.getCourses()){
            Log.d(TAG,course.toString());
            insertCourses(module.getId(),course);
        }

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(MODULE_TABLE,null,cv);
    }

    private void insertCourses(String id, Course course) {
        ContentValues cv=new ContentValues();
        cv.put(MODULE_ID,id);
        cv.put(COURSE_ID,course.getId());
        cv.put(COURSE_TITLE,course.getTitle());
        cv.put(COURSE_ICON_IMAGE_NAME,course.getIconImage());
        cv.put(COURSE_STATUS,course.getStatus().ordinal());

        for (Question question:course.getQuestions()){
            Log.d(TAG,question.toString());
            insertQuestion(course.getId(),question);
        }

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(COURSE_TABLE,null,cv);
    }

    private void insertQuestion(String id, Question question) {
        ContentValues cv =new ContentValues();
        cv.put(COURSE_ID,id);
        cv.put(QUESTION_ID,question.getId());
        cv.put(QUESTION_TITLE,question.getTitleText());
        cv.put(QUESTION_ANSWER,question.getAnswer());
        cv.put(QUESTION_DESCRIPTION,question.getDescriptionText());
        cv.put(QUESTION_RIGHT_ANSWER,question.getRightAnswer());
        cv.put(QUESTION_ID,question.getId());
        cv.put(QUESTION_TYPE,question.getType());

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(QUESTION_TABLE,null,cv);
    }

    public boolean checkIsDataAlreadyInDBorNot(String TableName,
                                               String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Module> getAllModules(@Nullable String userName, @Nullable UserType userType){
        String sql="SELECT * FROM "+MODULE_TABLE;
        if (userName!=null) sql+=" WHERE "+USER_NAME+" = '"+userName+"'";
        ArrayList<Module> modules;
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            modules=new ArrayList<>();
            do {
                Module module=new Module();
                module.setId(cursor.getString(cursor.getColumnIndex(MODULE_ID)));
                module.setTitle(cursor.getString(cursor.getColumnIndex(MODULE_TITLE)));
                module.setIconImage(cursor.getString(cursor.getColumnIndex(MODULE_ICON_IMAGE_NAME)));
                module.setCourses(getAllCourses(module.getId()));

                modules.add(module);
            }while (cursor.moveToNext());
            cursor.close();
            return modules;
        }

        return null;
    }

    private ArrayList<Course> getAllCourses(String id) {
        String sql="SELECT * FROM "+COURSE_TABLE+" WHERE "+MODULE_ID+" = '"+id+"'";
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        ArrayList<Course> courses;
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            courses=new ArrayList<>();
            do {
                Course course=new Course();
                course.setId(cursor.getString(cursor.getColumnIndex(COURSE_ID)));
                course.setTitle(cursor.getString(cursor.getColumnIndex(COURSE_TITLE)));
                course.setIconImage(cursor.getString(cursor.getColumnIndex(COURSE_ICON_IMAGE_NAME)));
                course.setQuestions(getAllQuestions(course.getId()));

                courses.add(course);
            }while (cursor.moveToNext());
            cursor.close();
            return courses;
        }
        return null;
    }

    private List<Question> getAllQuestions(String id) {
        String sql="SELECT * FROM "+QUESTION_TABLE+" WHERE "+COURSE_ID+" = '"+id+"'";
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        ArrayList<Question> questions;
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            questions=new ArrayList<>();
            do {
                Question question=new Question();
                question.setId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                question.setTitleText(cursor.getString(cursor.getColumnIndex(QUESTION_TITLE)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(QUESTION_ANSWER)));
                question.setRightAnswer(cursor.getString(cursor.getColumnIndex(QUESTION_RIGHT_ANSWER)));
                question.setImage(cursor.getString(cursor.getColumnIndex(QUESTION_IMAGE_NAME)));
                question.setType(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                question.setDescriptionText(cursor.getString(cursor.getColumnIndex(QUESTION_DESCRIPTION)));
                question.setTrueFalse(cursor.getString(cursor.getColumnIndex(QUESTION_TRUE_FALSE)));

                questions.add(question);

            }while (cursor.moveToNext());
            cursor.close();
            return questions;

        }
        return null;
    }

    public ArrayList<Module> getModules(String moduleId) {
        String sql="SELECT * FROM "+MODULE_TABLE+" WHERE "+MODULE_ID+" = '"+moduleId+"'";
        ArrayList<Module> modules;
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            modules=new ArrayList<>();
            do {
                Module module=new Module();
                module.setId(cursor.getString(cursor.getColumnIndex(MODULE_ID)));
                module.setTitle(cursor.getString(cursor.getColumnIndex(MODULE_TITLE)));
                module.setIconImage(cursor.getString(cursor.getColumnIndex(MODULE_ICON_IMAGE_NAME)));
                module.setCourses(getAllCourses(module.getId()));

                modules.add(module);
            }while (cursor.moveToNext());
            cursor.close();
            return modules;
        }

        return null;
    }
}
