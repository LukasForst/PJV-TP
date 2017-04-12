package com.forst.lukas.pibe.fragment;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.forst.lukas.pibe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link Fragment} which provide simple application filter.
 *
 * @author Lukas Forst
 */
public class AppFilterFragment extends Fragment {
    // TODO: 27.3.17
    List<String> app_names = new ArrayList<>();
    List<String> filtered = new ArrayList<>();

    public AppFilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_app_filter, container, false);

        PackageManager pm = inflatedView.getContext().getApplicationContext().getPackageManager();
        Thread t = new Thread(new GetInstaledApps(pm));
        t.run();

        final ListView listView = (ListView) inflatedView.findViewById(R.id.fragment_app_filter_listview);
        final StableArrayAdapter adapter = new StableArrayAdapter(
                inflatedView.getContext().getApplicationContext(),
                android.R.layout.simple_list_item_single_choice,
                app_names);


        // TODO: 12/04/17 it's not possible to get item via position
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //view.setSelected(true);
                Log.i("clicked", item);

                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    checkedTextView.setChecked(false);
                    filtered.remove(item);
                } else {
                    checkedTextView.setChecked(true);
                    filtered.add(item);
                    for (String s : filtered) {
                        Log.i("Filtered", s);
                    }
                }
            }

        });

        // TODO: 12/04/17 resolve loading page again
        for (int i = 0; i < adapter.getCount(); i++) {
            String it = adapter.getItem(i);
            View v;
            for (String s : filtered) {
                if (it.equals(s)) {
                    Log.i("Checked", "NOW");
                    v = listView.getAdapter().getView(i, null, null);
                    //v = listView.getChildAt(i).getRootView();
                    listView.setItemChecked(i, true);

                    CheckedTextView checkedTextView = (CheckedTextView) v;
                    checkedTextView.setChecked(true);
                    break;
                }
            }
        }

        return inflatedView;
    }


    class GetInstaledApps implements Runnable {
        PackageManager pm;

        public GetInstaledApps(PackageManager pm) {
            this.pm = pm;
        }

        @Override
        public void run() {
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            for (ApplicationInfo app : apps) {
                //Log.i("app", pm.getApplicationLabel(app).toString());
                app_names.add(pm.getApplicationLabel(app).toString());
            }
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
