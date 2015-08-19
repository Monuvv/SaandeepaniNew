package com.sandeepani.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sandeepani.model.ExamScheduleModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.view.R;

import java.util.List;

/**
 * Created by Sandeep on 29-03-2015.
 */
public class ExamsListviewAdapter extends ArrayAdapter<ExamScheduleModel> {
    private List<ExamScheduleModel> list;
    private int resource;
    private LayoutInflater inflater;
    public SparseBooleanArray mSelectedItemsIds;

    public ExamsListviewAdapter(Context context, int resource, List<ExamScheduleModel> list) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);
            holder.monthTV = (TextView) convertView.findViewById(R.id.month_tv);
            holder.timeTV = (TextView) convertView.findViewById(R.id.time_tv);
            holder.subjectTV = (TextView) convertView.findViewById(R.id.subject_tv);
            holder.weekTV = (TextView) convertView.findViewById(R.id.week_tv);
            holder.subject_SyllabusTV = (TextView) convertView.findViewById(R.id.subject_SyllabusTV);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExamScheduleModel examScheduleModel = getItem(position);
        holder.weekTV.setText(examScheduleModel.getExamsStartTime()+"-"+examScheduleModel.getExamsEndTime());
        holder.subjectTV.setText(examScheduleModel.getSubjectName());
        holder.subject_SyllabusTV.setText(examScheduleModel.getSubjectSyllabus());
        holder.timeTV.setText(examScheduleModel.getExamsStartTime()+ "-"+ examScheduleModel.getExamsEndTime());
        holder.monthTV.setText(CommonUtils.getMonth(examScheduleModel.getExamsStartTime()));
        holder.dateTV.setText(CommonUtils.getDate(examScheduleModel.getExamsStartTime()));
        //ExamScheduleModel studentDTO = getItem(position);
        holder.checkBox.setTag(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        TextView dateTV, monthTV, timeTV, weekTV, subjectTV,subject_SyllabusTV;
        CheckBox checkBox;
    }

}
