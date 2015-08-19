package com.sandeepani.webserviceparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 4/9/15.
 */
public class ChildCalenderEvents {

    public static ChildCalenderEvents childCalenderEventsAdapter = null;

    private ChildCalenderEvents() {

    }

    public static ChildCalenderEvents getInstance() {
        if (childCalenderEventsAdapter == null) {
            childCalenderEventsAdapter = new ChildCalenderEvents();
        }
        return childCalenderEventsAdapter;
    }

    public static ArrayList<HashMap<String, String>> getChildCalenderEvents(JSONObject jsonObject) {
        ArrayList<HashMap<String, String>> childCalenderArrayList = new ArrayList<HashMap<String, String>>();
        LinkedHashMap<String, String> calenderMap = null;
        try {
            if (jsonObject != null) {
             //   int size = jsonObject.length();
               // for (int i = 0; i < size; i++) {
                    JSONArray conversationArray = jsonObject.getJSONArray("events");
                    for (int j = 0; j < conversationArray.length(); j++) {
                        calenderMap = new LinkedHashMap<String, String>();
                        JSONObject calenderData = conversationArray.getJSONObject(j);


                        if (calenderData.has("startTime")) {
                            String startTime = calenderData.getString("startTime");
                            calenderMap.put("startTime", startTime);
                        }

                        if (calenderData.has("title")) {
                            String title = calenderData.getString("title");
                            calenderMap.put("title", title);
                        }

                        if (calenderData.has("endTime")) {
                            String endTime = calenderData.getString("endTime");
                            calenderMap.put("endTime", endTime);
                        }

                        if (calenderData.has("description")) {
                            String description = calenderData.getString("description");
                            calenderMap.put("description", description);
                        }

                        Log.i("childCalenderMap", calenderMap.toString());
                    }
                    childCalenderArrayList.add(calenderMap);
                }
          //  }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("childCalenderList::", childCalenderArrayList.toString());
        return childCalenderArrayList;
    }
}
