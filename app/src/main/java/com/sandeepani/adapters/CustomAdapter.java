package com.sandeepani.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sandeepani.model.GradeModel;
import com.sandeepani.view.R;

import java.util.List;


public class CustomAdapter extends ArrayAdapter<GradeModel> {
    private LayoutInflater inflater;
    private List<GradeModel> gradesList;
    private Typeface typeface;

    /**
     * Constructor
     *
     * @param context  activity context
     * @param resource layout resource
     * @param objects  list of drop down items
     */
    public CustomAdapter(Context context, int resource, List<GradeModel> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        gradesList = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drop_down_item, null);
            holder = new ViewHolder();
            holder.spinnerTV = (TextView) convertView.findViewById(R.id.drop_down_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GradeModel model = gradesList.get(position);
        holder.spinnerTV.setTag(model);
        holder.spinnerTV.setText(model.getGradeName() + " " + model.getSection());
        return convertView;
    }

    private class ViewHolder {
        TextView spinnerTV;
    }
}
