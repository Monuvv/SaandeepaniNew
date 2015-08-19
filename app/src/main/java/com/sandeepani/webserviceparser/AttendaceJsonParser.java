package com.sandeepani.webserviceparser;

import com.sandeepani.model.GradeModel;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.model.SubjectModel;
import com.sandeepani.model.TeacherModel;
import com.sandeepani.view.Teacher.AttendenceUpdateActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 21-03-2015.
 */
public class AttendaceJsonParser {
    public static AttendaceJsonParser teacherHomeJsonParser = null;

    private AttendaceJsonParser() {

    }

    public static AttendaceJsonParser getInstance() {
        if (teacherHomeJsonParser == null) {
            teacherHomeJsonParser = new AttendaceJsonParser();
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
                    if (object.has("stdentId")) {
                        studentDTO.setStudentId(object.getInt("stdentId"));
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

    public   ArrayList<StudentDTO>  getAbsentStudentlist(JSONArray obj){
        {
            TeacherModel teacherModel = new TeacherModel();
            ArrayList<StudentDTO> studentList=null;

            try {
                if(obj.length()>0 && obj.getJSONObject(0)!=null) {
                    JSONObject jsonObject = obj.getJSONObject(0);

                    // if (jsonObject.has("grades")) {
                    ArrayList<GradeModel> gradesList = new ArrayList<GradeModel>();
                    //    JSONArray gradesArray = jsonObject.getJSONArray("grades");

                    if (jsonObject.has("total_absent")) {
                        studentList = new ArrayList<StudentDTO>();
                        JSONArray studentsArray = jsonObject.getJSONArray("students");
                        int studentsLength = studentsArray.length();
                        for (int j = 0; j < studentsLength; j++) {
                            StudentDTO studentDTO = new StudentDTO();
                            JSONObject studentObject = studentsArray.getJSONObject(j);
                            if (studentObject.has("studentName")) {
                                studentDTO.setStundentName(studentObject.getString("studentName"));
                            }
                            if (studentObject.has("stdentId")) {
                                studentDTO.setStudentId(studentObject.getInt("stdentId"));
                            }
                            studentList.add(studentDTO);
                            studentDTO = null;
                        }


                        //    }
                    }
                    //   gradeModel.setStudentsModels(studentList);
                    teacherModel.setGradeModels(gradesList);
                    //  teacherModel.getGradeModels().add(gradeModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return studentList;
        }
    }

    public TeacherModel getTeacherModel(JSONArray obj/*,String tagName*/) {
        TeacherModel teacherModel = new TeacherModel();
        ArrayList<StudentDTO> studentList=null;
        ArrayList<StudentDTO> absentstudentList=null;

        try {
            if(obj.length()>0 && obj.getJSONObject(0)!=null) {
                JSONObject jsonObject = obj.getJSONObject(0);

                // if (jsonObject.has("grades")) {
                ArrayList<GradeModel> gradesList = new ArrayList<GradeModel>();
                //    JSONArray gradesArray = jsonObject.getJSONArray("grades");

                GradeModel gradeModel = new GradeModel();
                if (jsonObject.has("grade")) {
                    gradeModel.setGradeName(jsonObject.getString("grade"));

                }
                if (jsonObject.has("section")) {
                    gradeModel.setSection(jsonObject.getString("section"));

                }

                //----------------------
                if (jsonObject.has("attendanceDoneFlag")) {
                    if (jsonObject.getString("attendanceDoneFlag").toString().equals("Y")) {
                        AttendenceUpdateActivity.hasattendancedone=true;
                        if (jsonObject.has("absentees")) {
                            absentstudentList = new ArrayList<StudentDTO>();
                            JSONArray absentstudentsArray = jsonObject.getJSONArray("absentees");
                            int studentsLength = absentstudentsArray.length();
                            for (int j = 0; j < studentsLength; j++) {
                                StudentDTO studentDTO = new StudentDTO();
                                JSONObject studentObject = absentstudentsArray.getJSONObject(j);
                                if (studentObject.has("studentName")) {
                                    studentDTO.setStundentName(studentObject.getString("studentName"));
                                }
                                if (studentObject.has("stdentId")) {
                                    studentDTO.setStudentId(studentObject.getInt("stdentId"));
                                }
                                absentstudentList.add(studentDTO);
                                studentDTO = null;
                            }
                        }
                    }
                    else
                    {
                        AttendenceUpdateActivity.hasattendancedone=false;
                    }
                }
                //-------------------------------

                if (jsonObject.has("students"/*tagName*/)) {
                    studentList = new ArrayList<StudentDTO>();
                    JSONArray studentsArray = jsonObject.getJSONArray("students");
                    int studentsLength = studentsArray.length();
                    for (int j = 0; j < studentsLength; j++) {
                        StudentDTO studentDTO = new StudentDTO();
                        JSONObject studentObject = studentsArray.getJSONObject(j);
                        if (studentObject.has("studentName")) {
                            studentDTO.setStundentName(studentObject.getString("studentName"));
                        }
                        if (studentObject.has("stdentId")) {
                            studentDTO.setStudentId(studentObject.getInt("stdentId"));
                        }
                        studentList.add(studentDTO);
                        studentDTO = null;
                    }
                }
                gradeModel.setAbsentStudentsModels(absentstudentList);
                gradeModel.setStudentsModels(studentList);
                teacherModel.setGradeModels(gradesList);
                teacherModel.getGradeModels().add(gradeModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teacherModel;
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
