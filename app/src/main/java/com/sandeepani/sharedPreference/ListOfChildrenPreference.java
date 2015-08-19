package com.sandeepani.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
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
public class ListOfChildrenPreference {
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String RESPONSE = "ChildrenList";
    public static final String KEY_CHILDREN_LIST = "Childen_List";

    public ListOfChildrenPreference(Context context) {

        this.context = context;
        pref = this.context.getSharedPreferences(RESPONSE, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * @param ChildrenList
     */

    public void SaveChildrenListToPreference(JSONArray ChildrenList) {

        Log.d("Setchildlist=", ChildrenList.toString());
        editor.putString(KEY_CHILDREN_LIST, ChildrenList.toString());
        editor.commit();
        Log.d("Setchildlistafterstore", pref.getString(KEY_CHILDREN_LIST, null));
    }

    /**
     * @return ChildrenList .
     */
    public ArrayList<HashMap<String, String>> getChildrenListFromPreference() {
        String userData;
        JSONArray showdata = null;
        ArrayList<HashMap<String, String>> getChildrenList = null;
        LinkedHashMap<String, String> childrenNameAndIdHashMap = null;
        userData = pref.getString(KEY_CHILDREN_LIST, null);
        if (userData != null) {
            Log.d("ChildList>>>>", userData);
            try {
                showdata = new JSONArray(userData);
                {
                    int size = showdata.length();
                    for (int i = 0; i < size; i++) {
                        try {
                            JSONObject object = showdata.getJSONObject(i);
                            JSONArray childrenArray = object.getJSONArray("children");
                            getChildrenList = new ArrayList<HashMap<String, String>>();
                            for (int j = 0; j < childrenArray.length(); j++) {
                                childrenNameAndIdHashMap = new LinkedHashMap<String, String>();
                                JSONObject noOfStudent = childrenArray.getJSONObject(j);
                                if (noOfStudent.has("studentId")) {
                                    childrenNameAndIdHashMap.put("studentId", noOfStudent.getString("studentId"));
                                }
                                if (noOfStudent.has("studentName")) {
                                    childrenNameAndIdHashMap.put("studentName", noOfStudent.getString("studentName"));
                                }
                                getChildrenList.add(childrenNameAndIdHashMap);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.i("ChildataPreference", getChildrenList.toString());
        return getChildrenList;
    }

}
