package com.bravelocation.yeltzlandnew;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MoreListDataPump {
    public static LinkedHashMap<String, List<MoreListDataItem>> getData() {
        LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail = new LinkedHashMap<String, List<MoreListDataItem>>();

        List<MoreListDataItem> stats = new ArrayList<MoreListDataItem>();
        stats.add(new MoreListDataItem("Fixture List", "", R.drawable.ic_calendar, R.color.yeltzBlueOverlay, false, true, false));
        stats.add(new MoreListDataItem("Where's The Ground?", "", R.drawable.ic_soccerball_o, R.color.yeltzBlueOverlay, false, false, true));
        stats.add(new MoreListDataItem("League Table", "http://www.evostikleague.co.uk/match-info/tables", R.drawable.ic_table, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> others = new ArrayList<MoreListDataItem>();
        others.add(new MoreListDataItem("HTFC on Facebook", "https://www.facebook.com/n/?halesowentownfc", R.drawable.ic_facebook_square, R.color.facebookBlueOverlay));
        others.add(new MoreListDataItem("NPL site", "http://www.evostikleague.co.uk", R.drawable.ic_soccerball_o, R.color.evostickRedOverlay));
        others.add(new MoreListDataItem("Fantasy Island", "http://yeltz.co.uk/fantasyisland", R.drawable.ic_plane, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Stourbridge Town FC", "", R.drawable.ic_thumbs_down, R.color.stourbridgeRedOverlay));

        List<MoreListDataItem> history = new ArrayList<MoreListDataItem>();
        history.add(new MoreListDataItem("Follow Your Instinct", "http://www.yeltzland.net/followyourinstinct/", R.drawable.ic_newspaper, R.color.yeltzBlueOverlay));
        history.add(new MoreListDataItem("Yeltzland News Archive", "http://www.yeltzland.net/news.html", R.drawable.ic_archive, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> options = new ArrayList<MoreListDataItem>();
        options.add(new MoreListDataItem("Notification Settings", "", R.drawable.ic_cog, R.color.yeltzBlueOverlay, true, false, false));

        List<MoreListDataItem> about = new ArrayList<MoreListDataItem>();
        about.add(new MoreListDataItem("Yeltzland on Amazon Echo", "https://www.amazon.co.uk/Yeltzland-stuff-about-Halesowen-Town/dp/B01MTJOHBY/", R.drawable.ic_amazon, R.color.yeltzBlueOverlay));
        about.add(new MoreListDataItem("More Brave Location Apps", "https://bravelocation.com/apps", R.drawable.ic_mapmarker, R.color.bravelocationRedOverlay));
        about.add(new MoreListDataItem("Version " + BuildConfig.VERSION_NAME , "", 0, R.color.bravelocationRedOverlay));

        expandableListDetail.put("Statistics", stats);
        expandableListDetail.put("Other Websites", others);
        expandableListDetail.put("Know Your History", history);
        expandableListDetail.put("Options", options);
        expandableListDetail.put("About", about);
        return expandableListDetail;
    }
}
