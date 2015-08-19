package com.sandeepani.adapters;

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

import com.sandeepani.interfaces.IOnCheckedChangeListener;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.view.R;

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
        //  ViewHolder holder = null;
        CheckBox checkBox;
        TextView studentNameTV,studentIdTV;
        StudentDTO planet =this.getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            //  holder = new ViewHolder();
            studentIdTV = (TextView) convertView.findViewById(R.id.student_id_tv);
            studentNameTV = (TextView) convertView.findViewById(R.id.student_name_tv);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            //  convertView.setTag(holder);
            convertView.setTag(new ViewHolder(studentIdTV,studentNameTV, checkBox));
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    StudentDTO planet = (StudentDTO) cb.getTag();
                    planet.setChecked(cb.isChecked());
                }
            });
        } else {
            //  holder = (ViewHolder) convertView.getTag();
            ViewHolder viewHolder = (ViewHolder) convertView
                    .getTag();
            checkBox = viewHolder.getCheckBox();
            studentIdTV = viewHolder.getStudentIdTV();
            studentNameTV = viewHolder.getStudentNameTV();
        }
        checkBox.setTag(planet);
        checkBox.setChecked(planet.isChecked());
        studentIdTV.setText(String.valueOf(planet.getStudentId()));
        studentNameTV.setText(planet.getStundentName());

        StudentDTO studentDTO = getItem(position);
        if (mSelectedItemsIds.get(studentDTO.getStudentId())) {
            checkBox.setChecked(true);
        }
        //  holder.checkBox.setTag(getItem(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        studentIdTV.setText("#" + dto.getStudentId() + "");
        studentNameTV.setText(dto.getStundentName());
        if(hasAbsentList)
            if(!isEditEnabled){
                checkBox.setChecked(true);
                convertView.setEnabled(isEditEnabled);
                checkBox.setEnabled(isEditEnabled);
                for(StudentDTO studentDTO1:absentList){
                    int name=studentDTO1.getStudentId();
                    if(name == dto.getStudentId()){
                        checkBox.setChecked(false);
                        break;
                    }
                }
            }
            else
            {
                convertView.setEnabled(isEditEnabled);
                checkBox.setEnabled(isEditEnabled);
            }
        return convertView;
    }

    public static class ViewHolder {
        TextView studentIdTV, studentNameTV;
        CheckBox checkBox;

        public ViewHolder(TextView studentIdTV, TextView studentNameTV, CheckBox checkBox) {
            this.studentIdTV = studentIdTV;
            this.studentNameTV = studentNameTV;
            this.checkBox = checkBox;
        }

        public TextView getStudentIdTV() {
            return studentIdTV;
        }

        public void setStudentIdTV(TextView studentIdTV) {
            this.studentIdTV = studentIdTV;
        }

        public TextView getStudentNameTV() {
            return studentNameTV;
        }

        public void setStudentNameTV(TextView studentNameTV) {
            this.studentNameTV = studentNameTV;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
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
