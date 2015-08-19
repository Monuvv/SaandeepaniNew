package com.sandeepani.view.Teacher;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.mycalendar.WeekdayArrayAdapter;
import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.StudentsListAdapter;
import com.sandeepani.interfaces.IOnCheckedChangeListener;
import com.sandeepani.model.GradeModel;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.model.TeacherModel;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;

import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.R;
import com.sandeepani.webserviceparser.AttendaceJsonParser;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AttendenceUpdateActivity extends BaseActivity implements View.OnClickListener, IOnCheckedChangeListener, RequestCompletion {

    private TopBar1 topBar;
    private String teacherName = "";
    private ListView studentsListview;
    private InfiniteViewPager viewPager;
    private TextView absetntTV, presentTV, resultTV;
    int currentIndicator = 0;
    private TextView doneTV, editTV;
    private StudentsListAdapter adapter;
    private ArrayList<StudentDTO> studentsList = null;
    private ArrayList<StudentDTO> studentsListabsent = null;
    private TeacherModel teacherModel = null;
    private int studentsSize;
    String mselectedDate = "";
    public static boolean hasattendancedone = false;

    enum RequestType {
        TYPE_GET, GET_POST;
    }

    private RequestType type = RequestType.TYPE_GET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
      //  topBar.logoutIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.attendence_title));
        studentsListview = (ListView) findViewById(R.id.students_listview);
        doneTV = (TextView) findViewById(R.id.done_tv);
        editTV = (TextView) findViewById(R.id.edit);
        doneTV.setOnClickListener(this);
        editTV.setOnClickListener(this);
        presentTV = (TextView) findViewById(R.id.present_tv);
        absetntTV = (TextView) findViewById(R.id.absent_tv);
        resultTV = (TextView) findViewById(R.id.result_tv);
        resultTV.setText("");

        ((TextView) findViewById(R.id.teacher_name_tv)).setText(StorageManager.readString(this, getString(R.string.pref_username), ""));
        String teacherName1 = StorageManager.readString(this, getString(R.string.pref_username), "");
        teacherName = "/" + teacherName1;

        viewPager = (InfiniteViewPager) findViewById(R.id.infinite_viewpager);
        viewPager.setAdapter(new MyInfinitePagerAdapter(0));
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {
            @Override
            public void onPageScrolled(final Object indicator, final float positionOffset, final int positionOffsetPixels) {
                Calendar cal = null;
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (Integer.parseInt(String.valueOf(indicator)) * 7));
                //((TextView)findViewById(R.id.todayDate)).setText(getMonth( cal.get(Calendar.MONTH) +1) +" "+cal.get(Calendar.YEAR) );
            }

            @Override
            public void onPageSelected(final Object indicator) {
                Log.d("InfiniteViewPager", "onPageSelected " + indicator.toString());
                currentIndicator = Integer.parseInt("" + indicator.toString());
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                Log.d("InfiniteViewPager", "state " + String.valueOf(state));
            }
        });

        GridView weekdayGridView = (GridView) findViewById(R.id.weekday_gridview_main);
        weekdayGridView.setVisibility(View.VISIBLE);
        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(this, android.R.layout.simple_list_item_1, getDaysOfWeek());
        weekdayGridView.setAdapter(weekdaysAdapter);
        Calendar cal = Calendar.getInstance();
        ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (CommonUtils.isNetworkAvailable(this)) {
            Log.e("------->>>", sdf.format(cal.getTime()));
            getAttendanceWebService(sdf.format(cal.getTime()));
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.network_error));
        }
    }

    public void getAttendanceWebService(String date) {
        {
            type = RequestType.TYPE_GET;
            if (CommonUtils.isNetworkAvailable(this)) {
                Constants.showProgress(this);
                String postUrl = "/app/attendance/grade/v1/" + "5/a/" + date;
                postUrl = getString(R.string.base_url) + postUrl;
                Log.i("TimetableURL", postUrl);
                WebServiceCall call = new WebServiceCall(this);
                call.getCallRequest(postUrl);
                // httpConnectThread = new HttpConnectThread(this, null, this);
                //     String url = getString(R.string.base_url) + getString(R.string.url_teacher_deatils);
                // teacherName = "/" + StorageManager.readString(this, getString(R.string.pref_username), "");
                // httpConnectThread.execute(postUrl);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;

            case R.id.done_tv:
                if (doneTV.getText().toString().equals(getString(R.string.done_caps))) {
                    CommonUtils.getLogs("DOne");
                    doneClicked();
                } else if (doneTV.getText().toString().equals(getString(R.string.start_caps))) {
                    adapter.selectAll();
                    CommonUtils.getLogs("start");
                } /*else if (doneTV.getText().toString().equals("edit")) {
                    Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
                    View vi=adapter.getView(1,null,studentsListview);
                    vi.setBackgroundColor(getResources().getColor(R.color.Darkgreen));
                }*/
                break;

//            case R.id.logoutIV:
//                SharedPreferences clearSharedPreferenceForLogout;
//                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
//                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
//                editor.clear();
//                editor.commit();
//                finish();
//                startActivity(new Intent(this, LoginActivity.class));

            case R.id.edit:
                editTV.setVisibility(View.GONE);
                doneTV.setVisibility(View.VISIBLE);
                studentsListabsent = teacherModel.getGradeModels().get(0).getAbsentStudentsModels();
                adapter = new StudentsListAdapter(this, R.layout.select_student_list_item, studentsList, studentsListabsent, true, true);
                studentsListview.setAdapter(adapter);
                break;
            default:
        }
    }

    private void doneClicked() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (adapter.mSelectedItemsIds.size() == studentsList.size()) {
                jsonObject.put("present_flag", "P");

            } else {
                jsonObject.put("present_flag", "A");
                JSONArray array = new JSONArray();
                for (StudentDTO dto : studentsList) {
                    if (!adapter.mSelectedItemsIds.get(dto.getStudentId())) {
                        array.put(dto.getStudentId() + "");
                    }
                }
                jsonObject.put("studentList", array);
            }
            GradeModel gradeModel = teacherModel.getGradeModels().get(0);
            jsonObject.put("grade", gradeModel.getGradeName());
            jsonObject.put("date", mselectedDate);//06-04-2015
            jsonObject.put("section", gradeModel.getSection());
            CommonUtils.getLogs("POST Obj : " + jsonObject);
            if (CommonUtils.isNetworkAvailable(this)) {
                type = RequestType.GET_POST;
                //    httpConnectThread = new HttpConnectThread(this, jsonObject, this);
                WebServiceCall call = new WebServiceCall(this);
                call.postToServer(jsonObject, (getString(R.string.base_url) + getString(R.string.url_save_attendence)));
                //httpConnectThread.execute(getString(R.string.base_url) + getString(R.string.url_save_attendence));
            } else {
                CommonUtils.getToastMessage(this, getString(R.string.network_error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkedStateChanged(StudentDTO studentDTO, boolean isChecked) {

        int presnt = adapter.mSelectedItemsIds.size();
        int absent = studentsSize - presnt;
        presentTV.setText(getString(R.string.present_caps) + "  " + presnt);
        absetntTV.setText(getString(R.string.absent_caps) + "  " + absent);
        resultTV.setText(presnt + "/" + studentsSize);
        if (presnt > 0) {
            doneTV.setText(getString(R.string.done_caps));
        } else {
            doneTV.setText(getString(R.string.start_caps));
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Response::::" + responseJson);
        if (responseJson != null && responseArray == null) {
            responseArray = new JSONArray();
            responseArray.put(responseJson);
        }
        JSONObject obj = null;
        if (responseArray != null) {
            switch (type) {
                case TYPE_GET:
                    boolean hasClassList = false;
                    try {
                        JSONObject jsonObject = responseArray.getJSONObject(0);
                        if (jsonObject.has("message")) {
                            hasClassList = jsonObject.getString("message").equals("No Class assigned as Class Teacher");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!hasClassList) {
                        teacherModel = AttendaceJsonParser.getInstance().getTeacherModel(responseArray);
                        studentsList = teacherModel.getGradeModels().get(0).getStudentsModels();
                        studentsSize = studentsList.size();
                        if (AttendenceUpdateActivity.hasattendancedone) {
                            studentsListabsent = teacherModel.getGradeModels().get(0).getAbsentStudentsModels();
                            adapter = new StudentsListAdapter(this, R.layout.select_student_list_item, studentsList, studentsListabsent, true, false);
                            studentsListview.setAdapter(adapter);
                            editTV.setVisibility(View.VISIBLE);
                            doneTV.setVisibility(View.GONE);
                        } else {
                            adapter = new StudentsListAdapter(this, R.layout.select_student_list_item, studentsList);
                            studentsListview.setAdapter(adapter);
                            editTV.setVisibility(View.GONE);
                            doneTV.setVisibility(View.VISIBLE);
                        }
                        Constants.stopProgress(this);
                        try {
                            obj = (JSONObject) responseArray.get(0);

                            if (obj.has("message")) {
                                CommonUtils.getToastMessage(this, obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Constants.stopProgress(this);
                        onBackPressed();
                        Toast.makeText(this, "No Class assigned as Class Teacher", Toast.LENGTH_LONG).show();
                    }
                    break;
                case GET_POST:
                    try {
                        obj = responseJson;
                        if (obj.has("message")) {
                            CommonUtils.getToastMessage(this, obj.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            Constants.stopProgress(this);
            try {
                obj = responseJson;
                obj = responseJson;
                if (obj.has("message")) {
                    CommonUtils.getToastMessage(this, obj.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Response::::" + error);
        Constants.stopProgress(this);
        Constants.showMessage(this, "Retry Again", error);
    }


    class onDateClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
            String selectedDate = tv.getTag().toString();
            ((TextView) findViewById(R.id.text1)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView) findViewById(R.id.text2)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView) findViewById(R.id.text3)).setTextColor(Color.parseColor("#D7D7D7"));
            if (!(((TextView) findViewById(R.id.text4)).getCurrentTextColor() == Color.parseColor("#FF0000")))
                ((TextView) findViewById(R.id.text4)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView) findViewById(R.id.text5)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView) findViewById(R.id.text6)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView) findViewById(R.id.text7)).setTextColor(Color.parseColor("#D7D7D7"));

            if (!(tv.getCurrentTextColor() == Color.parseColor("#FF0000")))
                tv.setTextColor(Color.BLUE);
            Log.e("===selected date", selectedDate);
            mselectedDate = selectedDate;
            getAttendanceWebService(selectedDate);
//			Toast.makeText(ChildrenTimeTableActivity.this, selectedDate,wind Toast.LENGTH_LONG).show();
            //    getChildTimeTabel(selectedDate);
        }
    }

    private class MyInfinitePagerAdapter extends InfinitePagerAdapter<Integer> {
        /**
         * Standard constructor.
         *
         * @param initValue the initial indicator value the ViewPager should start with.
         */

        public MyInfinitePagerAdapter(final Integer initValue) {
            super(initValue);
        }

        @Override
        public ViewGroup instantiateItem(Integer indicator) {
            Log.d("InfiniteViewPager", "instantiating page " + indicator);
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.date_view_pager, null);
//            final TextView text = (TextView) layout.findViewById(R.id.moving_view_x);
//            text.setText(String.format("Page %s", indicator));
//            Log.i("InfiniteViewPager", String.format("textView.text() == %s", text.getText()));
            TextView text1 = (TextView) layout.findViewById(R.id.text1);
            TextView text2 = (TextView) layout.findViewById(R.id.text2);
            TextView text3 = (TextView) layout.findViewById(R.id.text3);
            TextView text4 = (TextView) layout.findViewById(R.id.text4);
            TextView text5 = (TextView) layout.findViewById(R.id.text5);
            TextView text6 = (TextView) layout.findViewById(R.id.text6);
            TextView text7 = (TextView) layout.findViewById(R.id.text7);
            Calendar cal = null;
            if (indicator == 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -3);

            }
            if (indicator < 0) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, (indicator * 7) - 3);
            }
            if (indicator > 0) {
                cal = Calendar.getInstance();
                if (indicator == 1)
                    cal.add(Calendar.DAY_OF_YEAR, 4);
                else
                    cal.add(Calendar.DAY_OF_YEAR, (indicator * 7) - 3);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            text1.setText("" + cal.get(Calendar.DATE));
            text1.setTag(sdf.format(cal.getTime()));
            text1.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text2.setText("" + cal.get(Calendar.DATE));
            text2.setTag(sdf.format(cal.getTime()));
            text2.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text3.setText("" + cal.get(Calendar.DATE));
            text3.setTag(sdf.format(cal.getTime()));
            text3.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text4.setText("" + cal.get(Calendar.DATE));
            text4.setTag(sdf.format(cal.getTime()));
            //text4.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text4.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text5.setText("" + cal.get(Calendar.DATE));
            text5.setTag(sdf.format(cal.getTime()));
            //text5.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text5.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text6.setText("" + cal.get(Calendar.DATE));
            text6.setTag(sdf.format(cal.getTime()));
            //text6.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text6.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text7.setText("" + cal.get(Calendar.DATE));
            text7.setTag(sdf.format(cal.getTime()));
            //text7.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text7.setOnClickListener(new onDateClickListner());

            layout.setTag(indicator);
            if (indicator == 0) {
                text4.setTextColor(Color.parseColor("#FF0000"));
                //				myTypefaceLight = Typeface.createFromAsset(getActivity().getAssets(),
                //						"font/roboto_black.ttf");
                //				text4.setTypeface(myTypefaceLight);
            }
            return layout;
        }

        @Override
        public Integer getNextIndicator() {
            return getCurrentIndicator() + 1;
        }

        @Override
        public Integer getPreviousIndicator() {
            return getCurrentIndicator() - 1;
        }

        @Override
        public String getStringRepresentation(final Integer currentIndicator) {
            return String.valueOf(currentIndicator);
        }

        @Override
        public Integer convertToIndicator(final String representation) {
            return Integer.valueOf(representation);
        }

        public void checkAndChangeSelectedcolor(TextView tv, Date current) {
        }
    }

    protected ArrayList<String> getDaysOfWeek() {
        ArrayList<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -3);
        for (int i = 0; i < 7; i++) {
            list.add(getDay(cal.get(Calendar.DAY_OF_WEEK)));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    public String getDayFull(int dayOfWeek) {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "sunday";
        }
        return weekDay;

    }

    public static String getDay(int dayOfWeek) {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "MON";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "TUE";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "WED";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "THU";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "FRI";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "SAT";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "SUN";
        }
        return weekDay;

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
