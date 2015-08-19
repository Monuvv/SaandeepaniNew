package com.sandeepani.model;

import java.util.ArrayList;

/**
 * Created by Vijay on 3/25/15.
 */
public class ParentModel {
    private String educationalQualification;
    private String email;
    private String username;
    private String profession;
    private String name;
    private String officeNumber;
    private String mobileNumber;
    private int numberOfChildren;
    private ArrayList<StudentDTO> childList;

    public String getEducationalQualification() {
        return educationalQualification;
    }

    public void setEducationalQualification(String educationalQualification) {
        this.educationalQualification = educationalQualification;
    }

    public ArrayList<StudentDTO> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<StudentDTO> childList) {
        this.childList = childList;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> numberOfChild = new ArrayList<String>();

    public void setNumberOFChild(ArrayList<String> numberOfChild) {
        this.numberOfChild = numberOfChild;

    }

    public ArrayList<String> getNumberOFChild(ArrayList<String> numberOfChild) {
        return numberOfChild;
    }
}
