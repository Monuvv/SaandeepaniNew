package com.sandeepani.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sandeepani.utils.Message;
import com.sandeepani.view.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Vijay on 3/28/15.
 */
public class NotificationAdapter extends BaseAdapter {

    public Activity context;
    private LayoutInflater inflater;
    private List<Message> messages ;

    public NotificationAdapter(Activity context,  List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
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
            convertView = inflater.inflate(R.layout.notifiction_item, null);

        TextView msgTV = (TextView) convertView.findViewById(R.id.msg);
        TextView timeTV = (TextView) convertView.findViewById(R.id.time);

        msgTV.setText(messages.get(position).getMsg());
        timeTV.setText(DateFormat.getDateTimeInstance().format(new Date(Long.parseLong(messages.get(position).get_time()))) );
        convertView.setTag(messages.get(position).get_type());


        return convertView;

    }
}
