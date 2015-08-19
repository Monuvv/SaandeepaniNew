package com.mychild.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.mychild.interfaces.IOnCheckedChangeListener;
import com.mychild.model.StudentDTO;
import com.mychild.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep on 22-03-2015.
 */
public class StudentsListAdapter extends ArrayAdapter<StudentDTO> {
    private List<StudentDTO> list;
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<StudentDTO> temporaryStorage;
    private ArrayList<StudentDTO> absentList;
    public SparseBooleanArray mSelectedItemsIds;
    private IOnCheckedChangeListener iOnCheckedChangeListener = null;
    private boolean hasAbsentList=false;
    private boolean isEditEnabled=false;

    public StudentsListAdapter(Context context, int resource, List<StudentDTO> list) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        temporaryStorage = new ArrayList<StudentDTO>();
        temporaryStorage.addAll(list);
        this.resource = resource;
        iOnCheckedChangeListener = (IOnCheckedChangeListener) context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public StudentsListAdapter(Context context, int resource, List<StudentDTO> list,List<StudentDTO> absentList,boolean hasAbsentList,boolean isEditEnabled) {
        super(context, resource, list);
        this.list = list;
        mSelectedItemsIds = new SparseBooleanArray();
        temporaryStorage = new ArrayList<StudentDTO>();
        temporaryStorage.addAll(list);
        this.resource = resource;
        this.absentList=new ArrayList<StudentDTO>();
        this.absentList.addAll(absentList);
        this.hasAbsentList=hasAbsentList;
        this.isEditEnabled=isEditEnabled;
        iOnCheckedChangeListener = (IOnCheckedChangeListener) context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.studentIdTV = (TextView) convertView.findViewById(R.id.student_id_tv);
            holder.studentNameTV = (TextView) convertView.findViewById(R.id.student_name_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StudentDTO studentDTO = getItem(position);
        if (mSelectedItemsIds.get(studentDTO.getStudentId())) {
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setTag(getItem(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // int pos = Integer.parseInt((buttonView).getTag().toString());
                StudentDTO dto = (StudentDTO) buttonView.getTag();
                if (isChecked)
                    mSelectedItemsIds.put(dto.getStudentId(), true);
                else
                    mSelectedItemsIds.delete(dto.getStudentId());
                iOnCheckedChangeListener.checkedStateChanged(dto, isChecked);
            }
        });
        StudentDTO dto = getItem(position);
        holder.studentIdTV.setText("#" + dto.getStudentId() + "");
        holder.studentNameTV.setText(dto.getStundentName());
        if(hasAbsentList) {
            holder.checkBox.setChecked(true);
            convertView.setEnabled(isEditEnabled);
            holder.checkBox.setEnabled(isEditEnabled);
            for(StudentDTO studentDTO1:absentList){
                int name=studentDTO1.getStudentId();
                if(name == dto.getStudentId()){
                    holder.checkBox.setChecked(false);
                    break;
                }
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView studentIdTV, studentNameTV;
        CheckBox checkBox;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    public void getFilters(String str) {
        list.clear();
        if (str.length() == 0) {
            list.addAll(temporaryStorage);
        } else {
            for (StudentDTO dto : temporaryStorage) {
                String name = dto.getStundentName().toLowerCase();
                if (name.contains(str)) {
                    list.add(dto);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (StudentDTO dto : list) {
            mSelectedItemsIds.put(dto.getStudentId(), true);
        }
        notifyDataSetChanged();
    }
}
