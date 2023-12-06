package com.codebook.in.smartattendance;

public class Attendance {
    private String mRollNumber;
    private String mStudentName;
    private boolean mIsChecked;

    public Attendance(String rollNumber, String studentName, boolean isChecked) {
        mRollNumber = rollNumber;
        mStudentName = studentName;
        mIsChecked = isChecked;
    }

    public String getRollNumber() {
        return mRollNumber;
    }

    public void setRollNumber(String rollNumber) {
        mRollNumber = rollNumber;
    }

    public String getStudentName() {
        return mStudentName;
    }

    public void setStudentName(String studentName) {
        mStudentName = studentName;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }
}