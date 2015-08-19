package com.sandeepani.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.view.R;

import java.util.List;

/**
 * Created by Vijay on 3/25/15.
 */
public class CustomDialogueAdapter extends ArrayAdapter<StudentDTO> {

    private List<StudentDTO> list;
    private int resource;
    private LayoutInflater inflater;
    public SparseBooleanArray mSelectedItemsIds;
    private int examId;
    private IOnSwichChildListener iOnSwichChildListener;
    private boolean involveOnCheckedListener = false;

    public CustomDialogueAdapter(Context context, int resource, List<StudentDTO> list, int examId) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        this.resource = resource;
        this.examId = examId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iOnSwichChildListener = (IOnSwichChildListener) context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.childIdTV = (TextView) convertView.findViewById(R.id.child_id);
            holder.childNameTV = (TextView) convertView.findViewById(R.id.child_name);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StudentDTO studentDTO = getItem(position);
        holder.childIdTV.setText(studentDTO.getStudentId() + "");
        holder.childNameTV.setText(studentDTO.getStundentName());
        holder.radioButton.setTag(position);
        if (examId == position) {
            holder.radioButton.setChecked(true);
            //    involveOnCheckedListener = true;
        } else {
            holder.radioButton.setChecked(false);
        }
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int value = Integer.parseInt(buttonView.getTag().toString());
                if (isChecked && value != examId) {
                    notifyDataSetChanged();
                    iOnSwichChildListener.onSwitchChild(value);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView childIdTV, childNameTV;
        RadioButton radioButton;
    }
}
