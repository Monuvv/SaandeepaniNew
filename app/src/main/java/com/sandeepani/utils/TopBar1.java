package com.sandeepani.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandeepani.view.R;

public class TopBar1 extends RelativeLayout {
    public ImageView backArrowIV,mMenuSliderIV;
    public TextView titleTV;
    public ImageView logoutIV;

    SharedPreferences clearSharedPreferenceForLogout;


    public TopBar1(Context context) {
        super(context);
    }

    public TopBar1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopBar1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initTopBar() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_top_bar1, this);
        backArrowIV = (ImageView) findViewById(R.id.back_arrow_iv);
        titleTV = (TextView) findViewById(R.id.title_tv);
    }}