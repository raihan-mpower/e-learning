
package mpower.org.elearning_module.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Module {

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
