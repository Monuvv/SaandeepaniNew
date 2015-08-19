package com.sandeepani.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.sandeepani.model.ParentModel;

/**
 * Created by vijay on 2/25/2015.
 */
public abstract class Constants {
    public static ProgressDialog progress;
    static SharedPreferences clearSharedPreferenceForLogout;
    public static String SWITCH_CHILD_FLAG = "No Child Selected";
    public static int SET_SWITCH_CHILD_ID = 0;
    public static void showMessage(Context context, String title, String message){
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
            }
        });
        alert.show();
    }

    public static void showProgress(Context context){
        progress = new ProgressDialog(context);
        progress.setMessage("Please Wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
    }
    public static void stopProgress(Context context){
        progress.dismiss();
    }

    public static void logOut(Activity activity){
        clearSharedPreferenceForLogout = activity.getSharedPreferences("Response", 0);
        clearSharedPreferenceForLogout = activity.getSharedPreferences("MyChild_Preferences", 0);
        SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
        editor.clear();
        editor.commit();
        activity.finish();
    }

    public static String getChildNameAfterSelecting(int position,ParentModel parentModel){
        String childName = parentModel.getChildList().get(position).getStundentName();
        System.out.println("Switching Child Name::" + childName);
        return childName;
    }

    public static int getChildIdAfterSelecting(int position,ParentModel parentModel){
        int getStudentId =  parentModel.getChildList().get(position).getStudentId();
        System.out.println("Switching Child ID::"+ getStudentId);
        return getStudentId;
    }

    public static String getChildGradefterSelecting(int position,ParentModel parentModel){
        String grade =  parentModel.getChildList().get(position).getGrade();
        String section=parentModel.getChildList().get(position).getSection();
        System.out.println("Switching Child ID::"+ grade);
        return grade+""+section;
    }

}
