package com.sandeepani.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 4/5/15.
 */
public class TeacherTimetabelParser {

    public static TeacherTimetabelParser teacherTimetabelParser = null;

    private TeacherTimetabelParser() {

    }

    public static TeacherTimetabelParser getInstance() {
        if (teacherTimetabelParser == null) {
            teacherTimetabelParser = new TeacherTimetabelParser();
        }
        return teacherTimetabelParser;
    }


    public static ArrayList<HashMap<String, String>> getTeacherTimetabel(JSONObject jsonObject) {
        ArrayList<HashMap<String, String>> teacherTimetabelArray = null;
        LinkedHashMap<String, String> teacherTimetabelMap = null;
        try {
            if (jsonObject != null) {
                int size = jsonObject.length();
                for (int i = 0; i < size; i++) {
                    JSONArray teacherTimetabel = jsonObject.getJSONArray("timeTable");
                    teacherTimetabelArray = new ArrayList<HashMap<String, String>>();
                    for (int j = 0; j < teacherTimetabel.length(); j++) {
                        teacherTimetabelMap = new LinkedHashMap<String, String>();
                        JSONObject timeTabelForTeacher = teacherTimetabel.getJSONObject(j);

                        if (timeTabelForTeacher.has("grade")) {
                            String grade = timeTabelForTeacher.getString("grade");
                            teacherTimetabelMap.put("grade", grade);
                        }

                        if (timeTabelForTeacher.has("section")) {
                            String section = timeTabelForTeacher.getString("section");
                            teacherTimetabelMap.put("section", section);
                        }
                        if (timeTabelForTeacher.has("subject")) {
                            String subject = timeTabelForTeacher.getString("subject");
                            teacherTimetabelMap.put("subject", subject);
                        }
                        if (timeTabelForTeacher.has("day")) {
                            String day = timeTabelForTeacher.getString("day");
                            teacherTimetabelMap.put("day", day);
                        }
                        if (timeTabelForTeacher.has("startTime")) {
                            String startTime = timeTabelForTeacher.getString("startTime");
                            teacherTimetabelMap.put("startTime", startTime);
                        }
                        if (timeTabelForTeacher.has("endTime")) {
                            String endTime = timeTabelForTeacher.getString("endTime");
                            teacherTimetabelMap.put("endTime", endTime);
                        }
                        teacherTimetabelArray.add(teacherTimetabelMap);
                        Log.i("childHomeWorkMap", teacherTimetabelMap.toString());
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TimetabelTeacher:::", teacherTimetabelArray.toString());
        return teacherTimetabelArray;
    }


}
