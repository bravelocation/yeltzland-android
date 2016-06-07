package com.bravelocation.yeltzland;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

public class MoreListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail;
    private Typeface textFont;

    public MoreListAdapter(Context context, List<String> expandableListTitle,
                           LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail) {
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
        final MoreListDataItem moreListDataItem = (MoreListDataItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(moreListDataItem.title);
        expandedListTextView.setTypeface(this.textFont);

        if (moreListDataItem.icon != 0) {
            ImageView expandedListImageView = (ImageView) convertView.findViewById(R.id.expandedListItemImage);
            expandedListImageView.setImageResource(moreListDataItem.icon);
            expandedListImageView.setColorFilter(ContextCompat.getColor(context, moreListDataItem.iconTint), PorterDuff.Mode.MULTIPLY);
        }

        ImageView pointerImageView = (ImageView) convertView.findViewById(R.id.pointer);
        if (moreListDataItem.url.length() > 0 || moreListDataItem.icon == R.drawable.ic_thumbs_down) {
            pointerImageView.setImageResource(R.drawable.ic_angle_right);
            pointerImageView.setColorFilter(ContextCompat.getColor(context, R.color.yeltzLightBlueOverlay), PorterDuff.Mode.MULTIPLY);
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
