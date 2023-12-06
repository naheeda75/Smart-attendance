package com.codebook.in.smartattendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassesAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<Classes> classesArrayList;

    public ClassesAdapter(Context context, int layout, ArrayList<Classes> classesArrayList) {
        this.context = context;
        this.layout = layout;
        this.classesArrayList = classesArrayList;
    }

    @Override
    public int getCount() {
        return classesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        TextView class_year,class_branch,class_section,class_subject;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.class_year = (TextView) row.findViewById(R.id.class_year);
            holder.class_branch = (TextView) row.findViewById(R.id.class_branch);
            holder.class_section = (TextView) row.findViewById(R.id.class_section);
//            holder.class_subject=(TextView) row.findViewById(R.id.class_subject);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Classes classes = classesArrayList.get(position);

        holder.class_year.setText(classes.getClass_Year());
        holder.class_branch.setText(classes.getClass_Branch());
        holder.class_section.setText(classes.getClass_Section());
//        holder.class_subject.setText(classes.getClass_Subject());

        return row;

    }
}
