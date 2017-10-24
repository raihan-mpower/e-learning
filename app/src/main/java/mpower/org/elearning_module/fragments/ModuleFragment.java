package mpower.org.elearning_module.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.CourseActivity;
import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;
import mpower.org.elearning_module.utils.Utils;

public class ModuleFragment extends Fragment {
    GridView gridView;
    private UserType userType;
    ModuleGridViewAdapter moduleGridViewAdapter;
    ProgressDialog progressDialog;
    private ArrayList<Module> moduleArrayList;
    private DatabaseHelper databaseHelper;
    public static String sCURRENT_MODULE_ID="";
    public ModuleFragment() {
        // Required empty public constructor
    }


    public static ModuleFragment newInstance(UserType userType) {
        ModuleFragment fragment = new ModuleFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.USER_TYPE,userType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(getContext());
        if (getArguments() != null) {
            int u=  getArguments().getInt(AppConstants.USER_TYPE);
            userType=UserType.values()[u];
            if (userType!=null){
                CurrentUserProgress.getInstance().setUserType(userType);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_module,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..Please Wait");
        progressDialog.show();
        gridView = view.findViewById(R.id.gridView1);

        HashMap<String,String> progressMap=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),userType);
        Log.d("TAG",progressMap.toString());

        String progressMId = progressMap.get(AppConstants.KEY_MODULE_ID);



        //moduleArrayList=databaseHelper.getAllModules(null,null);

       // moduleArrayList=databaseHelper.getModules(progressMap.get("moduleId"));
      //  moduleArrayList=databaseHelper.getModules("2");
        moduleArrayList=databaseHelper.getAllModules(null,null);


        for (Module module:moduleArrayList){
            if (module.getId().equalsIgnoreCase(progressMId)){
                module.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(module.getId())<Integer.valueOf(progressMId)){
                module.setStatus(Status.UNLOCKED);
            }else {
                module.setStatus(Status.LOCKED);
            }
        }

        /*ArrayList<Module> modules=new ArrayList<>();
        modules.add(moduleArrayList.get(0));
        for (Module module:moduleArrayList){
            for (Module module1:modules){
                if (!module.getId().equalsIgnoreCase(module1.getId())){
                    modules.add(module);
                }
            }
        }*/

        progressDialog.dismiss();
        if (moduleArrayList==null){

        }
        moduleGridViewAdapter=new ModuleGridViewAdapter(getContext(),moduleArrayList);
        gridView.setAdapter(moduleGridViewAdapter);
        setUpAdapter();
        return view;
    }

    private void setUpAdapter() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),CourseActivity.class);
                sCURRENT_MODULE_ID=moduleArrayList.get(i).getId();
                if (moduleArrayList.get(i).isLocked()){
                    Helper.showToast(getContext(),"Complete other modules first", Toast.LENGTH_LONG);
                    return;

                }
                CourseActivity.CURRENT_MODULE_ID=moduleArrayList.get(i).getId();
                CurrentUserProgress.getInstance().setProgressModule(moduleArrayList.get(i).getId());
                CourseActivity.courses = (ArrayList<Course>) moduleArrayList.get(i).getCourses();
                startActivity(intent);
            }
        });
    }

    private void showToast() {

    }

    private void insertIntoDb(ArrayList<Module> moduleArrayList) {
        for (Module module:moduleArrayList){
            databaseHelper.insertModule(module);
        }
    }

}
