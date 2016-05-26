package com.bravelocation.yeltzland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoreListDataPump {
    public static HashMap<String, List<MoreListDataItem>> getData() {
        HashMap<String, List<MoreListDataItem>> expandableListDetail = new HashMap<String, List<MoreListDataItem>>();

        List<MoreListDataItem> others = new ArrayList<MoreListDataItem>();
        others.add(new MoreListDataItem("HTFC on Facebook", "https://www.facebook.com/halesowentownfc/", R.drawable.htfc_logo));
        others.add(new MoreListDataItem("NPL site", "http://www.evostikleague.co.uk", R.drawable.htfc_logo));
        others.add(new MoreListDataItem("Fantasy Island", "http://yeltz.co.uk/fantasyisland", R.drawable.htfc_logo));
        others.add(new MoreListDataItem("Stourbridge Town FC", "", R.drawable.htfc_logo));

        List<MoreListDataItem> history = new ArrayList<MoreListDataItem>();
        history.add(new MoreListDataItem("Yeltz Archives", "http://www.yeltzarchives.com", R.drawable.htfc_logo));
        history.add(new MoreListDataItem("Yeltzland News Archive", "http://www.yeltzland.net/news.html", R.drawable.htfc_logo));

        List<MoreListDataItem> about = new ArrayList<MoreListDataItem>();
        about.add(new MoreListDataItem("Another Brave Location App!", "http://bravelocation.com/apps", R.drawable.htfc_logo));

        expandableListDetail.put("Other Websites", others);
        expandableListDetail.put("Know Your History", history);
        expandableListDetail.put("About The App", about);
        return expandableListDetail;
    }
}
