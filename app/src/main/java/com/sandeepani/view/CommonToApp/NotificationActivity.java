package com.sandeepani.view.CommonToApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sandeepani.adapters.NotificationAdapter;
import com.sandeepani.utils.Message;
import com.sandeepani.utils.MessageDBHandler;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.Parent.AttendanceActivity;
import com.sandeepani.view.Parent.ChildHomeWorkActivity;
import com.sandeepani.view.Parent.ChildrenTimeTableActivity;
import com.sandeepani.view.Parent.ExamsActivity;
import com.sandeepani.view.Parent.ParentChatAvtivity;
import com.sandeepani.view.Parent.ParentHomeActivity;
import com.sandeepani.view.Parent.ParentInboxActivity;
import com.sandeepani.view.R;
import com.thehayro.view.InfiniteViewPager;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vijay on 4/5/15.
 */
public class NotificationActivity extends BaseFragmentActivity implements  View.OnClickListener {
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    private TopBar topBar;
    InfiniteViewPager viewPager;
    int currentIndicator = 0;
    ListView notiFicationListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTopBar();
        notiFicationListView = (ListView)findViewById(R.id.notificationListView);

        MessageDBHandler messageBdHandler = new MessageDBHandler(NotificationActivity.this);
        List<Message> messages=  messageBdHandler.getAllMessages();
        Collections.reverse(messages);

notiFicationListView.setAdapter(new NotificationAdapter(NotificationActivity.this,messages));


        notiFicationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("Notification Type",view.getTag().toString());

                  if(view.getTag().toString().equals("homework"))
                      startActivity(new Intent(NotificationActivity.this, ChildHomeWorkActivity.class));
                else

                  if(view.getTag().toString().equals("time_table"))
                      startActivity(new Intent(NotificationActivity.this, ChildrenTimeTableActivity.class));
                  else

                  if(view.getTag().toString().equals("exams"))
                  {
                      Intent intent = new Intent(NotificationActivity.this, ExamsActivity.class);
                      intent.putExtra(getString(R.string.key_from), getString(R.string.key_from_parent));
                      startActivity(intent);
                  }
                  else

                  if(view.getTag().toString().equals("mail_box"))
                      startActivity(new Intent(NotificationActivity.this, ParentInboxActivity.class));
                  else

                  if(view.getTag().toString().equals("chat"))
                      startActivity(new Intent(NotificationActivity.this, ParentChatAvtivity.class));
                  else

                  if(view.getTag().toString().equals("calender"))
                      startActivity(new Intent(NotificationActivity.this, CalendarActivity.class));
                  else

                  if(view.getTag().toString().equals("attendance"))
                      startActivity(new Intent(NotificationActivity.this, AttendanceActivity.class));
                  else

                      startActivity(new Intent(NotificationActivity.this, ParentHomeActivity.class));



            }
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(NotificationActivity.this, ParentHomeActivity.class));
        finish();
    }


    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.title_notification));

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            default:
        }

    }



}
