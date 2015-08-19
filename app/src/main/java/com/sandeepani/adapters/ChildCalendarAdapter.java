package com.sandeepani.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sandeepani.view.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ChildCalendarAdapter extends BaseAdapter {

    public Activity context;
    private LayoutInflater inflater;
    private JSONArray ary;
    ArrayList<HashMap<String,String>> childCalenderlist;

    public ChildCalendarAdapter(Activity context,ArrayList<HashMap<String,String>> childCalenderlist) {
        this.context = context;
        this.childCalenderlist = childCalenderlist;
    }
    @Override
    public int getCount() {
        return childCalenderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return childCalenderlist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cal_item, null);
        

		TextView eventTimingTV = (TextView) convertView.findViewById(R.id.timingTV);
        TextView eventNameTV = (TextView) convertView.findViewById(R.id.subjectTV);
        TextView  eventdescTv = (TextView) convertView.findViewById(R.id.desc);
       
        String startTime= childCalenderlist.get(position).get("startTime");
        String endTime=  childCalenderlist.get(position).get("endTime");
        eventNameTV.setText(childCalenderlist.get(position).get("title"));
        eventdescTv.setText(childCalenderlist.get(position).get("description"));
        if(startTime == "null" && endTime == "null"){
            eventTimingTV.setText("0:00" +"-"+"0:00");
        }else {
            eventTimingTV.setText( startTime +"-"+endTime);
        }
        return convertView;

    }

}
