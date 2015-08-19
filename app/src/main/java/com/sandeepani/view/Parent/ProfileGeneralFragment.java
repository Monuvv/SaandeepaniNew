package com.sandeepani.view.Parent;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandeepani.model.ParentModel;
import com.sandeepani.sharedPreference.PrefManager;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.pkmmte.view.CircularImageView;

/**
 * Created by Antony on 23-05-2015.
 */
public class ProfileGeneralFragment extends Fragment {
    public static final String TAG = ProfileGeneralFragment.class.getSimpleName();
    CircularImageView circularImageView;
    public ParentModel parentModel = null;
    public AppController appController = null;
    // CommunicationChannel mCommChListner = null;
    private TextView child_idTV, child_nameTV1;
    PrefManager sharedPref;
    Context mContext;
    View view;

    public void ProfileFragmentActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater
                .inflate(R.layout.fragment_profile_general, container, false);
        circularImageView = (CircularImageView) view.findViewById(R.id.circularImageView);
        circularImageView.setImageResource(R.drawable.icon_transport);
        circularImageView.setBorderColor(getResources().getColor(R.color.Darkgreen));
        circularImageView.setBorderWidth(20);
        circularImageView.addShadow();
        child_idTV = (TextView) view.findViewById(R.id.child_idTV);
        child_nameTV1 = (TextView) view.findViewById(R.id.child_nameTV);
        //  Context context = getActivity();
        appController = AppController.getInstance();
        sharedPref = new PrefManager(getActivity());
        Log.e("context---1--- ", getActivity().getClass().getSimpleName());
      //  inboxWebServiceCall(getActivity());
        return view;
    }

  /*  public void inboxWebServiceCall(Activity c) {
        String Url_inbox = null;
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Constants.showProgress(getActivity());
            Url_inbox = getString(R.string.base_url) + getString(R.string.parent_mail);
            Url_inbox = "http://gimmewingsdev.elasticbeanstalk.com/Parent/username/ravi@test.com";
            Log.i("TimetableURL", Url_inbox);
            Log.e("context---2--- ", c.getClass().getSimpleName());
            WebServiceCall call = new WebServiceCall(getActivity());
            Log.e("context---3--- ", c.getClass().getSimpleName());
            call.getCallRequest(Url_inbox);
        } else {
            CommonUtils.getToastMessage(getActivity(), getString(R.string.no_network_connection));
        }
    }*/

    void setReceivedText(String s) {

       /* System.out.print("contextiughiuhuh " + applicationContext.getClass().getSimpleName());
        Log.i("aaaaaaaaaaaa", applicationContext.getClass().getSimpleName());
      *//*  child_idTV= (TextView) view.findViewById(R.id.child_idTV);
        child_nameTV1=(TextView) view.findViewById(R.id.child_nameTV);*//*

        appController = AppController.getInstance();
        System.out.print("contextiughiuhuh " + applicationContext.getClass().getSimpleName());

        CommonUtils.getLogs("Parent Response success" + responseArray);
        Log.i(TAG, responseArray.toString());
        Constants.stopProgress(getActivity());
        //Storing to Shared preference to cache the child list for the parent
        if (responseArray != null) {
            parentModel = ParentHomeJsonParser.getInstance().getParentDetails(responseArray);
            Log.i("####parentModel###", parentModel.getChildList().toString());
            appController.setParentData(parentModel);
            if (parentModel.getNumberOfChildren() >= 0) {
                appController.setSelectedChild(0);
            }
            int selectedChildPosition = 0;
            String s1 = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData()).toString();
            Log.e("asfsadsdsd", s1);
            String s2 = String.valueOf(Constants.getChildIdAfterSelecting(selectedChildPosition, appController.getParentsData
                    ()));
            Log.e("asfsadsdsd", s2);*/

            child_nameTV1.setText(s);
          //  child_idTV.setText("34645");


            // child_idTV.setText(s2);
//            ListOfChildrenPreference manager = new ListOfChildrenPreference(this);
//            manager.SaveChildrenListToPreference(responseArray);
        } /*else {
            Toast.makeText(getActivity(), "No data..", Toast.LENGTH_LONG).show();
        }*/
    }

  /*  //create an interface which will help us to communicate with fragments by help of Activity
    interface CommunicationChannel
    {
        public void setCommunication(String msg);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(activity instanceof CommunicationChannel)
        {
            mCommChListner = (CommunicationChannel)activity;
        }
        else
        {
            throw new ClassCastException();
        }
    }*/
//}
