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
import mpower.org.elearning_module.model.Exam;
import mpower.org.elearning_module.model.ExamQuestion;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.UserType;

/**
 * Created by sabbir on 10/17/17.
 * @author sabbir
 */

public class DatabaseHelper extends CustomDbOpenHelper {

    private static final String TAG=DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="tb_e_learn.db";
    private static final String _ID="_id";

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
    private static final String QUESTION_AUDIO_NAME="question_audio_name";
    private static final String QUESTION_TYPE="question_type";
    private static final String QUESTION_DESCRIPTION="question_description";
    private static final String QUESTION_ANSWER="question_answer";
    private static final String QUESTION_WRONG_ANSWER="question_wrong_answer";
    private static final String QUESTION_RIGHT_ANSWER="question_right_answer";
    private static final String QUESTION_TRUE_FALSE="question_true_false";

    private static final String EXAM_TABLE="exam_table";
    private static final String EXAM_ID="exam_id";
    private static final String EXAM_TITLE="exam_title";

    private static final String EXAM_QUESTION_TABLE="exam_question_table";
    private static final String EXAM_QUESTION_ID="exam_question_id";
    private static final String EXAM_QUESTION_TITLE="exam_question_title";
    private static final String EXAM_QUESTION_IMAGE_NAME="exam_question_icon_image";
    private static final String EXAM_QUESTION_AUDIO_NAME="exam_question_audio_name";
    private static final String EXAM_QUESTION_TYPE="exam_question_type";
    private static final String EXAM_QUESTION_DESCRIPTION="exam_question_description";
    private static final String EXAM_QUESTION_ANSWER="exam_question_answer";
    private static final String EXAM_QUESTION_RIGHT_ANSWER="exam_question_right_answer";
    private static final String EXAM_QUESTION_TRUE_FALSE="exam_question_true_false";

    private static final String EXAM_QUESTION_ANSWER_TABLE="exam_question_answer_table";

    private static final String TOTAL_MODULES_FOR_THIS_COURSE="total_module_courses";

    private static final String EXAM_PROGRESS_TABLE="exam_progress_table";
    private static final String PREVEIOUS_EXAM_QUESTIONS="prev_exam_questions";
    private static final String EXAM_SCORE="exam_score";
    private static final String EXAM_TOTAL_QUESTIONS="exam_total_ques";



