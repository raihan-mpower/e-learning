package mpower.org.elearning_module.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.activities.ModuleActivity;
import mpower.org.elearning_module.adapter.CourseGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.CurrentUserProgress;
import mpower.org.elearning_module.utils.Helper;
import mpower.org.elearning_module.utils.Status;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserType;

public class CourseFragment extends BaseFragment {

    boolean firstTime=false;
    GridView gridView;
    private UserType userType;
    CourseGridViewAdapter courseAdapter;
    ProgressDialog progressDialog;
    private ArrayList<Course> courseArrayList;
    private DatabaseHelper databaseHelper;
    public static String sCURRENT_MODULE_ID="";

    public CourseFragment() {
        // Required empty public constructor
    }


    public static CourseFragment newInstance(UserType userType) {
        CourseFragment fragment = new CourseFragment();
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

            int noOfModules=databaseHelper.getNoOfModulesForThisCourse(courseId);

            if (Integer.valueOf(moduleId)>=noOfModules){
                moduleId="1";
                databaseHelper.updateProgressTable(UserCollection.getInstance().getUserData().getUsername(),
                        moduleId,courseId,questionId,userType);
            }


            HashMap<String,String> updatedProgress=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),
                    CurrentUserProgress.getInstance().getUserType());

            Helper.MakeLog(this.getClass(),updatedProgress.toString());


            for (Course course:courseArrayList){
                if (course.getId().equalsIgnoreCase(updatedProgress.get(AppConstants.KEY_COURSE_ID))){
                    course.setStatus(Status.UNLOCKED);
                }else if (Integer.valueOf(course.getId())<Integer.valueOf(updatedProgress.get(AppConstants.KEY_COURSE_ID))){
                    course.setStatus(Status.UNLOCKED);
                }/*else if(Integer.valueOf(updatedProgress.get(AppConstants.KEY_COURSE_ID))>=module.getCourses().size()){
                    module.setStatus(Status.UNLOCKED);
                }*/else {
                    course.setStatus(Status.LOCKED);
                }
            }

            setUpAdapter();
            Log.d("TAG","RETURNED");
        }
    }

    /*@Override
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
    }*/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_module;
    }

    @Override
    protected void onViewReady(View view, @Nullable Bundle savedInstanceState) {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        gridView = view.findViewById(R.id.gridView1);

        HashMap<String,String> progressMap=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),userType);
        Log.d("TAG",progressMap.toString());

        String progressCourseId = progressMap.get(AppConstants.KEY_COURSE_ID);

        UserType userType=CurrentUserProgress.getInstance().getUserType();

        courseArrayList=databaseHelper.getAllCourses(null,userType);


        for (Course course:courseArrayList){
            if (course.getId().equalsIgnoreCase(progressCourseId)){
                course.setStatus(Status.UNLOCKED);
            }else if (Integer.valueOf(course.getId())<Integer.valueOf(progressCourseId)){
                course.setStatus(Status.UNLOCKED);
            }else {
                course.setStatus(Status.LOCKED);
            }
        }

        progressDialog.dismiss();
        if (courseArrayList==null){

        }
        firstTime=true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpAdapter() {
        courseAdapter=new CourseGridViewAdapter(getContext(),courseArrayList);
        gridView.setAdapter(courseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (courseArrayList.get(i).isLocked()){
                   // Helper.showToast(getContext(),getString(R.string.complete_other_modules), Toast.LENGTH_LONG);
                    //showToast(getString(R.string.complete_other_modules));
                    showSimpleDialog(getString(R.string.locked),getString(R.string.complete_other_modules));
                    return;

                }
                ModuleActivity.CURRENT_MODULE_TITLE=courseArrayList.get(i).getTitle();
                Intent intent = new Intent(getActivity(),ModuleActivity.class);
                ModuleActivity.sModules= (ArrayList<Module>) courseArrayList.get(i).getModules();
                sCURRENT_MODULE_ID=courseArrayList.get(i).getId();
                ModuleActivity.CURRENT_MODULE_ID=courseArrayList.get(i).getId();
                intent.putExtra(AppConstants.DATA,(ArrayList<Module>)courseArrayList.get(i).getModules());
                CurrentUserProgress.getInstance().setProgressModule(courseArrayList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void insertIntoDb(ArrayList<Course> moduleArrayList) {
        for (Course module:moduleArrayList){
            databaseHelper.insertCourse(module, userType);
        }
    }

}
