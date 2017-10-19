
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = new ArrayList<Question>();


    private Status status=Status.Locked;

    public enum Status {
        Locked,Unlocked
    } ;
    /**
     * No args constructor for use in serialization
     * 
     */
    public Course() {
    }

    /**
     * 
     * @param id
     * @param title
     * @param iconImage
     * @param questions
     */
    public Course(String title, String id, String iconImage, List<Question> questions,Status status) {
        super();
        this.title = title;
        this.id = id;
        this.iconImage = iconImage;
        this.questions = questions;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isLocked(){
        return (status == Status.Locked);
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", iconImage='" + iconImage + '\'' +
                ", questions=" + questions +
                ", status=" + status +
                '}';
    }
}
