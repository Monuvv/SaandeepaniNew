package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.customView.SwitchChildView;
import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.model.ParentModel;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.DirectionsJSONParser;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.CommonToApp.LoginActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Antony on 11-06-2015.
 */
public class ParentTransportMapRouteActivity extends FragmentActivity implements RequestCompletion,IOnSwichChildListener, View.OnClickListener {
    private GoogleMap mMap;
    public static double mSourceLatitude = 12.976496, mSourceLongitude = 77.700215;
    public static double mDestinationLatitude = 13.013333, mDestinationLongitude = 77.76556;
    //   private LatLng currentgeo = new LatLng(13.013333, 77.76556);
    private LatLng currentgeo = new LatLng(12.976496, 77.700215);
    Timer timer = new Timer();
    public static final String TAG = ChildrenTimeTableActivity.class.getSimpleName();
    private TopBar topBar;
    private SwitchChildView switchChild;
    private int selectedChildPosition = 0;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private Dialog dialog = null;
    String childName;
    int getChildId = 0;
    //   12.976496, 77.700215
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwitchChildDialogueData();
        setContentView(R.layout.activity_transport_maproute);
        setTopBar();
        switchChildBar();
        setUpMapIfNeeded();
        //  getDirection();
        // getGeoCodeWebservicescall();
        callAsynchronousTask();
    }

    public void setSwitchChildDialogueData() {
        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    @Override
    public void onSwitchChild(int selectedChildPosition) {

        childName = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData());
        getChildId = Constants.getChildIdAfterSelecting(selectedChildPosition, appController.getParentsData());
        switchChild.childNameTV.setText(childName);
        Constants.SWITCH_CHILD_FLAG = childName;
        Log.i("Switching child::",Constants.SWITCH_CHILD_FLAG);
        Constants.SET_SWITCH_CHILD_ID = getChildId;
        this.selectedChildPosition = selectedChildPosition;
        appController.setSelectedChild(selectedChildPosition);
        dialog.dismiss();
        callAsynchronousTask();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            timer.cancel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        //  Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getGeoCodeWebservicescall();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 180000 / 3); //execute in every 50000 ms
    }

    @Override
    protected void onPause() {
        super.onPause();
       /* try {
            timer.cancel();
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void setTopBar() {
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.transport));
        topBar.logoutIV.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchChildBar() {
        switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        switchChild.initSwitchChildBar();
        switchChild.childNameTV.setText("Name");
        switchChild.switchChildBT.setOnClickListener(this);
        switchChild.childNameTV.setOnClickListener(this);
    }

    private void getGeoCodeWebservicescall() {
        if (CommonUtils.isNetworkAvailable(this)) {
            //    Constants.showProgress(this);
            String Url_notice = (getString(R.string.base_url) + getString(R.string.map_getbusgeocode_url)+String.valueOf(Constants.SET_SWITCH_CHILD_ID));
            WebServiceCall call = new WebServiceCall(this);
            Log.e("111111111111111>>", Url_notice);
            call.getCallRequest(Url_notice);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }


    private void setUpMapIfNeeded() {
        // TODO Auto-generated method stub
        if (mMap == null)
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void getDirection() {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask
                .execute("https://maps.googleapis.com/maps/api/directions/json?origin=13.012731,77.578157&destination=13.013333,77.76556"/*"https://maps.googleapis.com/maps/api/directions/json?origin="
                        + mSourceLatitude
                        + ","
                        + mSourceLongitude
                        + "&destination="
                        + mDestinationLatitude
                        + ","
                        + mDestinationLongitude*/);
    }

/*
    [
    {
        "status": "success",
            "message": "route present",
            "location":
        {
            "driverName": "ram",
                "driverPhonenumber": "2324424",
                "intialLatitude": "",
                "intialLongitute": "",
                "lastUpdated": "2015-08-07T17:31:18Z",
                "latitude": "2015-08-07T17:31:18Z",
                "currentLongitute": "12.9789659"
        }
    }
    ]*/


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

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        //    Constants.stopProgress(this);
        Log.e("--map response--->>>", responseArray.toString());
        try {
            if (responseArray != null) {
                JSONObject jsonObject = responseArray.getJSONObject(0);
                if (jsonObject.has("location")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("location");
                    Log.e("jsonObject1--->>", jsonObject1.get("driverName").toString() +
                            "----" + jsonObject1.get("driverPhonenumber").toString() +
                            "----" + jsonObject1.get("currentLongitute").toString());
                    String mDriverName = jsonObject1.get("driverName").toString();
                    String mDriverPhoneNo = " " + jsonObject1.get("driverPhonenumber").toString();
                    String mCurrentLong ="12.976499",mCurrentLatitude="77.700219";

                    if(jsonObject1.has("currentLongitute")){
                        mCurrentLong = jsonObject1.get("currentLongitute").toString();
                    }
                    if(jsonObject1.has("currentlatitude")){
                        mCurrentLatitude = jsonObject1.get("currentlatitude").toString();
                    }
                    showMarkerOfBusGeoCode(mCurrentLatitude,mCurrentLong);
                }
            } else {
                Toast.makeText(this, "No data..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMarkerOfBusGeoCode(String mCurrentLatitude, String mCurrentLong) {
       /* public static double mSourceLatitude = 12.976496, mSourceLongitude = 77.700215;
        public static double mDestinationLatitude = 13.013333, mDestinationLongitude = 77.76556;*/
        try {
            LatLng currentgeo = new LatLng(Double.parseDouble(mCurrentLong), Double.parseDouble(mCurrentLatitude));
            LatLng point = new LatLng(Double.parseDouble(mCurrentLong), Double.parseDouble(mCurrentLatitude));
            Log.e("-------------->>>>", Double.parseDouble(mCurrentLatitude) + "-----" + Double.parseDouble(mCurrentLong));
            mMap.addMarker(new MarkerOptions().position(point).title("Bus Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentgeo, 13f));
            //  callAsynchronousTask();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Error in response::" + error);
        //   Constants.stopProgress(this);
        Constants.showMessage(this, "unable to request. ", error);
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
            case R.id.logoutIV:
                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
                Constants.logOut(this);
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;
            default:
                //Enter code in the event that that no cases match
        }
    }


    // Fetches data from url passed
    class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }

        /**
         * A method to download json data from url
         */
        public String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                // Connecting to url
                urlConnection.connect();
                // Reading data from url
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();

            } catch (Exception e) {
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        /**
         * A class to parse the Google Places in JSON format
         */
        private class ParserTask extends
                AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(
                    String... jsonData) {
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                String distance = "";
                String duration = "";

                try {
                    if (result.size() < 1) {
                        return;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) { // Get distance from the list
                            distance = point.get("distance");
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = point.get("duration");
                            continue;
                        }
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.RED);
                }

                // distanceAndTime.setText("Distance:" + distance + ", Duration:" +
                // duration);

                // Drawing polyline in the Google Map for the i-th route
                ParentTransportMapRouteActivity.this.mMap.addPolyline(lineOptions);
                List<LatLng> polyPoints = lineOptions.getPoints(); // route is
                // instance of
                // PolylineOptions
                LatLngBounds.Builder bc = new LatLngBounds.Builder();
                for (LatLng item : polyPoints) {
                    bc.include(item);
                }
                LatLng point = new LatLng(mSourceLatitude, mSourceLongitude);
                mMap.addMarker(new MarkerOptions().position(point).title("Source"));
                point = currentgeo;
                mMap.addMarker(new MarkerOptions().position(point).title(
                        "Destination"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

            }
        }
    }
}