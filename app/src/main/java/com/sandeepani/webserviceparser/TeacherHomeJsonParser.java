package com.sandeepani.webserviceparser;

import com.sandeepani.model.GradeModel;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.model.SubjectModel;
import com.sandeepani.model.TeacherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 21-03-2015.
 */
public class TeacherHomeJsonParser {
    public static TeacherHomeJsonParser teacherHomeJsonParser = null;

    private TeacherHomeJsonParser() {

    }

    public static TeacherHomeJsonParser getInstance() {
        if (teacherHomeJsonParser == null) {
            teacherHomeJsonParser = new TeacherHomeJsonParser();
        }
        return teacherHomeJsonParser;
    }

    public ArrayList<StudentDTO> getStudentsList(JSONArray jsonArray) {
        ArrayList<StudentDTO> studentList = new ArrayList<StudentDTO>();
        if (jsonArray != null) {
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                try {
                    StudentDTO studentDTO = new StudentDTO();
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.has("studentName")) {
                        studentDTO.setStundentName(object.getString("studentName"));
                    }
                    if (object.has("studentId")) {
                        studentDTO.setStudentId(object.getInt("studentId"));
                    }
                    studentList.add(studentDTO);
                    studentDTO = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return studentList;
    }

    public TeacherModel getTeacherDetails(JSONObject obj) {
        TeacherModel teacherModel = new TeacherModel();
        JSONObject jsonObject = null;
        try {
            if (obj.has("teacher")) {
                jsonObject = obj.getJSONObject("teacher");
            }
            if (jsonObject.has("teacherId")) {
                teacherModel.setTeacherId(jsonObject.getInt("teacherId"));
            }
            if (jsonObject.has("teacherName")) {
                teacherModel.setTeacherName(jsonObject.getString("teacherName"));
            }
            if (jsonObject.has("emailId")) {
                teacherModel.setTeacherEmail(jsonObject.getString("emailId"));
            }
            if (jsonObject.has("grades")) {
                ArrayList<GradeModel> gradesList = new ArrayList<GradeModel>();
                JSONArray gradesArray = jsonObject.getJSONArray("grades");
                int gradesLength = gradesArray.length();
                for (int i = 0; i < gradesLength; i++) {
                    JSONObject gradeJson = gradesArray.getJSONObject(i);
                    GradeModel gradeModel = new GradeModel();
                    if (gradeJson.has("gradeName")) {
                        gradeModel.setGradeName(gradeJson.getString("gradeName"));
                    }
                    if (gradeJson.has("section")) {
                        gradeModel.setSection(gradeJson.getString("section"));
                    }
                    if (gradeJson.has("student")) {
                        ArrayList<StudentDTO> studentList = new ArrayList<StudentDTO>();
                        JSONArray studentsArray = gradeJson.getJSONArray("student");
                        int studentsLength = studentsArray.length();
                        for (int j = 0; j < studentsLength; j++) {
                            StudentDTO studentDTO = new StudentDTO();
                            JSONObject studentObject = studentsArray.getJSONObject(j);
                            if (studentObject.has("studentName")) {
                                studentDTO.setStundentName(studentObject.getString("studentName"));
                            }
                            if (studentObject.has("studentId")) {
                                studentDTO.setStudentId(studentObject.getInt("studentId"));
                            }
                            studentList.add(studentDTO);
                            studentDTO = null;
                        }
                        gradeModel.setStudentsModels(studentList);
                        studentList = null;
                    }
                    gradesList.add(gradeModel);
                    gradeModel = null;
                }
                teacherModel.setGradeModels(gradesList);
                gradesList = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacherModel;
    }

    public TeacherModel  getSubjectListM(JSONObject jsonObject){
        {
            TeacherModel teacherModel = new TeacherModel();
            try {
              //  JSONObject jsonObject = new JSONObject(str);
                if (jsonObject.has("teacherId")) {
                    teacherModel.setTeacherId(Integer.parseInt(jsonObject.getString("teacherId")));
                }
                if (jsonObject.has("username")) {
                    teacherModel.setTeacherName(jsonObject.getString("username"));
                }
                if (jsonObject.has("grade")) {
                    teacherModel.setTeacherGrade(jsonObject.getString("grade"));
                }
                if (jsonObject.has("section")) {
                    teacherModel.setTeacherSection(jsonObject.getString("section"));
                }
                if (jsonObject.has("subjects")) {
                    JSONArray subjectsArray = jsonObject.getJSONArray("subjects");
                    int size = subjectsArray.length();
                    ArrayList<SubjectModel> subjectsList = new ArrayList<SubjectModel>();
                    for (int i = 0; i < size; i++) {
                        JSONObject subjectObj = subjectsArray.getJSONObject(i);
                        SubjectModel subjectModel = new SubjectModel();
                        if (subjectObj.has("subjectId")) {
                            subjectModel.setSubjectId(subjectObj.getInt("subjectId"));
                        }
                        if (subjectObj.has("subjectName")) {
                            subjectModel.setSubjectName(subjectObj.getString("subjectName"));
                        }
                        subjectsList.add(subjectModel);
                        subjectModel = null;
                        subjectObj = null;
                    }
                    teacherModel.setSubjectsList(subjectsList);
                    subjectsList = null;
                    subjectsArray = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return teacherModel;
        }
    }

    public TeacherModel getSubjectsList(String str) {
        TeacherModel teacherModel = new TeacherModel();
        try {
            JSONObject jsonObject = new JSONObject(str);
            if (jsonObject.has("teacherId")) {
                teacherModel.setTeacherId(Integer.parseInt(jsonObject.getString("teacherId")));
            }
            if (jsonObject.has("username")) {
                teacherModel.setTeacherName(jsonObject.getString("username"));
            }
            if (jsonObject.has("grade")) {
                teacherModel.setTeacherGrade(jsonObject.getString("grade"));
            }
            if (jsonObject.has("section")) {
                teacherModel.setTeacherSection(jsonObject.getString("section"));
            }
            if (jsonObject.has("subjects")) {
                JSONArray subjectsArray = jsonObject.getJSONArray("subjects");
                int size = subjectsArray.length();
                ArrayList<SubjectModel> subjectsList = new ArrayList<SubjectModel>();
                for (int i = 0; i < size; i++) {
                    JSONObject subjectObj = subjectsArray.getJSONObject(i);
                    SubjectModel subjectModel = new SubjectModel();
                    if (subjectObj.has("subjectId")) {
                        subjectModel.setSubjectId(subjectObj.getInt("subjectId"));
                    }
                    if (subjectObj.has("subjectName")) {
                        subjectModel.setSubjectName(subjectObj.getString("subjectName"));
                    }
                    subjectsList.add(subjectModel);
                    subjectModel = null;
                    subjectObj = null;
                }
                teacherModel.setSubjectsList(subjectsList);
                subjectsList = null;
                subjectsArray = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacherModel;
    }
}
