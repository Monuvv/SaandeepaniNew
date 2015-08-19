package com.sandeepani.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 4/16/15.
 */
public class TeacherListForChildParser {

    public static TeacherListForChildParser teacherListForChildParser = null;

    private TeacherListForChildParser() {

    }

    public static TeacherListForChildParser getInstance() {
        if (teacherListForChildParser == null) {
            teacherListForChildParser = new TeacherListForChildParser();
        }
        return teacherListForChildParser;
    }

    public HashMap<String, String> getTeacherList(JSONArray object) {

        LinkedHashMap<String, String> teacherMap = null;
            try {
                teacherMap  = new LinkedHashMap<String, String>();
                for (int i = 0; i < object.length(); i++) {

                    JSONObject listObj = object.getJSONObject(i);
                    if (listObj.has("username")) {
                        String username = listObj.getString("username");

                        if(listObj.has("displayName")){
                            teacherMap.put(listObj.getString("displayName"), username);

                        }
                    }

                    Log.i("teacherMap", teacherMap.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.i("TeacherArrayList::", teacherMap.toString());
        return teacherMap;
    }

}
