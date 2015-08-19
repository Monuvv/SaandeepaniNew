package com.sandeepani.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/28/15.
 */
public class ChildTimeTabelParser {
    public static ChildTimeTabelParser childTimeTabelParser = null;

    private ChildTimeTabelParser() {

    }

    public static ChildTimeTabelParser getInstance() {
        if (childTimeTabelParser == null) {
            childTimeTabelParser = new ChildTimeTabelParser();
        }
        return childTimeTabelParser;
    }

    public static ArrayList<HashMap<String, String>> getChildrenTimeTabel(JSONArray jsonArray) {
        ArrayList<HashMap<String, String>> childTimeTableArray = new ArrayList<HashMap<String, String>>();
        LinkedHashMap<String, String> childTimeTableMap = null;
        try {
            if (jsonArray != null) {
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    for (int j = 0; j < object.length(); j++) {
                        childTimeTableMap = new LinkedHashMap<String, String>();
                        if (object.has("subject")) {
                            String subject = object.getString("subject");
                            childTimeTableMap.put("subject", subject);
                        }
                        if (object.has("teacher")) {
                            String teacher = object.getString("teacher");
                            childTimeTableMap.put("teacher", teacher);
                        }
                        if (object.has("startTime")) {
                            String startTime = object.getString("startTime");
                            childTimeTableMap.put("startTime", startTime);
                        }
                        if (object.has("endTime")) {
                            String endTime = object.getString("endTime");
                            childTimeTableMap.put("endTime", endTime);
                        }
                        Log.i("childHomeWorkMap", childTimeTableMap.toString());
                    }
                    childTimeTableArray.add(childTimeTableMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("childHomeWorkArrayList", childTimeTableArray.toString());
        return childTimeTableArray;
    }
}
