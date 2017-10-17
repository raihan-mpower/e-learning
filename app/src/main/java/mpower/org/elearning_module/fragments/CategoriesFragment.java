package mpower.org.elearning_module.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mpower.org.elearning_module.R;

/**
 * Created by sabbir on 4/10/17.
 * @author Sabbir ,sabbir@mpower-social.com
 */

public class CategoriesFragment extends Fragment {
    TextView tvDummy;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.comming_soon,container,false);
        tvDummy=(TextView) view.findViewById(R.id.tv_dummy);
        tvDummy.setText("Others");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Others");

    }

}
