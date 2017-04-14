package com.forst.lukas.pibe.fragment;


import android.content.pm.PackageManager;
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
import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.InstalledApplications;

/**
 * {@link Fragment} which provide simple application filter.
 *
 * @author Lukas Forst
 */
public class AppFilterFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private AutoCompleteTextView completeTextView;
    private ArrayAdapter<String> autoCompleteAdapter;

    private ListView filteredAppsListView;
    private ArrayAdapter<String> listViewAdapter;

    private Button addButton;

    public AppFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView  = inflater.inflate(R.layout.fragment_app_filter, container, false);

        PackageManager pm = inflatedView.getContext().getApplicationContext().getPackageManager();
        Thread t = new Thread(new InstalledApplications(pm));
        t.run();

        completeTextView = (AutoCompleteTextView)
                inflatedView.findViewById(R.id.fragment_app_filter_autoCompleteText);
        autoCompleteConfig();

        filteredAppsListView = (ListView)
                inflatedView.findViewById(R.id.fragment_app_filter_listView);
        listViewConfig();

        addButton = (Button) inflatedView.findViewById(R.id.fragment_app_filter_addButton);
        buttonConfig();

        return inflatedView;
    }

    private void buttonConfig() {
        //button settings
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedApp = completeTextView.getText().toString();
                if (PibeData.getInstalledAppsNames().contains(selectedApp)) {
                    PibeData.getFilteredApps().add(selectedApp);
                    Log.i(TAG, selectedApp);
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(v.getContext(), "Invalid name.", Toast.LENGTH_SHORT).show();
                }
                completeTextView.setText("");
            }
        });

    }

    private void autoCompleteConfig() {
        autoCompleteAdapter = new ArrayAdapter<>(
                getContext().getApplicationContext(), android.R.layout.select_dialog_item,
                PibeData.getInstalledAppsNames());

        completeTextView.setThreshold(1);//will start working from first character
        completeTextView.setAdapter(autoCompleteAdapter);
        completeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTextView.setText("");
            }
        });

        autoCompleteAdapter.notifyDataSetChanged();
    }

    private void listViewConfig() {
        listViewAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_list_item_1, PibeData.getFilteredApps());
        filteredAppsListView.setAdapter(listViewAdapter);
        filteredAppsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                PibeData.getFilteredApps().remove(item);
                listViewAdapter.notifyDataSetChanged();
            }
        });

    }
}
