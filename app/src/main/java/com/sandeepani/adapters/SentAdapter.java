package com.sandeepani.adapters;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sandeepani.view.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SentAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<HashMap<String, String>> parentInbox;

    //   ParentWriteMailToTeacher obj;
//        datepick j;
//    String mm=j.as;
    public static String s,sm;
    public  static TextView mailDescription;

    public SentAdapter()
    {}

    /**
     * @author Customized List Adapter.
     */
    public SentAdapter(Context context, ArrayList<HashMap<String, String>> parentInbox) {
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
            convertView = inflater.inflate(R.layout.activity_sent_adapter, null);
        String oldDate=parentInbox.get(position).get("toDate");
        // String newDate=getDate(oldDate);
//       s=parentInbox.get(position).get("fromName");



        // ImageView inbox = (ImageView) convertView.findViewById(R.id.inboxMessageIV);
        TextView toTeacher = (TextView) convertView.findViewById(R.id.toteacherNameTV);
        TextView mailSub = (TextView) convertView.findViewById(R.id.mailSubTV);
        TextView date1 = (TextView) convertView.findViewById(R.id.dateTV);
        TextView mailDescrip = (TextView) convertView.findViewById(R.id.mailDescripTV);


        //  String oldDate=parentInbox.get(position).get("toDate");



//            SimpleDateFormat formatter,FORMATTER;
//        formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        ParsePosition pos=new ParsePosition(0);
//        Date da=formatter.parse(oldDate.substring(0,24));
//        FORMATTER=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
//        String newDate= FORMATTER.format(da);

        date1.setText(oldDate);
        toTeacher.setText(parentInbox.get(position).get("toId"));
        mailSub.setText(parentInbox.get(position).get("title"));
        mailDescrip.setText(parentInbox.get(position).get("messageText"));
        s=date1.getText().toString();
        // mailDescription.getText().toString();
        return convertView;

    }

//    public String getDate(String oldDate)
//    {
//        String newDate=null;
//        try {
//            SimpleDateFormat formatter, FORMATTER;
//            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//
//            Date dat = formatter.parse(oldDate.substring(0, 24));
//            FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
////            FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
//            newDate=FORMATTER.format(dat);
//            System.out.println("OldDate-->" + oldDate);
//            System.out.println("NewDate-->" + newDate);
//        }catch(Exception e){
//
//        }
//        return newDate;
//    }

}



