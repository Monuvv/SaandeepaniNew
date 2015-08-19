package com.sandeepani.view.Parent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sandeepani.view.R;

/**
 * Created by Antony on 23-05-2015.
 */
public class ProfileAchievementFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater
                .inflate(R.layout.fragment_profile_achievements, container, false);
        return view;
    }
}
