package mpower.org.elearning_module.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.CourseActivity;
import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

public class ModuleFragment extends Fragment {

    boolean firstTime=false;
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
    public void onResume() {
        super.onResume();
        if (firstTime){
            Log.d("TAG","FirstVisit In onResume");
            setUpAdapter();
            firstTime=false;
        }else {
            HashMap<String,String> progress=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),
                    CurrentUserProgress.getInstance().getUserType());
            Log.d("TAG",progress.toString());

            String courseId=progress.get(AppConstants.KEY_COURSE_ID);
            String moduleId=progress.get(AppConstants.KEY_MODULE_ID);
            String questionId=progress.get(AppConstants.KEY_QUESTION_ID);

            int noOfCourses=databaseHelper.getNoOfCoursesForThisModule(moduleId);

            if (Integer.valueOf(courseId)>=noOfCourses){
                courseId="1";
                databaseHelper.updateProgressTable(UserCollection.getInstance().getUserData().getUsername(),
                        moduleId,courseId,questionId,userType);
            }


            HashMap<String,String> updatedProgress=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),
                    CurrentUserProgress.getInstance().getUserType());
            Helper.MakeLog(this.getClass(),updatedProgress.toString());



            int totalCourseOfThisModule;

            for (Module module:moduleArrayList){
                if (module.getId().equalsIgnoreCase(updatedProgress.get(AppConstants.KEY_MODULE_ID))){
                    module.setStatus(Status.UNLOCKED);
                }else if (Integer.valueOf(module.getId())<Integer.valueOf(updatedProgress.get(AppConstants.KEY_MODULE_ID))){
                    module.setStatus(Status.UNLOCKED);
                }else if(Integer.valueOf(updatedProgress.get(AppConstants.KEY_COURSE_ID))>=module.getCourses().size()){
                    module.setStatus(Status.UNLOCKED);
                }else {
                    module.setStatus(Status.LOCKED);
                }
            }

            setUpAdapter();
            Log.d("TAG","RETURNED");
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

        UserType userType=CurrentUserProgress.getInstance().getUserType();

        moduleArrayList=databaseHelper.getAllModules(null,userType);


        for (Module module:moduleArrayList){
            if (module.getId().equalsIgnoreCase(progressMId)){
                module.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(module.getId())<Integer.valueOf(progressMId)){
                module.setStatus(Status.UNLOCKED);
            }else {
                module.setStatus(Status.LOCKED);
            }
        }

        progressDialog.dismiss();
        if (moduleArrayList==null){

        }
        firstTime=true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void setUpAdapter() {
        moduleGridViewAdapter=new ModuleGridViewAdapter(getContext(),moduleArrayList);
        gridView.setAdapter(moduleGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (moduleArrayList.get(i).isLocked()){
                    Helper.showToast(getContext(),"Complete other modules first", Toast.LENGTH_LONG);
                    return;

                }
                CourseActivity.CURRENT_MODULE_TITLE=moduleArrayList.get(i).getTitle();
                Intent intent = new Intent(getActivity(),CourseActivity.class);
                sCURRENT_MODULE_ID=moduleArrayList.get(i).getId();
                CourseActivity.CURRENT_MODULE_ID=moduleArrayList.get(i).getId();
                intent.putExtra(AppConstants.DATA,(ArrayList<Course>)moduleArrayList.get(i).getCourses());
                CurrentUserProgress.getInstance().setProgressModule(moduleArrayList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void insertIntoDb(ArrayList<Module> moduleArrayList) {
        for (Module module:moduleArrayList){
            databaseHelper.insertModule(module, userType);
        }
    }

}
