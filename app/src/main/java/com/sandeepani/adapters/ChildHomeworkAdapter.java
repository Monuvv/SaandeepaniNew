package com.sandeepani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 3/27/15.
 */
public class ChildHomeworkAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    ArrayList<HashMap<String,String>> childHmework;
    /**
     *
     * @author Customized List Adapter.
     *
     */
    public ChildHomeworkAdapter(Context context, ArrayList<HashMap<String, String>> childHmework) {
        super();
        this.context = context;
        this.childHmework = childHmework;

    }
    @Override
    public int getCount() {
        return childHmework.size();
    }

    @Override
    public Object getItem(int position) {
        return childHmework.get(position);
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
            convertView = inflater.inflate(R.layout.custom_home_work, null);
        ImageView homeWorkImage = (ImageView) convertView.findViewById(R.id.homework_image);
        TextView homeworkTitle = (TextView) convertView.findViewById(R.id.title);
        TextView homeWorkDescription = (TextView) convertView.findViewById(R.id.descreption);
        homeworkTitle.setText(childHmework.get(position).get("subject"));
        homeWorkDescription.setText(childHmework.get(position).get("message"));
        return convertView;
    }
}

