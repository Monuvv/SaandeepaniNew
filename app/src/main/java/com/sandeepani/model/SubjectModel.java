package com.sandeepani.model;

/**
 * Created by Sandeep on 28-03-2015.
 */
public class SubjectModel {
    private int subjectId;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    private String subjectName = " ";
}
