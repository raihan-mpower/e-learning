
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamCurriculum_ implements Serializable
{

    @SerializedName("exams")
    @Expose
    private List<Exam> exams = null;
    private final static long serialVersionUID = 2307625329109746912L;

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

}