   public DatabaseHelper(Context context)  {
       super(ELearningApp.DATABASE_FOLDER_NAME,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        TableCreator.createTables(db,getAllTheTablesSQL());

       /* createModuleTable(db);
        createCourseTable(db);
        createQuestionsTable(db);
        createProgressTable(db);
        createExamTable(db);
        createExamQuestionTable(db);
        createExamQuestionAnswerTable(db);
        createExamProgressTable(db);*/
    }

    private List<String> getAllTheTablesSQL() {
       List<String> sqlList=new ArrayList<>();

        String examProgresssql=EXAM_PROGRESS_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY,"
                +USER_NAME+" TEXT, "+EXAM_TOTAL_QUESTIONS+" INTEGER, "+EXAM_SCORE+" INTEGER, "+EXAM_ID+" TEXT, "+PREVEIOUS_EXAM_QUESTIONS+" TEXT "
                +" );";

        String examQuestionAnswerTablesql=EXAM_QUESTION_ANSWER_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "
                +EXAM_ID+" TEXT, "
                +EXAM_QUESTION_ID+" TEXT, "+EXAM_QUESTION_ANSWER+" TEXT"+" );";

        String examQuestionTablesql=EXAM_QUESTION_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+EXAM_ID+" TEXT, "
                +EXAM_QUESTION_ID+" TEXT, "+EXAM_QUESTION_TITLE+" TEXT, "+
                EXAM_QUESTION_DESCRIPTION+" TEXT, "+EXAM_QUESTION_TYPE+" INTEGER, "
                +EXAM_QUESTION_IMAGE_NAME+" TEXT, "+
                EXAM_QUESTION_ANSWER+" TEXT, "+EXAM_QUESTION_AUDIO_NAME+" TEXT, "
                +EXAM_QUESTION_RIGHT_ANSWER+" TEXT, "+EXAM_QUESTION_TRUE_FALSE+" TEXT "+
                ");";

        String examTablesql=EXAM_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+EXAM_ID+" TEXT, "
                +COURSE_ID+" TEXT, "+MODULE_ID+" TEXT, "+USER_TYPE+" INTEGER, "+EXAM_TITLE+" TEXT "+");";

        String pregressTablesql=PROGRESS_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+USER_NAME+" TEXT ,"+MODULE_ID+" TEXT, "+
                COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT,"+USER_TYPE+" INTEGER "+");";

        String questionTablesql= QUESTION_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+MODULE_ID+" TEXT, "+COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT, "+QUESTION_TITLE+" TEXT, "+
                QUESTION_DESCRIPTION+" TEXT, "+QUESTION_TYPE+" INTEGER, "+QUESTION_IMAGE_NAME+" TEXT, "+
                QUESTION_WRONG_ANSWER+" TEXT, "+
                QUESTION_ANSWER+" TEXT, "+QUESTION_AUDIO_NAME+" TEXT, "+QUESTION_RIGHT_ANSWER+" TEXT, "+QUESTION_TRUE_FALSE+" TEXT "+
                ");";

        String coursesql=COURSE_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+COURSE_ID+" TEXT, "
                +COURSE_TITLE+" TEXT, "+USER_TYPE+" INTEGER, "+
                COURSE_STATUS+" INTEGER ,"+COURSE_ICON_IMAGE_NAME+" TEXT, "
                +TOTAL_MODULES_FOR_THIS_COURSE+" INTEGER, "
                +QUESTION_ID+" TEXT "+
                ");";

        String modulesql=MODULE_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+MODULE_ID+" TEXT, "
                +COURSE_ID+" TEXT, "
                +MODULE_TITLE+" TEXT, "+
                MODULE_STATUS+" TEXT, "+MODULE_ICON_IMAGE_NAME+" TEXT "+
                ");";

        sqlList.add(modulesql);
        sqlList.add(coursesql);
        sqlList.add(questionTablesql);
        sqlList.add(pregressTablesql);
        sqlList.add(examProgresssql);
        sqlList.add(examTablesql);
        sqlList.add(examQuestionAnswerTablesql);
        sqlList.add(examQuestionTablesql);

        return sqlList;
    }


    private void createExamProgressTable(SQLiteDatabase db) {
        String sql="CREATE TABLE IF NOT EXISTS "+EXAM_PROGRESS_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY,"
                +USER_NAME+" TEXT, "+EXAM_SCORE+" INTEGER, "+EXAM_ID+" TEXT, "+PREVEIOUS_EXAM_QUESTIONS+" TEXT "
                +" );";
        db.execSQL(sql);
    }

    private void createExamQuestionAnswerTable(SQLiteDatabase db) {
       String sql="CREATE TABLE IF NOT EXISTS "+EXAM_QUESTION_ANSWER_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "
               +EXAM_ID+" TEXT, "
               +EXAM_QUESTION_ID+" TEXT, "+EXAM_QUESTION_ANSWER+" TEXT"+" );";

       db.execSQL(sql);
    }

