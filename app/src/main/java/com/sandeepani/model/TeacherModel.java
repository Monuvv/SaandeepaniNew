package com.sandeepani.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sandeep on 26-03-2015.
 */
public class TeacherModel implements Serializable {
    private int teacherId;
    private String teacherName = "";
    private String teacherEmail = "";
    private String teacherGrade = "";
private String isAttendanceDone= "N";
    public ArrayList<SubjectModel> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(ArrayList<SubjectModel> subjectsList) {
        this.subjectsList = subjectsList;
    }

    private ArrayList<SubjectModel> subjectsList;

    public String getTeacherSection() {
        return teacherSection;
    }

    public void setTeacherSection(String teacherSection) {
        this.teacherSection = teacherSection;
    }

    public String getTeacherGrade() {
        return teacherGrade;
    }

    public void setTeacherGrade(String teacherGrade) {
        this.teacherGrade = teacherGrade;
    }

    private String teacherSection = "";

    public ArrayList<GradeModel> getGradeModels() {
        return gradeModels;
    }

    public void setGradeModels(ArrayList<GradeModel> gradeModels) {
        this.gradeModels = gradeModels;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    private ArrayList<GradeModel> gradeModels;

    public String getIsAttendanceDone() {
        return isAttendanceDone;
    }

    public void setIsAttendanceDone(String isAttendanceDone) {
        this.isAttendanceDone = isAttendanceDone;
    }
}
