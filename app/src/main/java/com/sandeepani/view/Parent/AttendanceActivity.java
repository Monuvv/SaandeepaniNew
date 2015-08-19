package com.sandeepani.view.Parent;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseFragmentActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class AttendanceActivity extends BaseFragmentActivity implements RequestCompletion,OnClickListener,IOnSwichChildListener
{
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    private int selectedChildPosition = 0;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
    String childName;
    int getChildId = 0;
    LinearLayout calendar1,today;
    ImageView handleImg;
    TextView totalTV,presentTV, apsentTV;
    private static int monthOfAttendence ;
    private static int yearOfAttendence ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        try {
            setContentView(R.layout.activity_attendance);
            setTopBar();
            switchChildBar();
            setOnClickListener();
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
            //calendar1 = (LinearLayout) findViewById(R.id.calendar1);
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

            //handleImg = (ImageView) findViewById(R.id.handleImg);
            handleImg.setTag("close");
            ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));
            handleImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    if (v.getTag().toString().equals("open")) {
//                        v.setTag("close");
//                        calendar1.setVisibility(View.GONE);
//                    } else {
                        v.setTag("open");
                        calendar1.setVisibility(View.VISIBLE);
//                    }
                }
            });
            // Setup listener
            final CaldroidListener listener = new CaldroidListener() {
                @Override
                public void onSelectDate(Date date, View view) {
//					Calendar cal = Calendar.getInstance();
//					cal.setTime(date);
                }
                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                    monthOfAttendence = month;
                    yearOfAttendence = year;
                    Toast.makeText(getApplicationContext(), text, 	Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.calMonth)).setText(getMonth(month) + " " + year);
                    getAttendance(Constants.SET_SWITCH_CHILD_ID,month,year);
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
//        Calendar cal = Calendar.getInstance();
//        getAttendance(Constants.SET_SWITCH_CHILD_ID,"" + (cal.get(Calendar.MONTH) + 1), "" + cal.get(Calendar.DAY_OF_MONTH));
    }



    public void setOnClickListener() {
        calendar1 = (LinearLayout) findViewById(R.id.calendar1);
        handleImg = (ImageView) findViewById(R.id.handleImg);
        today = (LinearLayout) findViewById(R.id.today);
        totalTV = (TextView) findViewById(R.id.todalTv);
        presentTV = (TextView) findViewById(R.id.presentTv);
        apsentTV = (TextView) findViewById(R.id.absentTv);
        today.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Log.i(TAG, responseJson.toString());
        attendenceRecordUdateUI(responseJson);
        Constants.stopProgress(this);
    }



    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("timetable Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.child_name:
                startActivity(new Intent(this, ProfileFragmentActivity.class));
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;
//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//                break;
            default:
               //Enter code in the event that that no cases match
        }
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {

        getAttendance(Constants.SET_SWITCH_CHILD_ID,monthOfAttendence,yearOfAttendence);

        childName = Constants.getChildNameAfterSelecting(selectedChildPosition,appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition,appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::",Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();

    }
    public void attendenceRecordUdateUI(JSONObject responseJson){
        try {
            totalTV.setText(responseJson.getString("total_working_days"));
            presentTV.setText(responseJson.getString("present_days"));
            apsentTV.setText(responseJson.getString("total_absent_days"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void getAttendance(int childId,int month, int year) {
        String Url_cal = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            Url_cal = getString(R.string.base_url) + getString(R.string.attendance)+childId+"/month/"+ month + "/" + year;
            Log.i("Attendance URl", Url_cal);
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(Url_cal);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.attendence));
     //   topBar.logoutIV.setOnClickListener(this);

    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();

        switchChild.childNameTV.setText("Name");
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



}
