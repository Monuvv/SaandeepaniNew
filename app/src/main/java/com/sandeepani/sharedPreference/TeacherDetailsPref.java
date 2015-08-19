package com.sandeepani.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sandeepani.model.TeacherModel;
import com.sandeepani.webserviceparser.TeacherHomeJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Vijay on 3/28/15.
 */
public class TeacherDetailsPref {
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String RESPONSE = "ChildrenList";
    public static final String KEY_CHILDREN_LIST = "Childen_List";

    public TeacherDetailsPref(Context context) {

        this.context = context;
        pref = this.context.getSharedPreferences(RESPONSE, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * @param teacherList
     */

    public void SaveTeacherDetailsToPreference(JSONObject teacherList,String teacherName) {

        Log.d("Setchildlist=", teacherList.toString());
        editor.putString(teacherName, teacherList.toString());
        editor.commit();
        Log.d("Setchildlistafterstore", pref.getString(teacherName, null));
    }

    /**
     * @return ChildrenList .
     */
    public TeacherModel getChildrenListFromPreference(String teacherName) {
        String userData;
        JSONObject showdata = null;
        ArrayList<HashMap<String, String>> getChildrenList = null;
        TeacherModel teacherModel=null;
        LinkedHashMap<String, String> childrenNameAndIdHashMap = null;
        userData = pref.getString(teacherName, null);
        if (userData != null) {
            Log.d("ChildList>>>>", userData);
            try {
                showdata = new JSONObject(userData);
                {
                    int size = showdata.length();
                    for (int i = 0; i < size; i++) {

                            teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(showdata);


                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return teacherModel;
    }

}
