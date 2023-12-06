package com.codebook.in.smartattendance;

public class ExcelSheetData {

    private String rollno;
    private String name;

    public ExcelSheetData(String rollno, String name) {
        this.rollno = rollno;
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getName() {
        return name;
    }
}


