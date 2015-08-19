package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
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

import com.kk.mycalendar.CaldroidFragment;
import com.kk.mycalendar.CaldroidListener;
import com.kk.mycalendar.WeekdayArrayAdapter;
import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.ChildTimeTabelAdapter;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseFragmentActivity;
import com.sandeepani.view.CommonToApp.LoginActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.ChildTimeTabelParser;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Vijay on 3/28/15.
 */
public class ChildrenTimeTableActivity extends BaseFragmentActivity implements RequestCompletion, View.OnClickListener, IOnSwichChildListener {
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    private TopBar1 topBar;
    private SwitchChildView switchChild;
    ChildTimeTabelAdapter adapter;
    ListView timeTabelList;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    InfiniteViewPager viewPager;
    int currentIndicator = 0;
    String childName = null;
    int DATE_CHANGED_FLAG=0;
    private String childGrade;
    private String childsection;
    private String timeTabledate;
    String selectedTimeTableDate;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_child_time_tabel);
        setTopBar();
        switchChildBar();

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
        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(
                this, android.R.layout.simple_list_item_1, getDaysOfWeek());
        weekdayGridView.setAdapter(weekdaysAdapter);
        cal = Calendar.getInstance();
        ((TextView) findViewById(R.id.todayDate)).setText(cal.get(Calendar.DAY_OF_MONTH) + " " + getMonth(cal.get(Calendar.MONTH) + 1).substring(0, 3) + " " + cal.get(Calendar.YEAR));

        final CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), 1);
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                //	Toast.makeText(ChildrenTimeTableActivity.this, "Selected date "+date, Toast.LENGTH_LONG).show();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dialogCaldroidFragment.dismiss();
                getChildTimeTabel(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            }
        };
        dialogCaldroidFragment.setCaldroidListener(listener);

        ((View) findViewById(R.id.handleImg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialogCaldroidFragment.show(getSupportFragmentManager(), "myDialog");
            }
        });
        getChildTimeTabel(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedChildPosition = appController.getSelectedChild();
        switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        CommonUtils.getLogs("Timetable Response success");
        Log.i(TAG, responseArray.toString());
        timeTabelList = (ListView) findViewById(R.id.child_time_table_list);
        ArrayList<HashMap<String, String>> childrenTimeTable = ChildTimeTabelParser.getInstance().getChildrenTimeTabel(responseArray);
        adapter = new ChildTimeTabelAdapter(this, childrenTimeTable);
        timeTabelList.setAdapter(adapter);
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
        int id = v.getId();
        switch (id) {
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
            case R.id.logoutIV:
                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
                Constants.logOut(this);

                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                break;

            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.time_tabel));
//        topBar.logoutIV.setOnClickListener(this);
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {

        if(DATE_CHANGED_FLAG == 0){
            getChildTimeTabel(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
        }
        else {
            getChildTimeTabel(selectedTimeTableDate);
        }
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition,appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.childNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public void getChildTimeTabel(String day) {
        String Url_TimeTable = null;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);
            String gradeID = parentModel.getChildList().get(selectedChildPosition).getGrade();
            String section = parentModel.getChildList().get(selectedChildPosition).getSection();
            Url_TimeTable = getString(R.string.base_url) + getString(R.string.timetable_child) +gradeID+ "/"+section+"/" + day;
            Log.i("TimetableURL", Url_TimeTable);
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest(Url_TimeTable);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


    class onDateClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TextView tv = (TextView) v;
            selectedTimeTableDate = tv.getTag().toString();
//			Toast.makeText(ChildrenTimeTableActivity.this, selectedDate, Toast.LENGTH_LONG).show();

            ((TextView)findViewById(R.id.text1)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView)findViewById(R.id.text2)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView)findViewById(R.id.text3)).setTextColor(Color.parseColor("#D7D7D7"));
            if(!(((TextView)findViewById(R.id.text4)).getCurrentTextColor()==Color.parseColor("#FF0000")))
                ((TextView)findViewById(R.id.text4)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView)findViewById(R.id.text5)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView)findViewById(R.id.text6)).setTextColor(Color.parseColor("#D7D7D7"));
            ((TextView)findViewById(R.id.text7)).setTextColor(Color.parseColor("#D7D7D7"));


            if(!(tv.getCurrentTextColor()==Color.parseColor("#FF0000")))
                tv.setTextColor(Color.BLUE);

            getChildTimeTabel(selectedTimeTableDate);
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
            text1.setText("" + cal.get(Calendar.DATE));
            text1.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text1.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text2.setText("" + cal.get(Calendar.DATE));
            text2.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text2.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text3.setText("" + cal.get(Calendar.DATE));
            text3.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text3.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text4.setText("" + cal.get(Calendar.DATE));
            text4.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text4.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text5.setText("" + cal.get(Calendar.DATE));
            text5.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text5.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text6.setText("" + cal.get(Calendar.DATE));
            text6.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
            text6.setOnClickListener(new onDateClickListner());

            cal.add(Calendar.DAY_OF_YEAR, 1);
            text7.setText("" + cal.get(Calendar.DATE));
            text7.setTag(getDayFull(cal.get(Calendar.DAY_OF_WEEK)));
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

