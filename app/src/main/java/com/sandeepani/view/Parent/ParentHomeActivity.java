package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.ParentModel;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.sharedPreference.ListOfChildrenPreference;
import com.sandeepani.sharedPreference.PrefManager;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.CommonToApp.CalendarActivity;
import com.sandeepani.view.CommonToApp.ChangePasswordActivity;
import com.sandeepani.view.CommonToApp.GalleryActivity;
import com.sandeepani.view.CommonToApp.LoginActivity;
import com.sandeepani.view.CommonToApp.NoticeActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.ParentHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParentHomeActivity extends BaseActivity implements RequestCompletion, View.OnClickListener, IOnSwichChildListener {
    public static final String TAG = ParentHomeActivity.class.getSimpleName();
    PrefManager sharedPref;
    private TopBar topBar;
    private int selectedposition = 0;
    private SwitchChildView switchChild;
    private Dialog dialog = null;
    private ParentModel parentModel = null;
    private StudentDTO studentDTO = null;
    private int selectedChildPosition = 0;
    private AppController appController = null;
    int getChildId = 0;
    ListOfChildrenPreference manager;
    SharedPreferences clearSharedPreferenceForLogout;
    String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = (AppController) getApplicationContext();
        sharedPref = new PrefManager(this);
        getParentDetailsWebservicescall();
        setContentView(R.layout.activity_parent_home);
        setTopBar();
        switchChildBar();
        setOnClickListener();
        onChangingChild();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {

        CommonUtils.getLogs("Parent Response success" + responseArray);
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(this);
        //Storing to Shared preference to cache the child list for the parent
        if (responseArray != null) {
            parentModel = ParentHomeJsonParser.getInstance().getParentDetails(responseArray);
            Log.i("####parentModel###", parentModel.getChildList().toString());
            appController.setParentData(parentModel);
            if (parentModel.getNumberOfChildren() >= 0) {
                appController.setSelectedChild(0);
            }
            onChangingChild();
//            ListOfChildrenPreference manager = new ListOfChildrenPreference(this);
//            manager.SaveChildrenListToPreference(responseArray);
        } else {
            Toast.makeText(this, "No data..", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Parent Response Failure");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition, appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::", Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_child:
                if (parentModel != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.child_name:
                startActivity(new Intent(this, ProfileFragmentActivity.class));
                break;

            case R.id.homework:
                startActivity(new Intent(ParentHomeActivity.this, ChildHomeWorkActivity.class));
                break;

            case R.id.time_table:
                startActivity(new Intent(ParentHomeActivity.this, ChildrenTimeTableActivity.class));
                break;

            case R.id.exams:
                Intent intent = new Intent(this, ExamsActivity.class);
                intent.putExtra(getString(R.string.key_from), getString(R.string.key_from_parent));
                startActivity(intent);
                intent = null;
                break;

            case R.id.mail_box:
                startActivity(new Intent(this, ParentInboxActivity.class));
                break;

            case R.id.calender:
                startActivity(new Intent(ParentHomeActivity.this, CalendarActivity.class));
                break;

            case R.id.attendance:
                startActivity(new Intent(ParentHomeActivity.this, AttendanceActivity.class));
                break;

            case R.id.notice:
                Intent in = new Intent(this, NoticeActivity.class);
                in.putExtra(getString(R.string.key_from), getString(R.string.key_from_parent));
                startActivity(in);
                intent = null;
               // startActivity(new Intent(ParentHomeActivity.this, NoticeActivity/*ParentNoticeActivity*/.class));
                break;

            case R.id.transport:
                startActivity(new Intent(this, GalleryActivity.class));
                break;
            case R.id.logoutIV:
                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
                clearSharedPreferenceForLogout = getSharedPreferences("Response", 0);
                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
//                String regId = Pushbots.sharedInstance().regID();
//                WebServiceCall webServiceCall = new WebServiceCall(this);
//                webServiceCall.unRegisterDevice(regId);
                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.transport_homeic:
              //  Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ParentTransportMapRouteActivity.class));
                break;

//            case R.id.changepwd_parent_imgview:
//                startActivity(new Intent(this,ChangePasswordActivity.class));
//                break;
            default:
                //Enter code in the event that that no cases match
        }
    }

    public void onChangingChild() {
        Log.i("---->>>>>>", "1111111111");
        if (appController.getParentsData() != null) {
            childName = Constants.getChildNameAfterSelecting(0, appController.getParentsData());
            switchChild.childNameTV.setText(childName);
            Constants.SWITCH_CHILD_FLAG = childName;
            Log.i("Setting Default child::", Constants.SWITCH_CHILD_FLAG);
            getChildId = Constants.getChildIdAfterSelecting(0, appController.getParentsData());
            Constants.SET_SWITCH_CHILD_ID = getChildId;
            Log.i("---->>>>>>", "1111111111");
        } else {
            switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
        }
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.my_child));
        topBar.backArrowIV.setImageResource(R.drawable.icon_home);
        topBar.logoutIV.setOnClickListener(this);
        ImageView notification = (ImageView) topBar.findViewById(R.id.notification);
        notification.setVisibility(View.VISIBLE);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentHomeActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        Log.i("Antony", "1111111111");
        switchChild.initSwitchChildBar();
        switchChild.switchChildBT.setOnClickListener(this);
        Log.i("Antony", "222222222222");
//        StorageManager.readString(this, "username", "");
//        switchChild.parentNameTV.setText(childName);
//        switchChild.parentNameTV.setText(StorageManager.readString(this, "username", ""));
    }


    public void setOnClickListener() {

        ImageView homeWork = (ImageView) findViewById(R.id.homework);
        ImageView timeTable = (ImageView) findViewById(R.id.time_table);
        ImageView exams = (ImageView) findViewById(R.id.exams);
        ImageView mailBox = (ImageView) findViewById(R.id.mail_box);
        ImageView calender = (ImageView) findViewById(R.id.calender);
        ImageView attendance = (ImageView) findViewById(R.id.attendance);
        ImageView notice = (ImageView) findViewById(R.id.notice);
        ImageView transport = (ImageView) findViewById(R.id.transport);
        ImageView transport_homeic = (ImageView) findViewById(R.id.transport_homeic);
        //ImageView changePwd = (ImageView) findViewById(R.id.changepwd_parent_imgview);
        homeWork.setOnClickListener(this);
        timeTable.setOnClickListener(this);
        exams.setOnClickListener(this);
        notice.setOnClickListener(this);
        mailBox.setOnClickListener(this);
        calender.setOnClickListener(this);
        attendance.setOnClickListener(this);
        transport.setOnClickListener(this);
        transport_homeic.setOnClickListener(this);
       // changePwd.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
        /*switchChild.switchChildBT.setOnClickListener(this);*/

    }

    public void getParentDetailsWebservicescall() {
        String Url_parent_details = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            //SharedPreferences saredpreferences = this.getSharedPreferences("Response", 0);
            if (!StorageManager.readString(this, "username", "").isEmpty()) {
                Url_parent_details = getString(R.string.base_url) + getString(R.string.parent_url_endpoint) + StorageManager.readString(this, "username", "");
             //   Log.i("===Url_parent===", Url_parent_details);
            }
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest(Url_parent_details);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

}
