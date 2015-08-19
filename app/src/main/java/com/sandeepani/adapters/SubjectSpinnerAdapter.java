package com.sandeepani.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sandeepani.model.SubjectModel;
import com.sandeepani.view.R;

import java.util.List;

/**
 * Created by Sandeep on 28-03-2015.
 */
public class SubjectSpinnerAdapter extends ArrayAdapter<SubjectModel> {
    private LayoutInflater inflater;
    private List<SubjectModel> gradesList;
    private Typeface typeface;

    /**
     * Constructor
     *
     * @param context  activity context
     * @param resource layout resource
     * @param objects  list of drop down items
     */
    public SubjectSpinnerAdapter(Context context, int resource, List<SubjectModel> objects) {
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
        //GradeModel model = gradesList.get(position);
        holder.spinnerTV.setTag(getItem(position));
        holder.spinnerTV.setText(getItem(position).getSubjectName());
        return convertView;
    }

    private class ViewHolder {
        TextView spinnerTV;
    }
}