    private void createExamQuestionTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+EXAM_QUESTION_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+EXAM_ID+" TEXT, "+EXAM_QUESTION_ID+" TEXT, "+EXAM_QUESTION_TITLE+" TEXT, "+
                EXAM_QUESTION_DESCRIPTION+" TEXT, "+EXAM_QUESTION_TYPE+" INTEGER, "+EXAM_QUESTION_IMAGE_NAME+" TEXT, "+
                EXAM_QUESTION_ANSWER+" TEXT, "+EXAM_QUESTION_AUDIO_NAME+" TEXT, "+EXAM_QUESTION_RIGHT_ANSWER+" TEXT, "+EXAM_QUESTION_TRUE_FALSE+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);
    }

    private void createExamTable(SQLiteDatabase db) {
       //TODO exam table
        String sql="CREATE TABLE IF NOT EXISTS "+EXAM_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+EXAM_ID+" TEXT, "
                +COURSE_ID+" TEXT, "+MODULE_ID+" TEXT, "+USER_TYPE+" INTEGER, "+EXAM_TITLE+" TEXT "+");";
        db.execSQL(sql);
    }

    private void createProgressTable(SQLiteDatabase db) {
        String sql="CREATE TABLE IF NOT EXISTS "+PROGRESS_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+USER_NAME+" TEXT ,"+MODULE_ID+" TEXT, "+
                COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT,"+USER_TYPE+" INTEGER "+");";
        db.execSQL(sql);
    }

    private void createQuestionsTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+QUESTION_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+MODULE_ID+" TEXT, "+COURSE_ID+" TEXT, "+QUESTION_ID+" TEXT, "+QUESTION_TITLE+" TEXT, "+
                QUESTION_DESCRIPTION+" TEXT, "+QUESTION_TYPE+" INTEGER, "+QUESTION_IMAGE_NAME+" TEXT, "+
                QUESTION_ANSWER+" TEXT, "+QUESTION_AUDIO_NAME+" TEXT, "+QUESTION_RIGHT_ANSWER+" TEXT, "+QUESTION_TRUE_FALSE+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);

    }

    private void createCourseTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+COURSE_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+MODULE_ID+" TEXT, "+COURSE_ID+" TEXT, "+COURSE_TITLE+" TEXT, "+
                COURSE_STATUS+" INTEGER ,"+COURSE_ICON_IMAGE_NAME+" TEXT "+QUESTION_ID+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);
    }

    private void createModuleTable(SQLiteDatabase db) {
        String sql="CREATE TABLE "+MODULE_TABLE+" ( "+_ID+" INTEGER PRIMARY KEY, "+MODULE_ID+" TEXT, "+MODULE_TITLE+" TEXT, "+USER_TYPE+" INTEGER, "+
                MODULE_STATUS+" TEXT, "+MODULE_ICON_IMAGE_NAME+" TEXT "+
                ");";
        Log.d(TAG,sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS "+MODULE_TABLE+","+COURSE_TABLE+","+QUESTION_TABLE+
                ","+EXAM_QUESTION_TABLE+","+EXAM_TABLE+";";
        db.execSQL(sql);
        onCreate(db);
    }

    private void saveInProgressTable(String userName, String moduleId, String courseId, String questionId, UserType userType){
        Log.d(TAG,userName+""+moduleId+courseId+questionId+userType);

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
                dataMap.put(AppConstants.KEY_MODULE_ID,cursor.getString(cursor.getColumnIndex(MODULE_ID)));
                dataMap.put(AppConstants.KEY_COURSE_ID,cursor.getString(cursor.getColumnIndex(COURSE_ID)));
                dataMap.put(AppConstants.KEY_QUESTION_ID,cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
            }while (cursor.moveToNext());
            cursor.close();
            return dataMap;
        }else {
            saveInProgressTable(userName,"1","1","1",userType);
            dataMap.put(AppConstants.KEY_MODULE_ID,"1");
            dataMap.put(AppConstants.KEY_COURSE_ID,"1");
            dataMap.put(AppConstants.KEY_QUESTION_ID,"1");
            return dataMap;
        }

    }

    public void insertExam(Exam exam,UserType userType){
        if (checkIsDataAlreadyInDBorNot(EXAM_TABLE,EXAM_ID,exam.getId())){
         return;
        }
        ContentValues cv=new ContentValues();
        cv.put(EXAM_ID,exam.getId());
        cv.put(EXAM_TITLE,exam.getTitle());
        cv.put(COURSE_ID,exam.getCourseId());
        cv.put(MODULE_ID,exam.getModuleId());
        cv.put(USER_TYPE,userType.ordinal());

        for (ExamQuestion question:exam.getExamQuestions()){
            insertExamQuestion(exam.getId(),question);
        }

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(EXAM_TABLE,null,cv);
    }

    private void insertExamQuestion(String id, ExamQuestion question) {
        ContentValues cv =new ContentValues();
        cv.put(EXAM_ID,id);
        cv.put(EXAM_QUESTION_ID,question.getId());
        cv.put(EXAM_QUESTION_IMAGE_NAME,question.getImage());
        cv.put(EXAM_QUESTION_TITLE,question.getTitleText());
        cv.put(EXAM_QUESTION_AUDIO_NAME,question.getAudio());
        cv.put(EXAM_QUESTION_DESCRIPTION,question.getDescriptionText());
        cv.put(EXAM_QUESTION_RIGHT_ANSWER,question.getRightAnswer());
        cv.put(EXAM_QUESTION_TYPE,question.getType());

        for (String s:question.getAnswer()){
            insertQuestionAnswer(id,question.getId(),s);
        }

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(EXAM_QUESTION_TABLE,null,cv);
    }

    private void insertQuestionAnswer(String examId,String questionId, String s) {
        ContentValues cv=new ContentValues();
        cv.put(EXAM_ID,examId);
        cv.put(EXAM_QUESTION_ID,questionId);
        cv.put(EXAM_QUESTION_ANSWER,s);

        this.getWritableDatabase().insert(EXAM_QUESTION_ANSWER_TABLE,null,cv);
    }

    public void insertModule(String id,Module module) {

        ContentValues cv=new ContentValues();
        cv.put(COURSE_ID,id);
        cv.put(MODULE_ID,module.getId());
        cv.put(MODULE_TITLE,module.getTitle());
        cv.put(MODULE_ICON_IMAGE_NAME,module.getIconImage());


        for (Question question:module.getQuestions()){
            Log.d(TAG,question.toString());
            insertQuestion(id,module.getId(),question);
        }

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(MODULE_TABLE,null,cv);
    }

    /*private void insertCourses(String id, Course course) {
        ContentValues cv=new ContentValues();
        cv.put(MODULE_ID,id);
        cv.put(COURSE_ID,course.getId());
        cv.put(COURSE_TITLE,course.getTitle());
        cv.put(COURSE_ICON_IMAGE_NAME,course.getIconImage());
        cv.put(COURSE_STATUS,course.getStatus().ordinal());

        for (Question question:course.getQuestions()){
            Log.d(TAG,question.toString());
            insertQuestion(id,course.getId(),question);
        }

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(COURSE_TABLE,null,cv);
    }*/

    private void insertQuestion(String cId,String moduleId, Question question) {
        ContentValues cv =new ContentValues();
        cv.put(MODULE_ID,moduleId);
        cv.put(COURSE_ID,cId);
        cv.put(QUESTION_ID,question.getId());
        cv.put(QUESTION_IMAGE_NAME,question.getImage());
        cv.put(QUESTION_TRUE_FALSE,question.getTrueFalse());
        cv.put(QUESTION_TITLE,question.getTitleText());
        cv.put(QUESTION_ANSWER,question.getAnswer());
        cv.put(QUESTION_AUDIO_NAME,question.getAudio());
        cv.put(QUESTION_DESCRIPTION,question.getDescriptionText());
        cv.put(QUESTION_RIGHT_ANSWER,question.getRightAnswer());
        cv.put(QUESTION_WRONG_ANSWER,question.getWrongAnswer());
        cv.put(QUESTION_ID,question.getId());
        cv.put(QUESTION_TYPE,question.getType());

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(QUESTION_TABLE,null,cv);
    }

    private boolean checkIsDataAlreadyInDBorNot(String TableName,
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

    public ArrayList<Module> getAllModules(String courseId){
        String sql="SELECT * FROM "+MODULE_TABLE;

        /*if (userName!=null && userType!=null){
            sql+=" WHERE "+USER_NAME+" = '"+userName+"'"+" AND "+USER_TYPE+" = '"+userType.ordinal()+"'";
        }else if (userName!=null && userType==null){
            sql+=" WHERE "+USER_NAME+" = '"+userName+"'";
        }else if (userType!=null && userName==null){
            sql+=" WHERE "+USER_TYPE+" = '"+userType.ordinal()+"'";
        }
*/
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
                module.setQuestions(getAllQuestions(courseId,module.getId()));

                modules.add(module);
            }while (cursor.moveToNext());
            cursor.close();
            return modules;
        }

        return null;
    }

    public ArrayList<Course> getAllCourses(@Nullable String userName, @Nullable UserType userType) {

        String sql="SELECT * FROM "+COURSE_TABLE;

        if (userName!=null && userType!=null){
            sql+=" WHERE "+USER_NAME+" = '"+userName+"'"+" AND "+USER_TYPE+" = '"+userType.ordinal()+"'";
        }else if (userName!=null && userType==null){
            sql+=" WHERE "+USER_NAME+" = '"+userName+"'";
        }else if (userType!=null && userName==null){
            sql+=" WHERE "+USER_TYPE+" = '"+userType.ordinal()+"'";
        }

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
                course.setModules(getModules(course.getId()));

                courses.add(course);
            }while (cursor.moveToNext());
            cursor.close();
            return courses;
        }
        return null;
    }

    private List<Question> getAllQuestions(String moduleId,String courseId) {
        String sql="SELECT * FROM "+QUESTION_TABLE+" WHERE "+COURSE_ID+" = '"+courseId+"'"+" AND "
                +MODULE_ID+" = '"+moduleId+"'";
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
                question.setWrongAnswer(cursor.getString(cursor.getColumnIndex(QUESTION_WRONG_ANSWER)));
                question.setImage(cursor.getString(cursor.getColumnIndex(QUESTION_IMAGE_NAME)));
                question.setType(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                question.setAudio(cursor.getString(cursor.getColumnIndex(QUESTION_AUDIO_NAME)));
                question.setDescriptionText(cursor.getString(cursor.getColumnIndex(QUESTION_DESCRIPTION)));
                question.setTrueFalse(cursor.getString(cursor.getColumnIndex(QUESTION_TRUE_FALSE)));

                questions.add(question);

            }while (cursor.moveToNext());
            cursor.close();
            return questions;

        }
        return null;
    }

    public ArrayList<Module> getModules(String courseId) {
        String sql="SELECT * FROM "+MODULE_TABLE+" WHERE "+COURSE_ID+" = '"+courseId+"'";
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
                module.setQuestions(getAllQuestions(courseId,module.getId()));

                modules.add(module);
            }while (cursor.moveToNext());
            cursor.close();
            return modules;
        }

        return null;
    }

   public void updateProgressTable(String userName,String moduleId,String courseId,@Nullable String questionId,UserType userType){
       Log.d(TAG,userName+" "+moduleId+" "+courseId+" "+questionId+" "+userType);

       String where=USER_NAME+" = ?";
       ContentValues values=new ContentValues();
       values.put(MODULE_ID,moduleId);
       values.put(COURSE_ID,courseId);

       SQLiteDatabase db=this.getWritableDatabase();

       int i=db.update(PROGRESS_TABLE,values,where,new String[]{userName});
       Log.d(TAG,""+i);
   }

    public int getNoOfModulesForThisCourse(String courseID) {
        String where=COURSE_ID+" = ?";
        SQLiteDatabase database=this.getWritableDatabase();

        Cursor cursor=database.query(COURSE_TABLE, new String[]{TOTAL_MODULES_FOR_THIS_COURSE},
                where,new String[]{courseID},null,null,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            int i=cursor.getInt(cursor.getColumnIndex(TOTAL_MODULES_FOR_THIS_COURSE));
            cursor.close();
            return i;
        }

        return 0;
    }


    public Exam getExambyId(String examId){
        String sql="SELECT * FROM "+EXAM_TABLE+" WHERE "+EXAM_ID+" = '"+examId+"'";
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            Exam exam=new Exam();
            while (!cursor.isAfterLast()){
                exam.setCourseId(cursor.getString(cursor.getColumnIndex(COURSE_ID)));
                exam.setId(cursor.getString(cursor.getColumnIndex(EXAM_ID)));
                exam.setTitle(cursor.getString(cursor.getColumnIndex(EXAM_TITLE)));
                cursor.moveToNext();
            }
            cursor.close();
            exam.setExamQuestions(getExamQuestions(exam.getId()));
            return exam;
        }

        return null;
    }

    public Exam getExam(String moduleId){
        String sql="SELECT * FROM "+EXAM_TABLE+" WHERE "+MODULE_ID+" = '"+moduleId+"'";
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            Exam exam=new Exam();
            while (!cursor.isAfterLast()){
                exam.setCourseId(cursor.getString(cursor.getColumnIndex(COURSE_ID)));
                exam.setId(cursor.getString(cursor.getColumnIndex(EXAM_ID)));
                exam.setModuleId(cursor.getString(cursor.getColumnIndex(MODULE_ID)));
                exam.setTitle(cursor.getString(cursor.getColumnIndex(EXAM_TITLE)));
                cursor.moveToNext();
            }
            cursor.close();
            exam.setExamQuestions(getExamQuestions(exam.getId()));
            return exam;
        }

        return null;
    }

   public List<Exam> getAllExams(){
       String sql="SELECT * FROM "+EXAM_TABLE;
       List<Exam> exams;
       Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
       if (cursor!=null && cursor.getCount()>0){
           exams=new ArrayList<>();
           cursor.moveToFirst();
           while (!cursor.isAfterLast()){
               Exam exam=new Exam();
               exam.setCourseId(cursor.getString(cursor.getColumnIndex(COURSE_ID)));
               exam.setId(cursor.getString(cursor.getColumnIndex(EXAM_ID)));
               exam.setModuleId(cursor.getString(cursor.getColumnIndex(MODULE_ID)));
               exam.setTitle(cursor.getString(cursor.getColumnIndex(EXAM_TITLE)));

               exam.setExamQuestions(getExamQuestions(exam.getId()));
           }
           cursor.close();
           return exams;
       }

       return null;
   }

    private List<ExamQuestion> getExamQuestions(String examId) {
        String sql = "SELECT * FROM " + EXAM_QUESTION_TABLE + " WHERE " + EXAM_ID + " = '" + examId + "'";
        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            List<ExamQuestion> questions=new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                ExamQuestion question=new ExamQuestion();
                question.setId(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_ID)));
                question.setAudio(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_AUDIO_NAME)));
                question.setImage(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_IMAGE_NAME)));
                question.setDescriptionText(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_DESCRIPTION)));
                question.setRightAnswer(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_RIGHT_ANSWER)));
                question.setType(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_TYPE)));

                question.setAnswer(getExamQuestionAnswer(examId,question.getId()));
                questions.add(question);
                cursor.moveToNext();


            }
            cursor.close();
            return questions;

        }
       return null;
   }

    private List<String> getExamQuestionAnswer(String examId,String courseId) {
       String sql="SELECT * FROM "+EXAM_QUESTION_ANSWER_TABLE+" WHERE "+EXAM_ID+" = '"+examId+"' AND "
               +EXAM_QUESTION_ID+" = '"+courseId+"'";

       Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);

       if (cursor!=null && cursor.getCount()>0){
           List<String> answers=new ArrayList<>();
           cursor.moveToFirst();
           while (!cursor.isAfterLast()){
               answers.add(cursor.getString(cursor.getColumnIndex(EXAM_QUESTION_ANSWER)));
               cursor.moveToNext();
           }
           cursor.close();
           return answers;
       }

       return null;
    }

    public boolean isExamAvailableForCourse(String moduleId) {
       String sql="SELECT * FROM "+EXAM_TABLE+" WHERE "+MODULE_ID+" = '"+moduleId+"'";
       Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor==null){
            return false;
        }else {
            if (cursor.getCount()>0){
                cursor.close();
                return true;
            }else {
                return false;
            }
        }
    }

    public void saveExamProgress(String userName,String examId,int totalQues,int score,
                                 @Nullable String[] qIds){
       ContentValues cv=new ContentValues();
       cv.put(EXAM_ID,examId);
       cv.put(EXAM_SCORE,score);
       cv.put(USER_NAME,userName);
       cv.put(EXAM_TOTAL_QUESTIONS,totalQues);
       StringBuilder builder=new StringBuilder();
        if (qIds != null) {
            for (String q:qIds){
                builder.append(q).append(",");
            }
            cv.put(PREVEIOUS_EXAM_QUESTIONS,builder.toString());
        }

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.insert(EXAM_PROGRESS_TABLE,null,cv);
    }

    public  String[] getPreviusQuestions(String userName,String examId){
        List<String> qList=new ArrayList<>();
        String [] qArr = new String[0];
        String sql="SELECT * FROM "+EXAM_PROGRESS_TABLE+" WHERE "+USER_NAME+" = '"+userName+"'"
                +" AND "+EXAM_ID+" = '"+examId+"'";

        Cursor cursor=this.getWritableDatabase().rawQuery(sql,null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            while (cursor.moveToNext()){
                String s=cursor.getString(cursor.getColumnIndex(PREVEIOUS_EXAM_QUESTIONS));
                 qArr=s.split(",");
            }

            cursor.close();
            return qArr;
        }

        return null;
    }


    public void insertCourse(Course course, UserType userType) {
        if (checkIsDataAlreadyInDBorNot(COURSE_TABLE,COURSE_ID,course.getId())){
            return;
        }

        ContentValues cv=new ContentValues();
        cv.put(USER_TYPE,userType.ordinal());
        cv.put(COURSE_ID,course.getId());
        cv.put(COURSE_TITLE,course.getTitle());
        cv.put(COURSE_ICON_IMAGE_NAME,course.getIconImage());
        cv.put(COURSE_STATUS,course.getStatus().ordinal());
        cv.put(TOTAL_MODULES_FOR_THIS_COURSE,course.getModules().size());

        for (Module module:course.getModules()){
            Log.d(TAG,module.toString());
            insertModule(course.getId(),module);
        }

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(COURSE_TABLE,null,cv);
    }
}
