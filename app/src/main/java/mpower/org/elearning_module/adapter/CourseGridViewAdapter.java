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
    public CourseGridViewAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        Course course=courses.get(position);

        ViewHolder viewHolder;
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView=inflater.inflate(R.layout.item_row,null);
            viewHolder=new ViewHolder();
            viewHolder.tvTitle=convertView.findViewById(R.id.title);
            viewHolder.imageViewIcon=convertView.findViewById(R.id.icon);
            viewHolder.imageViewStatus=convertView.findViewById(R.id.image_view_status_2);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        viewHolder.tvTitle.setText(course.getTitle());
        viewHolder.imageViewIcon.setImageDrawable(Utils.loadDrawableFromAssets(context,"images/"+course.getIconImage()));

        if (course.isLocked()){
            viewHolder.imageViewStatus.setImageResource(R.drawable.lock);
        }else {
            viewHolder.imageViewStatus.setImageResource(R.drawable.unlocked);
        }



        return convertView;
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

    private class ViewHolder{
        TextView tvTitle;
        ImageView imageViewIcon;
        ImageView imageViewStatus;
    }

}