package com.sandeepani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sandeepani.model.ResultsModel;
import com.sandeepani.view.R;

import java.util.List;

/**
 * Created by Vijay on 5/21/15.
 */
public class ResultsListAdapter extends ArrayAdapter<ResultsModel> {

    private List<ResultsModel> list;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ResultsListAdapter(Context context, int resource, List<ResultsModel> list) {
        super(context, resource, list);
        this.list = list;
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if (inflater == null)
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null)
//            convertView = inflater.inflate(R.layout.activity_exam_results_tab, null);
//
//        TextView subjectTV = (TextView) convertView.findViewById(R.id.msg);
//        TextView markTV = (TextView) convertView.findViewById(R.id.time);
//        TextView gradeTV = (TextView) convertView.findViewById(R.id.time);
//        TextView statusTV = (TextView) convertView.findViewById(R.id.time);
//
//        ResultsModel resultsModel = getItem(position);
//
//        subjectTV.setText(resultsModel.getResultSubjectName());
//        markTV.setText(resultsModel.getResultMarks()+ "/" +resultsModel.getResultmaxMarks());
//        gradeTV.setText(resultsModel.getResultGrade());
//        statusTV.setText(resultsModel.getResultStatus());
//
//        return convertView;



        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.subjectTV = (TextView) convertView.findViewById(R.id.subjectTV);
            holder.markTV = (TextView) convertView.findViewById(R.id.markTV);
            holder.gradeTV = (TextView) convertView.findViewById(R.id.gradeTV);
            holder.statusTV = (TextView) convertView.findViewById(R.id.statusTV);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ResultsModel resultsModel = getItem(position);

        holder.subjectTV.setText(list.get(position).getResultSubjectName());
        holder.markTV.setText(list.get(position).getResultMarks());
        holder.gradeTV.setText(list.get(position).getResultGrade());
        holder.statusTV.setText(list.get(position).getResultStatus());

        return convertView;
    }

    private class ViewHolder {
        TextView subjectTV, markTV,gradeTV, statusTV;
    }
}
