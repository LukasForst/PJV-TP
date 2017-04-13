package com.forst.lukas.pibe.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.forst.lukas.pibe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Fragment} which provide simple application filter.
 *
 * @author Lukas Forst
 */
public class AppFilterFragment extends Fragment {
    static private List<String> filteredApps = new ArrayList<>();
    // TODO: 27.3.17
    private final String TAG = this.getClass().getSimpleName();
    private List<String> appNames = new ArrayList<>();

    public AppFilterFragment() {
        // Required empty public constructor
    }

    public static List<String> getFilteredApps() {
        return filteredApps;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView  = inflater.inflate(R.layout.fragment_app_filter, container, false);

        PackageManager pm = inflatedView.getContext().getApplicationContext().getPackageManager();
        Thread t = new Thread(new GetInstalledApplications(pm));
        t.run();

        // TODO: 13/04/17 wrong color bug
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>
                (getContext().getApplicationContext(), android.R.layout.select_dialog_item, appNames);
        //Getting the instance of AutoCompleteTextView
        final AutoCompleteTextView completeTextView =
                (AutoCompleteTextView) inflatedView.findViewById(R.id.fragment_app_filter_autoCompleteText);
        completeTextView.setThreshold(1);//will start working from first character
        completeTextView.setAdapter(autoCompleteAdapter);//setting the adapter data into the AutoCompleteTextView
        autoCompleteAdapter.notifyDataSetChanged();
        completeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTextView.setText("");
            }
        });
        completeTextView.setHintTextColor(Color.BLACK);
        completeTextView.setLinkTextColor(Color.BLACK);

        //listview settings
        final ListView listview = (ListView) inflatedView.findViewById(R.id.fragment_app_filter_listView);
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, filteredApps);
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                filteredApps.remove(item);
                listViewAdapter.notifyDataSetChanged();
            }
        });
        //button settings
        Button addButton = (Button) inflatedView.findViewById(R.id.fragment_app_filter_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedApp = completeTextView.getText().toString();
                if (appNames.contains(selectedApp)) {
                    filteredApps.add(selectedApp);
                    Log.i(TAG, selectedApp);
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(v.getContext(), "Invalid name.", Toast.LENGTH_SHORT).show();
                }
                completeTextView.setText("");
            }
        });



        return inflatedView;
    }

    class GetInstalledApplications implements Runnable {
        PackageManager pm;

        public GetInstalledApplications(PackageManager pm) {
            this.pm = pm;
        }

        @Override
        public void run() {
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            for (ApplicationInfo app : apps) {
                //Log.i("app", pm.getApplicationLabel(app).toString());
                appNames.add(pm.getApplicationLabel(app).toString());
            }
        }
    }
}
