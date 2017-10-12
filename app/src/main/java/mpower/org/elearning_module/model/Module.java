
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mpower.org.elearning_module.utils.UserType;

public class Module implements Serializable {

    @SerializedName("user_type")
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("icon_image")
    @Expose
    private String iconImage;
    @SerializedName("courses")
    @Expose
    private List<Course> courses = new ArrayList<Course>();


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

    public void setUserTypeEnum(UserType userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
    }

    UserType userTypeEnum;

    Status status;

    enum Status {
        Locked,Unlocked
    }

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
     * @param courses
     * @param iconImage
     */
    public Module(String title, String id, String iconImage, List<Course> courses) {
        super();
        this.title = title;
        this.id = id;
        this.iconImage = iconImage;
        this.courses = courses;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean isLocked(){
        return (status == Status.Locked);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
