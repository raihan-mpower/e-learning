package mpower.org.elearning_module.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.utils.AppConstants;
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
        gridView = (GridView) view.findViewById(R.id.gridView1);

        HashMap<String,String> progressMap=databaseHelper.getProgressForUser(UserCollection.getInstance().getUserData().getUsername(),userType);



        //moduleArrayList=databaseHelper.getAllModules(null,null);

        moduleArrayList=databaseHelper.getModules(progressMap.get("moduleId"));

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
            throw new RuntimeException("No Module for you!!");
        }
        moduleGridViewAdapter=new ModuleGridViewAdapter(getContext(),moduleArrayList);
        gridView.setAdapter(moduleGridViewAdapter);
        return view;
    }

    private void insertIntoDb(ArrayList<Module> moduleArrayList) {
        for (Module module:moduleArrayList){
            databaseHelper.insertModule(module);
        }
    }

}
