package mpower.org.elearning_module.utils;

import android.support.annotation.NonNull;

/**
 * Created by sabbir on 10/24/17.
 */

public class CurrentUserProgress {

    private  String sCURRENT_MODULE_IN_PROGRESS;
    private  String sCURRENT_MODULE_COURSE_IN_PROGRESS;
    private  String sCURRENT_COURSE_QUESTION_IN_PROGRESS;
    private UserType userType;
    private static CurrentUserProgress instance=null;

    /*public static CurrentUserProgress getInstance(){
        if (instance==null){
            synchronized (CurrentUserProgress.class){
                if (instance==null){
                    return new CurrentUserProgress();
                }
            }
        }return instance;
    }*/

    public  static synchronized CurrentUserProgress getInstance(){
        if (instance==null){

            instance= new CurrentUserProgress();
        }

        return instance;
    }

   private CurrentUserProgress(){
    }

    public void clearUserProgress(){
        sCURRENT_COURSE_QUESTION_IN_PROGRESS=null;
        sCURRENT_MODULE_COURSE_IN_PROGRESS=null;
        sCURRENT_MODULE_IN_PROGRESS=null;

        userType=null;
    }


    public void setProgressCourse(@NonNull String courseId){
        sCURRENT_MODULE_COURSE_IN_PROGRESS=courseId;
    }
    public void setProgressQuestion(@NonNull String questionId){
        sCURRENT_COURSE_QUESTION_IN_PROGRESS=questionId;
    }


    public void setProgressModule(@NonNull String moduleId){
        sCURRENT_MODULE_IN_PROGRESS=moduleId;
    }

    public String getCurrentUserModuleProgress(){
        return sCURRENT_MODULE_IN_PROGRESS;
    }

    public String getCurrentUserCourseProgress(){
        return sCURRENT_MODULE_COURSE_IN_PROGRESS;
    }

    public String getCurrentUserQuestionProgress(){
        return sCURRENT_COURSE_QUESTION_IN_PROGRESS;
    }

    public void setUserType(UserType userType) {
        this.userType=userType;
    }

    public UserType getUserType(){
        return userType;
    }
}
