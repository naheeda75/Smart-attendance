package com.codebook.in.smartattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentsAdapter  extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<Students> studentsArrayList;

    public StudentsAdapter(Context context, int layout, ArrayList<Students> studentsArrayList) {
        this.context = context;
        this.layout = layout;
        this.studentsArrayList = studentsArrayList;
    }

    @Override
    public int getCount() {
        return studentsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        TextView student_rollno,student_name,chk_attendance;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.student_rollno = (TextView) row.findViewById(R.id.student_rollno);
            holder.student_name = (TextView) row.findViewById(R.id.student_name);
//            holder.chk_attendance = (CheckBox) row.findViewById(R.id.mystud_chk);
            row.setTag(holder);
        }
        else {
            holder = (StudentsAdapter.ViewHolder) row.getTag();
        }

        Students students = studentsArrayList.get(position);

        holder.student_rollno.setText(students.getStudent_Rollno());
//        holder.student_branch.setText(students.getStudent_Branch());
        holder.student_name.setText(students.getStudent_Name());
//        holder.student_year.setText(students.getStudent_Year());
//        holder.chk_attendance.setText(students.getStudent_Rollno());
        return row;

    }
}
