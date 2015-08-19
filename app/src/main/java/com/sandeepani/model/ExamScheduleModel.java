package com.sandeepani.model;

/**
 * Created by Sandeep on 29-03-2015.
 */
public class ExamScheduleModel {
    private String subjectName="";
    private String subjectSyllabus = "";
    private String teacherName = "";
    private String examsStartTime = "";
    private String examsDate = "";


    public void setExamsDate(String examsDate) {
        this.examsDate = examsDate;
    }

    public String getExamsDate() {
        return examsDate;
    }

    public String getExamsEndTime() {
        return examsEndTime;
    }

    public void setExamsEndTime(String examsEndTime) {
        this.examsEndTime = examsEndTime;
    }

    public String getExamsStartTime() {
        return examsStartTime;
    }

    public void setExamsStartTime(String examsStartTime) {
        this.examsStartTime = examsStartTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectSyllabus() {
        return subjectSyllabus;
    }

    public void setSubjectSyllabus(String subjectSyllabus) {
        this.subjectSyllabus = subjectSyllabus;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    private String examsEndTime = "";
}
