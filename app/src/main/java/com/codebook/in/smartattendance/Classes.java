package com.codebook.in.smartattendance;

public class Classes {
    private String Class_Year;
    private String Class_Branch;
    private String Class_Section;
    private String Class_Subject;


    public Classes(String class_Year, String class_Branch, String class_Section) {
        this.Class_Year=class_Year;
        this.Class_Branch=class_Branch;
        this.Class_Section=class_Section;
    }

    public String getClass_Year() {
        return Class_Year;
    }

    public void setClass_Year(String class_Year) {
        Class_Year = class_Year;
    }

    public String getClass_Branch() {
        return Class_Branch;
    }

    public void setClass_Branch(String class_Branch) {
        Class_Branch = class_Branch;
    }

    public String getClass_Section() {
        return Class_Section;
    }

    public void setClass_Section(String class_Section) {
        Class_Section = class_Section;
    }

    public String getClass_Subject() {
        return Class_Subject;
    }

    public void setClass_Subject(String class_Subject) {
        Class_Subject = class_Subject;
    }
}
