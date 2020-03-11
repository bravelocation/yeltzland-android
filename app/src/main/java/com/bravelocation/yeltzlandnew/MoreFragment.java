package com.bravelocation.yeltzlandnew;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MoreFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListDetail = MoreListDataPump.getData();

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MoreListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                MoreListDataItem selectedItem = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition);

                if (selectedItem.settingsLink) {
                    Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(settingsIntent);
                    return true;
                }

                if (selectedItem.fixturesLink) {
                    Intent settingsIntent = new Intent(getActivity(), FixtureListActivity.class);
                    startActivity(settingsIntent);
                    return true;
                }

                if (selectedItem.latestScoreLink) {
                    Intent settingsIntent = new Intent(getActivity(), LatestScoreActivity.class);
                    startActivity(settingsIntent);
                    return true;
                }

                if (selectedItem.groundsLink) {
                    Intent settingsIntent = new Intent(getActivity(), LocationsMapsActivity.class);
                    startActivity(settingsIntent);
                    return true;
                }

                if (selectedItem.url.length() > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedItem.url));
                    startActivity(browserIntent);
                    return true;
                }

                return false;
            }
        });

        // Expand all sections initially
        int count = expandableListAdapter.getGroupCount();
        for (int position = 1; position <= count; position++) {
            expandableListView.expandGroup(position - 1);
        }

        return view;
    }
}
