package com.bravelocation.yeltzland;

import android.graphics.Color;

/**
 * Created by John on 26/05/2016.
 */
public class MoreListDataItem {
    public String title;
    public String url;
    public int icon;
    public int iconTint;

    public MoreListDataItem(String title, String url, int icon, int iconTint) {
        this.title = title;
        this.url = url;
        this.icon = icon;
        this.iconTint =  iconTint;
    }
}
