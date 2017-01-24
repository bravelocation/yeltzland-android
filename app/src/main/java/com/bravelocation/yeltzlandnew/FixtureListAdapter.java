package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

public class  FixtureListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<FixtureListDataItem>> expandableListDetail;
    private Typeface textFont;

    public FixtureListAdapter(Context context, List<String> expandableListTitle,
                           LinkedHashMap<String, List<FixtureListDataItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;

        this.textFont = Typeface.createFromAsset(context.getAssets(), "american_typewriter_regular.ttf");
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FixtureListDataItem fixtureListDataItem = (FixtureListDataItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fixture_list_item, null);
        }

        TextView opponentTextView = (TextView) convertView.findViewById(R.id.opponent);
        opponentTextView.setText(fixtureListDataItem.displayOpponent());
        opponentTextView.setTypeface(this.textFont);

        TextView scoreordateTextView = (TextView) convertView.findViewById(R.id.scoreordate);
        scoreordateTextView.setText(fixtureListDataItem.details());
        scoreordateTextView.setTypeface(this.textFont);

        if (fixtureListDataItem.teamScore != null && fixtureListDataItem.opponentScore != null) {
            if (fixtureListDataItem.teamScore > fixtureListDataItem.opponentScore) {
                opponentTextView.setTextColor(ContextCompat.getColor(context, R.color.matchWin));
                scoreordateTextView.setTextColor(ContextCompat.getColor(context, R.color.matchWin));
            } else if (fixtureListDataItem.teamScore < fixtureListDataItem.opponentScore) {
                opponentTextView.setTextColor(ContextCompat.getColor(context, R.color.matchLose));
                scoreordateTextView.setTextColor(ContextCompat.getColor(context, R.color.matchLose));
            } else {
                opponentTextView.setTextColor(ContextCompat.getColor(context, R.color.matchDraw));
                scoreordateTextView.setTextColor(ContextCompat.getColor(context, R.color.matchDraw));
            }
        } else {
            opponentTextView.setTextColor(ContextCompat.getColor(context, R.color.matchNone));
            scoreordateTextView.setTextColor(ContextCompat.getColor(context, R.color.matchNone));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(this.textFont, Typeface.BOLD);

        String listTitle = (String) getGroup(listPosition);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
