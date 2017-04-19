package com.forst.lukas.pibe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forst.lukas.pibe.R;

/**
 * {@link Fragment} with main screen displayed after onCreate()
 * @author Lukas Forst
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: 19/04/17 show information about PHONE_STATE_PERMISSION
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
