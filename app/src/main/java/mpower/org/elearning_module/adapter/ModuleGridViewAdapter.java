package mpower.org.elearning_module.adapter;

/**
 * Created by raihan on 8/20/17.
 */


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import mpower.org.elearning_module.activities.CourseActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.databases.DatabaseHelper;
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


        Module module=modules.get(position);


        ViewHolder viewHolder;
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView=inflater.inflate(R.layout.module,null);
            viewHolder=new ViewHolder();
            viewHolder.tvTitle=convertView.findViewById(R.id.title);
            viewHolder.imageViewIcon=convertView.findViewById(R.id.icon);
            viewHolder.imageViewStatus=convertView.findViewById(R.id.image_view_status_2);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }


        viewHolder.tvTitle.setText(module.getTitle());
        Glide.with(context).load(Uri.fromFile(new File(ELearningApp.IMAGES_FOLDER_NAME+File.separator+module.getIconImage())))
                .into(viewHolder.imageViewIcon);
      //  viewHolder.imageViewIcon.setImageDrawable(Utils.loadDrawableFromAssets(context,"images/"+module.getIconImage()));

        if (module.isLocked()){
            viewHolder.imageViewStatus.setImageResource(R.drawable.lock);
        }else {
            viewHolder.imageViewStatus.setImageResource(R.drawable.unlocked);
        }




        return convertView;
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

    private class ViewHolder{
        TextView tvTitle;
        ImageView imageViewIcon;
        ImageView imageViewStatus;
    }

}