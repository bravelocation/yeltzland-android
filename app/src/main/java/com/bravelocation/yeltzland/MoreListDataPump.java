package com.bravelocation.yeltzland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MoreListDataPump {
    public static LinkedHashMap<String, List<MoreListDataItem>> getData() {
        LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail = new LinkedHashMap<String, List<MoreListDataItem>>();

        List<MoreListDataItem> others = new ArrayList<MoreListDataItem>();
        others.add(new MoreListDataItem("HTFC on Facebook", "https://www.facebook.com/halesowentownfc/", R.drawable.ic_facebook_square, R.color.facebookBlueOverlay));
        others.add(new MoreListDataItem("NPL site", "http://www.evostikleague.co.uk", R.drawable.ic_soccerball_o, R.color.evostickRedOverlay));
        others.add(new MoreListDataItem("Fantasy Island", "http://yeltz.co.uk/fantasyisland", R.drawable.ic_plane, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Stourbridge Town FC", "", R.drawable.ic_thumbs_down, R.color.stourbridgeRedOverlay));

        List<MoreListDataItem> history = new ArrayList<MoreListDataItem>();
        history.add(new MoreListDataItem("Yeltz Archives", "http://www.yeltzarchives.com", R.drawable.ic_home, R.color.yeltzBlueOverlay));
        history.add(new MoreListDataItem("Yeltzland News Archive", "http://www.yeltzland.net/news.html", R.drawable.ic_newspaper, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> options = new ArrayList<MoreListDataItem>();
        options.add(new MoreListDataItem("Notification Settings", "", R.drawable.ic_cog, R.color.yeltzBlueOverlay, true));

        List<MoreListDataItem> about = new ArrayList<MoreListDataItem>();
        about.add(new MoreListDataItem("Another Brave Location App (v" + BuildConfig.VERSION_NAME + ")", "http://bravelocation.com/apps", R.drawable.ic_mapmarker, R.color.bravelocationRedOverlay));

        expandableListDetail.put("Other Websites", others);
        expandableListDetail.put("Know Your History", history);
        expandableListDetail.put("Options", options);
        expandableListDetail.put("About The App", about);
        return expandableListDetail;
    }
}
