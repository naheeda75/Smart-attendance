package com.codebook.in.smartattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<ExcelSheetData> dataList;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<ExcelSheetData> dataList) {
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_students, null);
            holder = new ViewHolder();
            holder.rollno = convertView.findViewById(R.id.class_year);
            holder.name = convertView.findViewById(R.id.class_branch);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExcelSheetData data = dataList.get(position);
        holder.rollno.setText(data.getRollno());
        holder.name.setText(data.getName());
        return convertView;
    }

    static class ViewHolder {
        TextView rollno;
        TextView name;
    }
}
