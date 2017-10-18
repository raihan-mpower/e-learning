package mpower.org.elearning_module.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.adapter.ModuleGridViewAdapter;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.parser.CurriculumParser;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.UserType;
import mpower.org.elearning_module.utils.Utils;

public class ModuleFragment extends Fragment {
    GridView gridView;
    private UserType userType;
    ModuleGridViewAdapter moduleGridViewAdapter;

    private ArrayList<Module> moduleArrayList;
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

        gridView = (GridView) view.findViewById(R.id.gridView1);



        moduleArrayList= (ArrayList<Module>) CurriculumParser.returnCurriculum(Utils.readAssetContents("curriculum.json",getActivity())).getModules();


        ArrayList<Module> sortedForUserType=new ArrayList<>();
        for (Module module:moduleArrayList){
            if (module.getUserTypeEnum()==userType){
                sortedForUserType.add(module);
            }
        }

        moduleGridViewAdapter=new ModuleGridViewAdapter(getContext(),sortedForUserType);
        gridView.setAdapter(moduleGridViewAdapter);
        return view;
    }

}
