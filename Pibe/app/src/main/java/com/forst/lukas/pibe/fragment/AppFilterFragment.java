package com.forst.lukas.pibe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forst.lukas.pibe.R;

/**
 * {@link Fragment} which provide simple application filter.
 */
public class AppFilterFragment extends Fragment {
    // TODO: 27.3.17

    public AppFilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView  = inflater.inflate(R.layout.fragment_app_filter, container, false);

        return inflatedView;
    }

}
