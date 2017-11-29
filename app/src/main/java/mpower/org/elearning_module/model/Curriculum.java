
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Curriculum implements Serializable {

    @SerializedName("courses")
    @Expose
    private List<Course> courses=new ArrayList<>();

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Curriculum() {
    }

    /**
     * 
     * @param modules
     */
    public Curriculum(List<Course> modules) {
        super();
        this.courses = modules;
    }

}
