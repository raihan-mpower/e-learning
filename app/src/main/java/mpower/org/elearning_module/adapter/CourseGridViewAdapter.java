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

import mpower.org.elearning_module.activities.CourseContentActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.databases.DatabaseHelper;
import mpower.org.elearning_module.model.Course;
import mpower.org.elearning_module.model.Question;
import mpower.org.elearning_module.utils.Utils;

public class CourseGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Course> courses;
    DatabaseHelper databaseHelper;
    public CourseGridViewAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
        databaseHelper=new DatabaseHelper(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;



        gridView = new View(context);

        // get layout from mobile.xml
        gridView = inflater.inflate(R.layout.module, null);
        TextView title = (TextView)gridView.findViewById(R.id.title);
        title.setText(courses.get(position).getTitle());
        ImageView icon = (ImageView) gridView.findViewById(R.id.icon);
        TextView textView=new TextView(context);


        icon.setImageDrawable(Utils.loadDrawableFromAssets(context,"images/"+courses.get(position).getIconImage()));


       /* gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                modules.get(position)
                CourseContentActivity.questions = (ArrayList<Question>) courses.get(position).getQuestions();
                Intent intent = new Intent(context,CourseContentActivity.class);
                context.startActivity(intent);
            }
        });*/



        return gridView;
    }

    @Override
    public int getCount() {
        return courses.size();
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