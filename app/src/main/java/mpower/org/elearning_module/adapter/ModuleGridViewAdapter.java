package mpower.org.elearning_module.adapter;

/**
 * Created by raihan on 8/20/17.
 */


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mpower.org.elearning_module.CourseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Module;
import mpower.org.elearning_module.utils.Utils;

public class ModuleGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Module> modules;

    public ModuleGridViewAdapter(Context context, ArrayList<Module> modules) {
        this.context = context;
        this.modules = modules;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;



        gridView = new View(context);

        // get layout from mobile.xml
        gridView = inflater.inflate(R.layout.module, null);
        TextView title = (TextView)gridView.findViewById(R.id.title);
        title.setText(modules.get(position).getTitle());
        ImageView icon = (ImageView) gridView.findViewById(R.id.icon);
        icon.setImageDrawable(Utils.loadDrawableFromAssets(context,"images/"+modules.get(position).getIconImage()));

        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                modules.get(position)
                Intent intent = new Intent(context,CourseActivity.class);
                CourseActivity.courses = (ArrayList<Course>) modules.get(position).getCourses();
                context.startActivity(intent);
            }
        });



        return gridView;
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}