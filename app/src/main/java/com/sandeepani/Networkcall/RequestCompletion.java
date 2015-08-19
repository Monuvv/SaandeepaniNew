package com.sandeepani.Networkcall;

import org.json.JSONArray;
import org.json.JSONObject;


public interface RequestCompletion {

    public void onRequestCompletion(JSONObject responseJson,JSONArray responseArray);
    public void onRequestCompletionError(String error);
}



