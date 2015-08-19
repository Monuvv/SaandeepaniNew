package com.sandeepani.view.Parent;
/*
    Example of how to apply custom styles to tab indicators. Android styling/theming
    does seem to lead to a proliferation of XML files. Explanation of ones in this
    project:

        res/layout/tabs_bg_plain.xml
            -- plain tab indicator with no special styles, for comparison.
        res/layout/tabs_bg_styled.xml
            -- defines layout of tab indicator, using following drawables.
        res/drawable/tab_bg_selector.xml
            -- defines various states of background of tab indicator, selecting
               from among the following two tab_bg_xxx ones depending on state:
        res/drawable/tab_bg_selected.xml
            -- background of tab indicator in “selected” state.
        res/drawable/tab_bg_unselected.xml
            -- background of tab indicator in “unselected” state.
        res/drawable/tab_text_selector.xml
            -- defines various states of tab indicator text.

    Copyright (c) 2010 Josh Clemm
    Copyright 2012 Lawrence D'Oliveiro <ldo@geek-central.gen.nz>

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.ExamsListviewAdapter;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.model.ExamModel;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.ExamsJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomTabActivity extends FragmentActivity implements View.OnClickListener,RequestCompletion {
    private SwitchChildView switchChild;
    private ListView examsListView;
    private int selectedChildPosition = 0, selectedExamposition = 0;
    private ImageView examsIV;
    private TextView examTypeTV, dateTV;
    private ArrayList<ExamModel> examsList;
    private Dialog examsTypeDialog = null;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
    private TopBar topBar;
    String childName;
    int getChildId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTopBar();
        switchChildBar();
        setOnClickListener();
        setUpTab();

        callExamsWebservice(Constants.SET_SWITCH_CHILD_ID);
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.exams_title));
        topBar.backArrowIV.setOnClickListener(this);
      //  topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
//        switchChild.childNameTV.setText(StorageManager.readString(this, getString(R.string.pref_username), ""));

    }

    public void setOnClickListener() {

        examsListView = (ListView) findViewById(R.id.exams_listview);
        examsIV = (ImageView) findViewById(R.id.exams_iv);
        examTypeTV = (TextView) findViewById(R.id.exam_type_tv);
        dateTV = (TextView) findViewById(R.id.date_tv);
        examsIV.setOnClickListener(this);
        switchChild.switchChildBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("Response::111" + responseJson);
        examsList = ExamsJsonParser.getInstance().getExamsList(responseJson);
        if (examsList != null && examsList.size() > 0) {
            selectedExamposition = 0;
            setExamScheduleListAdapter(examsList.get(selectedExamposition));
        }
        Constants.stopProgress(this);

    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error is exams response::" + error);
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);
    }



    private void callExamsWebservice(int childID) {
        String exmas_url = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            exmas_url = getString(R.string.base_url) + getString(R.string.url_child_exam)+childID;
            CommonUtils.getLogs("URL::" + exmas_url);
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(exmas_url);
//            httpConnectThread = new HttpConnectThread(this, null, this);
//            httpConnectThread.execute(exmas_url);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void setExamScheduleListAdapter(ExamModel examModel) {
        examTypeTV.setText(examModel.getExamType());
        dateTV.setText("Mar 29 - April 05 2015");
        ExamsListviewAdapter examsListviewAdapter = new ExamsListviewAdapter(this, R.layout.exams_schedule_list_item, examModel.getExamScheduleList());
        examsListView.setAdapter(examsListviewAdapter);
    }


    public void setUpTab(){
        final TabHost tabHost = (TabHost) findViewById(R.id.tabhost2);
        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
        class TabDef {
            public final String Indicator;//, Content;

            public TabDef(String Indicator, ListView lv) {
                this.Indicator = Indicator;
                //this.Content = Content;

            } /*TabDef*/
        }; /*TabDef*/
        for(TabDef thisTab : new TabDef[]{
                new TabDef("Exams", examsListView),
                new TabDef("Results", examsListView)
        })
        {


//            final TextView contentview = new TextView(this);
//            contentview.setText(thisTab.Content);


            View indicatorview = android.view.LayoutInflater.from(this).inflate(R.layout.tabs_bg_plain, null);
            TextView tabTitle = (TextView) indicatorview.findViewById(R.id.tabsText);
            tabTitle.setText(thisTab.Indicator);




            tabHost.addTab(tabHost.newTabSpec(thisTab.Indicator).setIndicator(indicatorview)
                    .setContent(new TabHost.TabContentFactory() {
                        public View createTabContent(String tag) {
                            Log.i("Tagggggg:", tag);

                            if(tag == "Exams"){
                                return examsListView;
                            }
                            else {
                                return examsListView;
                            }
                        } /*createTabContent*/
                    }));/*TabContentFactory*/

        } /*for*/
    }

}





















//
//      @Override
//    public void onCreate()
//
//      {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
////        for (boolean CustomStyle : new boolean[]{true, false})
////          {
//            final TabHost ThisTabHost = (TabHost)findViewById(R.id.tabhost2);
//            ThisTabHost.setup();
//            ThisTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
//            class TabDef
//              {
//                public final String Indicator, Content;
//                public TabDef(String Indicator,String Content)
//                  {
//                    this.Indicator = Indicator;
//                    this.Content = Content;
//                  } /*TabDef*/
//              } /*TabDef*/;
//            for
//              (TabDef ThisTab :new TabDef[]
//                      {
//                        new TabDef("Tab 1", "Content 1"),
//                        new TabDef("Tab 2", "Content 2"),
//                        new TabDef("Tab 3", "Content 3"),
//                      })
//              {
//                final TextView contentview = new TextView(this);
//                contentview.setText(ThisTab.Content);
//                View indicatorview = android.view.LayoutInflater.from(this).inflate(R.layout.tabs_bg_plain,null);
//                ((TextView)indicatorview.findViewById(R.id.tabsText)).setText(ThisTab.Indicator);
//                ThisTabHost.addTab
//                  (
//                    ThisTabHost.newTabSpec(ThisTab.Indicator)
//                        .setIndicator(indicatorview)
//                        .setContent
//                          (
//                            new TabHost.TabContentFactory()
//                              {
//                                public View createTabContent(String tag)
//                                  {
//                                    return contentview;
//                                  } /*createTabContent*/
//                              } /*TabContentFactory*/
//                          )
//                  );
//              } /*for*/
////          } /*for*/
//      } /*onCreate*/
//  } /*CustomTabActivity*/
