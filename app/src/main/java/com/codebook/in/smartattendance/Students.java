package com.codebook.in.smartattendance;

public class Students {
    private String Student_Rollno;
    private String Student_Name;
    private String Student_Year;
    private String Student_Branch;
    private String Student_Section;



    public Students(String student_Rollno, String student_Name, String student_Year, String student_Branch, String student_Section) {
        Student_Rollno = student_Rollno;
        Student_Name = student_Name;
        Student_Year = student_Year;
        Student_Branch = student_Branch;
        Student_Section = student_Section;
    }

    public String getStudent_Rollno() {
        return Student_Rollno;
    }

    public void setStudent_Rollno(String student_Rollno) {
        Student_Rollno = student_Rollno;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getStudent_Year() {
        return Student_Year;
    }

    public void setStudent_Year(String student_Year) {
        Student_Year = student_Year;
    }

    public String getStudent_Branch() {
        return Student_Branch;
    }

    public void setStudent_Branch(String student_Branch) {
        Student_Branch = student_Branch;
    }

    public String getStudent_Section() {
        return Student_Section;
    }

    public void setStudent_Section(String student_Section) {
        Student_Section = student_Section;
    }
}
