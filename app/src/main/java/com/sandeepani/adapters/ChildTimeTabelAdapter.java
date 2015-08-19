package com.sandeepani.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/28/15.
 */
public class ChildTimeTabelAdapter extends BaseAdapter {

    public Activity context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,String>> listTimeTabel;

    public ChildTimeTabelAdapter(Activity context,ArrayList<HashMap<String,String>> listTimeTabel) {
        this.context = context;
        this.listTimeTabel = listTimeTabel;
    }
    @Override
    public int getCount() {
        return listTimeTabel.size();
    }

    @Override
    public Object getItem(int position) {
        return listTimeTabel.get(position);
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
            convertView = inflater.inflate(R.layout.child_timetabel_adapter_list, null);

        TextView subjectTimingTV = (TextView) convertView.findViewById(R.id.timingTV);
        TextView subjectNameTV = (TextView) convertView.findViewById(R.id.subjectTV);
        String startTime= listTimeTabel.get(position).get("startTime");
        String endTime= listTimeTabel.get(position).get("endTime");
        subjectNameTV.setText(listTimeTabel.get(position).get("subject"));
        if(startTime == "null" && endTime == "null"){
            subjectTimingTV.setText("8:45" +"-"+"9:45");
        }else {
            subjectTimingTV.setText( startTime +"-"+endTime);
        }

        return convertView;

    }
}
