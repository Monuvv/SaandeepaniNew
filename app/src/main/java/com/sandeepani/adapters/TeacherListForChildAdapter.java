package com.sandeepani.adapters;

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
 * Created by Vijay on 4/16/15.
 */
public class TeacherListForChildAdapter extends BaseAdapter {

    ArrayList<HashMap<String,String>> teacherList;
    Context context;
    private LayoutInflater inflater;

    public  TeacherListForChildAdapter(Context context,ArrayList<HashMap<String,String>>  teacherList){
        this.teacherList = teacherList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return teacherList.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
////        if (inflater == null) {
////            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            if (convertView == null) {
//                LayoutInflater inflater = LayoutInflater.from(context);
//                convertView = inflater.inflate(R.layout.teacher_list_for_child_adapter, null);
//                TextView teacherName = (TextView) convertView.findViewById(R.id.drop_down_item);
////                if (position == 0) {
////                    teacherName.setText("----Select Teacher----");
////
////                } else {
//                    teacherName.setText(teacherList.get(position).get("username"));
//                }
//
//
////            }
//        //}
//        return convertView;

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.teacher_list_for_child_adapter, null);

        TextView teacherName = (TextView) convertView.findViewById(R.id.drop_down_item);
//        if(position ==0){
//            teacherName.setText("----Select Teacher----");
//
//
//        }
//        else {
            teacherName.setText(teacherList.get(position).get("username"));
//        }

        return convertView;
    }

}
