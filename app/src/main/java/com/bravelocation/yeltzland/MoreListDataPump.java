package com.bravelocation.yeltzland;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoreListDataPump {
    public static HashMap<String, List<MoreListDataItem>> getData() {
        HashMap<String, List<MoreListDataItem>> expandableListDetail = new HashMap<String, List<MoreListDataItem>>();

        List<MoreListDataItem> others = new ArrayList<MoreListDataItem>();
        others.add(new MoreListDataItem("HTFC on Facebook", "https://www.facebook.com/halesowentownfc/", R.drawable.ic_facebook_square, R.color.facebookBlueOverlay));
        others.add(new MoreListDataItem("NPL site", "http://www.evostikleague.co.uk", R.drawable.ic_soccerball_o, R.color.evostickRedOverlay));
        others.add(new MoreListDataItem("Fantasy Island", "http://yeltz.co.uk/fantasyisland", R.drawable.ic_plane, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Stourbridge Town FC", "", R.drawable.ic_thumbs_down, R.color.stourbridgeRedOverlay));

        List<MoreListDataItem> history = new ArrayList<MoreListDataItem>();
        history.add(new MoreListDataItem("Yeltz Archives", "http://www.yeltzarchives.com", R.drawable.ic_home, R.color.yeltzBlueOverlay));
        history.add(new MoreListDataItem("Yeltzland News Archive", "http://www.yeltzland.net/news.html", R.drawable.ic_newspaper, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> about = new ArrayList<MoreListDataItem>();
        about.add(new MoreListDataItem("Another Brave Location App!", "http://bravelocation.com/apps", R.drawable.ic_mapmarker, R.color.bravelocationRedOverlay));
        about.add(new MoreListDataItem("v" + BuildConfig.VERSION_NAME, "", 0, 0));

        expandableListDetail.put("Other Websites", others);
        expandableListDetail.put("Know Your History", history);
        expandableListDetail.put("About The App", about);
        return expandableListDetail;
    }
}
