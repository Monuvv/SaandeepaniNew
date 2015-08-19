package com.sandeepani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.sandeepani.model.MessageModel;
import com.sandeepani.view.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandeep on 04-04-2015.
 */
public class TeacherEmailsListAdapter extends ArrayAdapter<MessageModel> {
    private LayoutInflater inflater;
    private int resource;
    private ArrayList<MessageModel> temporaryStorage;
    private List<MessageModel> list;

    public TeacherEmailsListAdapter(Context context, int resource, List<MessageModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        temporaryStorage = new ArrayList<MessageModel>();
        temporaryStorage.addAll(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.from_MessageTV = (TextView) convertView.findViewById(R.id.from_teacher_name_tv);
            holder.subjectTV = (TextView) convertView.findViewById(R.id.mail_subject_tv);
            holder.mailDescriptionTV = (TextView) convertView.findViewById(R.id.mail_description_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageModel messageModel = getItem(position);
        holder.from_MessageTV.setText(messageModel.getFromID());
        holder.subjectTV.setText(messageModel.getSubject());
        holder.mailDescriptionTV.setText(messageModel.getMessageText());
        holder.dateTV.setText(messageModel.getDate());
        return convertView;
    }

    private class ViewHolder {
        TextView from_MessageTV, subjectTV, mailDescriptionTV, dateTV;
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
            for (MessageModel dto : temporaryStorage) {
                String name = dto.getFromID().toLowerCase();
                String subject = dto.getSubject().toLowerCase();
                if (name.contains(str) || subject.contains(str)) {
                    list.add(dto);
                }
            }
        }
        notifyDataSetChanged();
    }
}
