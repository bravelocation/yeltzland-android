package com.bravelocation.yeltzland;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by John on 21/06/2016.
 */
public class FixtureListDataPump {
    public static LinkedHashMap<String, List<FixtureListDataItem>> getData() {
        LinkedHashMap<String, List<FixtureListDataItem>> expandableListDetail = new LinkedHashMap<String, List<FixtureListDataItem>>();

        List<FixtureListDataItem> august = new ArrayList<FixtureListDataItem>();
        august.add(new FixtureListDataItem(new Date(2015, 7, 6, 15, 0), "Tipton Town", false, 1, 0));
        august.add(new FixtureListDataItem(new Date(2015, 7, 13, 15, 0), "Stourbridge Town", true, 3, 0));
        august.add(new FixtureListDataItem(new Date(2015, 7, 17, 19, 45), "Manchester Utd.", true, 1, 1));
        august.add(new FixtureListDataItem(new Date(2015, 7, 20, 15, 0), "Newcastle Utd.", false, 0, 1));

        List<FixtureListDataItem> september = new ArrayList<FixtureListDataItem>();
        september.add(new FixtureListDataItem(new Date(2015, 8, 15, 19, 45), "Lye Town", true, 10, 0));
        september.add(new FixtureListDataItem(new Date(2015, 8, 22, 19, 45), "Kidderminster Harriers", true));

        expandableListDetail.put("August", august);
        expandableListDetail.put("September", september);
        return expandableListDetail;
    }
}
