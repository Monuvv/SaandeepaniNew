package com.sandeepani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Vijay on 4/1/15.
 */
public class ParentInboxAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> parentInbox;


    /**
     * @author Customized List Adapter.
     */
    public ParentInboxAdapter(Context context, ArrayList<HashMap<String, String>> parentInbox) {
        super();
        this.context = context;
        this.parentInbox = parentInbox;

    }

    @Override
    public int getCount() {
        return parentInbox.size();
    }

    @Override
    public Object getItem(int position) {
        return parentInbox.get(position);
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
            convertView = inflater.inflate(R.layout.parent_inbox_adapter, null);
        ImageView inbox = (ImageView) convertView.findViewById(R.id.inboxMessageIV);
        TextView fromTeacher = (TextView) convertView.findViewById(R.id.fromteacherNameTV);
        TextView mailSubject = (TextView) convertView.findViewById(R.id.mailSubjectTV);
        TextView mailDescription = (TextView) convertView.findViewById(R.id.mailDescriptionTV);
        TextView date=(TextView)convertView.findViewById((R.id.dateTV));

        String oldDate=parentInbox.get(position).get("toDate");
        String newDate=getDate(oldDate);


        date.setText(newDate);
        fromTeacher.setText(parentInbox.get(position).get("fromName"));
        mailSubject.setText(parentInbox.get(position).get("title"));
        mailDescription.setText(parentInbox.get(position).get("messageText"));
        return convertView;
    }

    public String getDate(String oldDate){
        String newDate=null;
        try {
            SimpleDateFormat formatter, FORMATTER;
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            Date date = formatter.parse(oldDate.substring(0, 24));
            FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
             newDate=FORMATTER.format(date);
            System.out.println("OldDate-->" + oldDate);
            System.out.println("NewDate-->" + newDate);
        }catch(Exception e){

        }
        return newDate;
    }
}
