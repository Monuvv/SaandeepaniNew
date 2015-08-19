package com.sandeepani.customView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.sandeepani.view.R;


/**
 * Created by Vijay on 3/23/15.
 */
public class CustomDialogClass extends Dialog {

    public Activity activity;
    public Dialog dialog;
    //CustomDialogueAdapter adapter;


    public CustomDialogClass(Activity activity) {
        super(activity);
        this.activity = activity;
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listforcustomdialogue);
       /* CustomDialogueAdapter adapter = new CustomDialogueAdapter(activity);
        ListView childList = (ListView) findViewById(R.id.childlist);

        childList.setAdapter(adapter);*/
    }


}
