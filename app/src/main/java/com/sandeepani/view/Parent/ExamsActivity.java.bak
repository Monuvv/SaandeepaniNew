package com.mychild.view.Parent;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mychild.Networkcall.RequestCompletion;
import com.mychild.Networkcall.WebServiceCall;
import com.mychild.adapters.ExamsListviewAdapter;
import com.mychild.adapters.ExamsTypesListviewAdapter;
import com.mychild.adapters.ResultsListAdapter;
import com.mychild.customView.SwitchChildView;
import com.mychild.interfaces.IOnExamChangedListener;
import com.mychild.interfaces.IOnSwichChildListener;
import com.mychild.model.ExamModel;
import com.mychild.model.ParentModel;
import com.mychild.model.ResultsModel;
import com.mychild.utils.CommonUtils;
import com.mychild.utils.Constants;
import com.mychild.utils.TopBar1;
import com.mychild.view.CommonToApp.BaseActivity;
import com.mychild.view.R;
import com.mychild.volley.AppController;
import com.mychild.webserviceparser.ExamsJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ExamsActivity extends BaseActivity implements View.OnClickListener, RequestCompletion, IOnExamChangedListener, IOnSwichChildListener {

    private SwitchChildView switchChild;
    private ListView examsListView;
    private int selectedChildPosition = 0, selectedExamposition = 0;
    private ImageView examsIV;
    private TextView examTypeTV, dateTV;
    private ArrayList<ExamModel> examsList;
    private ArrayList<ResultsModel> resultList;
    private Dialog examsTypeDialog = null;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
    private TopBar1 topBar;
    String childName;
    int getChildId = 0;
    ResultsListAdapter resultsListAdapter;
    ExamsListviewAdapter examsListviewAdapter;
    private static String tabSelectedFlag = "Exams";
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        setTopBar();
        switchChildBar();
        setOnClickListener();

        Bundle buddle = getIntent().getExtras();
        if (buddle != null) {
            String fromKey = buddle.getString(getString(R.string.key_from));
            if (fromKey.equals(getString(R.string.key_from_parent))) {
                setUpTab();
                switchChild.switchChildBT.setVisibility(View.VISIBLE);
                appController = (AppController) getApplicationContext();
                parentModel = appController.getParentsData();
                switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
                selectedChildPosition = appController.getSelectedChild();
                callExamsWebservice(Constants.SET_SWITCH_CHILD_ID);
            } else if (fromKey.equals(getString(R.string.key_from_teacher))) {
                switchChild.switchChildBT.setVisibility(View.GONE);
                tabHost.setVisibility(View.GONE);
                callExamsWebservice();
            }
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (appController != null) {
            selectedChildPosition = appController.getSelectedChild();
            if (appController.getParentsData() != null) {
                onChangingChild();
            } else {
                switchChild.childNameTV.setText("No Child Selected");
            }
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {

        Log.i("tabSelectedFlag",tabSelectedFlag);
        try {
            if(responseJson.has("exam")){
                if(tabSelectedFlag == "Exams"){
                    JSONArray examsArray = responseJson.getJSONArray("exam");
                    if(examsArray.length() != 0){
                        CommonUtils.getLogs("Response::111" + responseJson);
                        examsList = ExamsJsonParser.getInstance().getExamsList(responseJson);
                        if (examsList != null && examsList.size() > 0) {
                            //selectedExamposition = 0;
                            setExamScheduleListAdapter(examsList.get(selectedExamposition));
                        }

                    }
                    else {
                        examsListView.setAdapter(null);
                        Constants.showMessage(this, "No Exams", "No Exams found...");
                    }
                }
                else {
                    resultList = ExamsJsonParser.getInstance().getResultList(responseJson);

                        if (resultList != null && resultList.size() > 0) {
                            setResultListAdapter(resultList);
                        }
                        else {
                            examsListView.setAdapter(null);
                            Constants.showMessage(this, "No Results", "No Results found...");
                        }
                }


            }else{
                Constants.showMessage(this, "Sorry", "No Exams Data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Constants.stopProgress(this);

    }


    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error is exams response::" + error);
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }

//    @Override
//    public void setAsyncTaskCompletionListener(String object) {
//        CommonUtils.getLogs("Response::::Exams" + object);
//        if (object != null && !object.equals("")) {
//            examsList = ExamsJsonParser.getInstance().getExamsList(object);
//            if (examsList != null && examsList.size() > 0) {
//                selectedExamposition = 0;
//                setExamScheduleListAdapter(examsList.get(selectedExamposition));
//            }
//        }
//    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.exams_iv:
                examsTypeDialog = getExamsDialog(examsList, selectedExamposition);
                break;
            case R.id.cancel_btn:
                examsTypeDialog.dismiss();
                break;
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//
//                break;

            default:
        }
    }

    @Override
    public void onExamChanged(int position, boolean isChecked) {
        if (isChecked) {
//            tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.green));
//            tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(Color.TRANSPARENT);
            examsTypeDialog.dismiss();
            selectedExamposition = position;
            setExamScheduleListAdapter(examsList.get(selectedExamposition));
        }

    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {
//        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.green));
//        tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(Color.TRANSPARENT);
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition,appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition,appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::", Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        callExamsWebservice(getChildId);
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    private void callExamsWebservice() {
        String exmas_url = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            exmas_url = getString(R.string.base_url)+"/app/exams/teacherExamsSchedule";
            CommonUtils.getLogs("URL::" + exmas_url);
            WebServiceCall call = new WebServiceCall(ExamsActivity.this);
            call.getJsonObjectResponse(exmas_url);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void callExamsWebservice(int childID) {
        String exmas_url = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            exmas_url = getString(R.string.base_url) + getString(R.string.url_child_exam)+childID;
            CommonUtils.getLogs("URL::" + exmas_url);
            WebServiceCall call = new WebServiceCall(ExamsActivity.this);
            call.getJsonObjectResponse(exmas_url);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void setExamScheduleListAdapter(ExamModel examModel) {
        tabSelectedFlag = "Exams";
        examTypeTV.setText(examModel.getExamType());
        dateTV.setText("Mar 29 - April 05 2015");
        examsListviewAdapter = new ExamsListviewAdapter(this, R.layout.exams_schedule_list_item, examModel.getExamScheduleList());
//        resultsListAdapter = new ResultsListAdapter(this,R.layout.activity_exam_results_tab, examModel.getChildResultModel());
        examsListView.setAdapter(examsListviewAdapter);

    }

    private void setResultListAdapter(ArrayList<ResultsModel> resultData) {

        resultsListAdapter = new ResultsListAdapter(this,R.layout.activity_exam_results_tab, resultData);
        examsListView.setAdapter(resultsListAdapter);

    }


    public void onChangingChild(){
        if(Constants.SWITCH_CHILD_FLAG == null){
            childName = Constants.getChildNameAfterSelecting(0,appController.getParentsData());
            switchChild.childNameTV.setText(childName);
            Constants.SWITCH_CHILD_FLAG = childName;
            Log.i("Setting Default child::",Constants.SWITCH_CHILD_FLAG);
            getChildId = Constants.getChildIdAfterSelecting(0,appController.getParentsData());
            Constants.SET_SWITCH_CHILD_ID = getChildId;
        }
        else {
            switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
        }
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        topBar.backArrowIV.setOnClickListener(this);
       // topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
//        switchChild.childNameTV.setText(StorageManager.readString(this, getString(R.string.pref_username), ""));

    }

    public void setOnClickListener() {
        tabHost = (TabHost) findViewById(R.id.tabhost2);
        examsListView = (ListView) findViewById(R.id.exams_listview);
        examsIV = (ImageView) findViewById(R.id.exams_iv);
        examTypeTV = (TextView) findViewById(R.id.exam_type_tv);
        dateTV = (TextView) findViewById(R.id.date_tv);
        examsIV.setOnClickListener(this);
        switchChild.switchChildBT.setOnClickListener(this);
    }

    private Dialog getExamsDialog(ArrayList<ExamModel> list, int examPosition) {
        if (list != null && list.size() > 0) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_exams);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
            Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(this);
            //cancelBtn.setOnClickListener();
            ListView examsListview = (ListView) dialog.findViewById(R.id.exams_types_lv);
            ExamsTypesListviewAdapter examsTypesListviewAdapter = new ExamsTypesListviewAdapter(this, R.layout.exam_type_listview_item, list, examPosition);
            examsListview.setAdapter(examsTypesListviewAdapter);
            dialog.show();
            return dialog;
        }
        return null;
    }

  public void setUpTab(){

        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
        class TabDef {
            public final String Indicator;
            public TabDef(String Indicator) {
                this.Indicator = Indicator;
            }
        };
        for(TabDef thisTab : new TabDef[]{
                new TabDef("Exams"),
                new TabDef("Results")
        })
        {
            View indicatorview = android.view.LayoutInflater.from(this).inflate(R.layout.tabs_bg_plain, null);
            TextView tabTitle = (TextView) indicatorview.findViewById(R.id.tabsText);
            tabTitle.setText(thisTab.Indicator);

            tabHost.addTab(tabHost.newTabSpec(thisTab.Indicator).setIndicator(indicatorview)
                    .setContent(new TabHost.TabContentFactory() {
                        public View createTabContent(String tag) {
                            tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.green));
                            return examsListView;
                        }
                    }));

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {

                    if (tabId == "Exams") {
                        tabSelectedFlag = "Exams";
//                        if(tabSelectedFlag == "Exams"){
//                            tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(R.drawable.tab_bg_selected);
//                        }
                        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.green));
                        tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(Color.TRANSPARENT);
                        callExamsWebservice(Constants.SET_SWITCH_CHILD_ID);
//                        examsListView.setAdapter(examsListviewAdapter);
                    } else {
                        tabSelectedFlag = "Results";
                        Toast.makeText(ExamsActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                        tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(getResources().getColor(R.color.green));
                        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(Color.TRANSPARENT);
                        callExamsWebservice(Constants.SET_SWITCH_CHILD_ID);
//                        examsListView.setAdapter(resultsListAdapter);
                    }
                }
            });
        }
    }


}
