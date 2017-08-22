
package mpower.org.elearning_module.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Curriculum implements Serializable {

    @SerializedName("modules")
    @Expose
    private List<Module> modules = new ArrayList<Module>();

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
    public Curriculum(List<Module> modules) {
        super();
        this.modules = modules;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

}
