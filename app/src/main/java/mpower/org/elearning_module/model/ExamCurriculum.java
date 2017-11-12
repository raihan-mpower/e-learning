
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamCurriculum implements Serializable
{


    private final static long serialVersionUID = 2533029985655910793L;
    @SerializedName("exams")
    @Expose
    private List<Exam> exams;

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

}
