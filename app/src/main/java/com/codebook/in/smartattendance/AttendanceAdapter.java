package com.codebook.in.smartattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Attendance> mAttendanceList;

    public AttendanceAdapter(Context context, ArrayList<Attendance> attendanceList) {
        mContext = context;
        mAttendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return mAttendanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAttendanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_students_attendance, null);
        }

        Attendance attendance = mAttendanceList.get(position);

        // Populate the other data fields in your ListView item layout
        TextView rollNumberTextView = view.findViewById(R.id.mystud_rollno);
        rollNumberTextView.setText(String.valueOf(attendance.getRollNumber()));

        TextView studentNameTextView = view.findViewById(R.id.mystud_name);
        studentNameTextView.setText(attendance.getStudentName());

        // Add a CompoundButton.OnCheckedChangeListener to the checkbox to handle changes to its checked state
        CheckBox attendanceCheckBox = view.findViewById(R.id.mystud_chk);
        attendanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendance.setChecked(isChecked);
            }
        });

        // Set the checked state of the checkbox based on the data for this item
        attendanceCheckBox.setChecked(attendance.isChecked());

        return view;
    }

}
