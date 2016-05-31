package com.bravelocation.yeltzland;

/**
 * Created by John on 26/05/2016.
 */
public class MoreListDataItem {
    public String title;
    public String url;
    public int icon;
    public int iconTint;
    public boolean settingsLink;

    public MoreListDataItem(String title, String url, int icon, int iconTint) {
        this(title, url, icon, iconTint, false);
    }

    public MoreListDataItem(String title, String url, int icon, int iconTint, boolean settingsLink) {
        this.title = title;
        this.url = url;
        this.icon = icon;
        this.iconTint = iconTint;
        this.settingsLink = settingsLink;
    }
}
