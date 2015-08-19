package com.kk.mycalendar;

import java.util.Date;

import android.view.View;


public abstract class CaldroidListener {

    public abstract void onSelectDate(Date date, View view);


    public void onLongClickDate(Date date, View view) {
        // Do nothing
    }


    public void onChangeMonth(int month, int year) {
        // Do nothing
    }

    ;


    public void onCaldroidViewCreated() {
        // Do nothing
    }
}
