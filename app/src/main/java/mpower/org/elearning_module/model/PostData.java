package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by sabbir on 12/3/17.
 *
 * @author sabbir (sabbir@mpowe-social.com)
 */

public class PostData implements Serializable {
    private String userName;
    private String examId;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    private String courseId;
    private String moduleId;
    private String startTime;
    private String endTime;
    private HashMap<String,String> examInfo;
    private String courseRating;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public HashMap<String, String> getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(HashMap<String, String> examInfo) {
        this.examInfo = examInfo;
    }

    public String getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(String courseRating) {
        this.courseRating = courseRating;
    }
}
