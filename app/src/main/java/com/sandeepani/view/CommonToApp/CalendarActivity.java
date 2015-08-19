package com.sandeepani.view.CommonToApp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.ChildCalendarAdapter;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.ChildCalenderEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class CalendarActivity extends BaseFragmentActivity implements RequestCompletion, OnClickListener, IOnSwichChildListener {
    public static final String TAG = CalendarActivity.class.getSimpleName();
    LinearLayout calendar1;
    ImageView handleImg;
    ListView calListView;
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    private int selectedChildPosition = 0;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
    int getChildId = 0;
    String childName;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        try {
            setContentView(R.layout.activity_calendar);
            setTopBar();
            switchChildBar();
            //	Calendar minYear = Calendar.getInstance();
            //			minYear.add(Calendar.YEAR, -1);
            //
            //			Calendar maxYear = Calendar.getInstance();
            //			maxYear.add(Calendar.DAY_OF_WEEK, 1);
            //			CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
            //			Date today = new Date();
            //			calendar.setOnDateSelectedListener(this);
            //			calendar.init( minYear.getTime(),maxYear.getTime())
            //		.withSelectedDate(today);
            //
            calendar1 = (LinearLayout) findViewById(R.id.calendar1);
            //scrollView  = (ScrollView)findViewById(R.id.scrollView);
            CaldroidFragment caldroidFragment = new CaldroidFragment();
            CaldroidFragment.type = 0;
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            caldroidFragment.setArguments(args);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            t.commit();
            handleImg = (ImageView) findViewById(R.id.handleImg);
            handleImg.setTag("close");
            ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));
            handleImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					if(v.getTag().toString().equals("open"))
//					{
//						v.setTag("close");
//						calendar1.setVisibility(View.GONE);
//					}
//					else
//					{
                    v.setTag("open");
                    calendar1.setVisibility(View.VISIBLE);
//					}
                }
            });
            // Setup listener
            final CaldroidListener listener = new CaldroidListener() {
                @Override
                public void onSelectDate(Date date, View view) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    getChildCalenderEvent(cal.get(Calendar.DAY_OF_MONTH)+ "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR), Constants.SET_SWITCH_CHILD_ID);
                }

                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                    //	Toast.makeText(getApplicationContext(), text, 	Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.calMonth)).setText(getMonth(month) + " " + year);
                }

                @Override
                public void onLongClickDate(Date date, View view) {
                }

                @Override
                public void onCaldroidViewCreated() {
                }
            };
            caldroidFragment.setCaldroidListener(listener);
            ((View) findViewById(R.id.today)).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CaldroidFragment caldroidFragment = new CaldroidFragment();
                    Bundle args = new Bundle();
                    Calendar cal = Calendar.getInstance();
                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                    caldroidFragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    caldroidFragment.setCaldroidListener(listener);
                    t.replace(R.id.calendar1, caldroidFragment);
                    t.commit();
                    ((TextView) findViewById(R.id.calMonth)).setText(getMonth(cal.get(Calendar.MONTH) + 1) + " " + cal.get(Calendar.YEAR));
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal = Calendar.getInstance();
        getChildCalenderEvent( cal.get(Calendar.DAY_OF_MONTH)+ "-" + (cal.get(Calendar.MONTH) + 1) + "-" +   cal.get(Calendar.YEAR),Constants.SET_SWITCH_CHILD_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                //Enter code in the event that that no cases match
        }

    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("child calender Response success");
        Log.i(TAG, responseJson.toString());
        calListView = (ListView) findViewById(R.id.calListView);

        String numberOfEvents = getNumberOfEvents(responseJson);

        if(!numberOfEvents.contains("0")){
            ArrayList<HashMap<String, String>> childCalenderList= ChildCalenderEvents.getInstance().getChildCalenderEvents(responseJson);
            ChildCalendarAdapter adapter = new ChildCalendarAdapter(CalendarActivity.this, childCalenderList);
            calListView.setAdapter(adapter);

        }
        else {
            Constants.showMessage(this, "Sorry", "No Calender Events...");
        }
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("childcalender Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    public String getNumberOfEvents(JSONObject responseJson){
        String numberOfEvents = null;
        try {
            numberOfEvents = responseJson.getString("no_of_events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return numberOfEvents;
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void getChildCalenderEvent(String date, int childID) {
        Constants.showProgress(this);
        String Url_cal;
        if (CommonUtils.isNetworkAvailable(this)) {
            Url_cal = getString(R.string.base_url) + getString(R.string.calendar_task_parent)+childID+"/"+ date;
            Log.i("ChildCalenderURL::", Url_cal);
            WebServiceCall call = new WebServiceCall(CalendarActivity.this);
            call.getJsonObjectResponse(Url_cal);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.calender));

    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
//        switchChild.childNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
            default:
                return "";
        }
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition,appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition,appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::",Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        String eventDate= cal.get(Calendar.DAY_OF_MONTH)+ "-" + (cal.get(Calendar.MONTH) + 1) + "-" +   cal.get(Calendar.YEAR);
        getChildCalenderEvent(eventDate,getChildId);
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }
}
