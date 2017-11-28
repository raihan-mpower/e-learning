
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserType;

public class Course implements Serializable {

    @SerializedName("user_type")
    private String userType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("modules")
    @Expose

    private List<Module> modules=new ArrayList<>();

    public List<Module> getModules() {
        return modules;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    private Status status= Status.LOCKED;

    public UserType getUserTypeEnum() {
        switch (getUserType()){
            case "":
                return UserType.OTHER;
            case "DOT":
                return UserType.DOT;
            default:
                return UserType.PUBLIC;
        }

    }

     ;
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
    public Course(String title, String id, String iconImage, List<Module> questions,Status status) {
        super();
        this.title = title;
        this.id = id;
        this.iconImage = iconImage;
        this.modules = questions;
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

    public List<Module> getQuestions() {
        return modules;
    }

    public void setQuestions(List<Module> questions) {
        this.modules = questions;
    }

    public boolean isLocked(){
        return (status == Status.LOCKED);
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
                ", questions=" + modules +
                ", status=" + status +
                '}';
    }
}
