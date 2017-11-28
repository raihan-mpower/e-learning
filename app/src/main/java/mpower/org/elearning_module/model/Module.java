
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserType;

public class Module implements Serializable {


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

    private List<Question> questions=new ArrayList<>();

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }



    public void setUserTypeEnum(UserType userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
    }

    UserType userTypeEnum;
    Status status;
    /**
     * No args constructor for use in serialization
     * 
     */
    public Module() {
    }

    /**
     * 
     * @param id
     * @param title
     * @param questions
     * @param iconImage
     */
    public Module(String title, String id, String iconImage, List<Question> questions) {
        super();
        this.title = title;
        this.id = id;
        this.iconImage = iconImage;
        this.questions = questions;
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

    public List<Question> getCourses() {
        return questions;
    }

    public void setCourses(List<Question> courses) {
        this.questions = courses;
    }

    public boolean isLocked(){
        return (status == Status.LOCKED);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
