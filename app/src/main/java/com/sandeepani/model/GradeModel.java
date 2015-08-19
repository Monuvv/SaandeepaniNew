package com.sandeepani.model;

import java.util.ArrayList;

/**
 * Created by Sandeep on 26-03-2015.
 */
public class GradeModel {
    private String gradeName;
    private String section = "";
    private ArrayList<StudentDTO> studentsModels;
    private ArrayList<StudentDTO> absentstudentsModels;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public ArrayList<StudentDTO> getStudentsModels() {
        return studentsModels;
    }

    public void setStudentsModels(ArrayList<StudentDTO> studentsModels) {
        this.studentsModels = studentsModels;
    }

    //----------------
    public ArrayList<StudentDTO> getAbsentStudentsModels() {
        return absentstudentsModels;
    }

    public void setAbsentStudentsModels(ArrayList<StudentDTO> absentstudentsModels) {
        this.absentstudentsModels = absentstudentsModels;
    }
    //-----------------------


    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
