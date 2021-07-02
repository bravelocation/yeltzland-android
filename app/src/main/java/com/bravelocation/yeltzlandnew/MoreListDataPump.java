package com.bravelocation.yeltzlandnew;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MoreListDataPump {
    public static LinkedHashMap<String, List<MoreListDataItem>> getData() {
        LinkedHashMap<String, List<MoreListDataItem>> expandableListDetail = new LinkedHashMap<String, List<MoreListDataItem>>();

        List<MoreListDataItem> stats = new ArrayList<MoreListDataItem>();
        stats.add(new MoreListDataItem("Fixture List", "", R.drawable.ic_calendar, R.color.yeltzBlueOverlay, false, true, false, false));
        stats.add(new MoreListDataItem("Latest Score", "", R.drawable.ic_clock, R.color.yeltzBlueOverlay, false, false, true, false));
        stats.add(new MoreListDataItem("Where's The Ground?", "", R.drawable.ic_map, R.color.yeltzBlueOverlay, false, false, false, true));
        stats.add(new MoreListDataItem("League Table", "https://www.thenpl.co.uk/match-info/tables?table_id=d-3290340", R.drawable.ic_table, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> others = new ArrayList<MoreListDataItem>();
        others.add(new MoreListDataItem("HTFC on Facebook", "https://www.facebook.com/HalesowenTown1873", R.drawable.ic_facebook_square, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("NPL site", "https://www.thenpl.co.uk", R.drawable.ic_soccerball_o, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Fantasy Island", "https://fantasyisland.yeltz.co.uk", R.drawable.ic_plane, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Stourbridge Town FC", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", R.drawable.ic_thumbs_down, R.color.yeltzBlueOverlay));
        others.add(new MoreListDataItem("Club Shop", "https://www.yeltzclubshop.com", R.drawable.ic_shop, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> history = new ArrayList<MoreListDataItem>();
        history.add(new MoreListDataItem("Follow Your Instinct", "https://www.yeltzland.net/followyourinstinct/", R.drawable.ic_newspaper, R.color.yeltzBlueOverlay));
        history.add(new MoreListDataItem("News Archive (1997-2006)", "https://www.yeltzland.net/news.html", R.drawable.ic_news, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> options = new ArrayList<MoreListDataItem>();
        options.add(new MoreListDataItem("Game time tweets", "", R.drawable.ic_twitter, R.color.yeltzBlueOverlay, true, false, false, false));

        List<MoreListDataItem> more = new ArrayList<MoreListDataItem>();
        more.add(new MoreListDataItem("Yeltzland on Amazon Echo", "https://www.amazon.co.uk/Yeltzland-stuff-about-Halesowen-Town/dp/B01MTJOHBY/", R.drawable.ic_amazon, R.color.yeltzBlueOverlay));
        more.add(new MoreListDataItem("Yeltzland on Google Assistant", "https://assistant.google.com/services/a/uid/000000a862d84885?hl=en-GB", R.drawable.ic_google, R.color.yeltzBlueOverlay));
        more.add(new MoreListDataItem("Add Fixture List to Calendar", "https://yeltzland.net/calendar-instructions", R.drawable.ic_calendar, R.color.yeltzBlueOverlay));

        List<MoreListDataItem> about = new ArrayList<MoreListDataItem>();
        about.add(new MoreListDataItem("Privacy Policy", "https://bravelocation.com/privacy/yeltzland", R.drawable.ic_privacy, R.color.yeltzBlueOverlay));
        about.add(new MoreListDataItem("Icons from icons8.com", "https://icons8.com", R.drawable.ic_icons8, R.color.yeltzBlueOverlay));
        about.add(new MoreListDataItem("More Brave Location Apps", "https://bravelocation.com/apps", R.drawable.ic_mapmarker, R.color.bravelocationRedOverlay));
        about.add(new MoreListDataItem("Version " + BuildConfig.VERSION_NAME , "", 0, R.color.bravelocationRedOverlay));

        expandableListDetail.put("Statistics", stats);
        expandableListDetail.put("Other Websites", others);
        expandableListDetail.put("Know Your History", history);
        expandableListDetail.put("Options", options);
        expandableListDetail.put("More from Yeltzland", more);
        expandableListDetail.put("About", about);
        return expandableListDetail;
    }
}
