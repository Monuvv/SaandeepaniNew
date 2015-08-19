package com.sandeepani.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class ChildHomeWorkJsonParser {
    public static ChildHomeWorkJsonParser childHomeWorkJsonParser = null;

    private ChildHomeWorkJsonParser() {

    }

    public static ChildHomeWorkJsonParser getInstance() {
        if (childHomeWorkJsonParser == null) {
            childHomeWorkJsonParser = new ChildHomeWorkJsonParser();
        }
        return childHomeWorkJsonParser;
    }

    public static ArrayList<HashMap<String, String>> getChildrenHomework(JSONObject jsonObject) {
        ArrayList<HashMap<String, String>> childHomeWork = null;
        LinkedHashMap<String, String> childHomeWorkMap = null;
        try {
            if (jsonObject != null) {
                int size = jsonObject.length();
                for (int i = 0; i < size; i++) {
                    JSONArray conversationArray = jsonObject.getJSONArray("homeworks");
                    childHomeWork = new ArrayList<HashMap<String, String>>();
                    for (int j = 0; j < conversationArray.length(); j++) {
                        childHomeWorkMap = new LinkedHashMap<String, String>();
                        JSONObject homeworkData = conversationArray.getJSONObject(j);

                        if (homeworkData.has("message")) {
                            String message = homeworkData.getString("message");
                            childHomeWorkMap.put("message", message);
                        }

                        if (homeworkData.has("subject")) {
                            String subject = homeworkData.getString("subject");
                            childHomeWorkMap.put("subject", subject);
                        }
                        childHomeWork.add(childHomeWorkMap);
                        Log.i("childHomeWorkMap", childHomeWorkMap.toString());
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ArrayList", childHomeWork.toString());
        return childHomeWork;
    }

}
