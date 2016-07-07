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
    public boolean fixturesLink;
    public boolean groundsLink;

    public MoreListDataItem(String title, String url, int icon, int iconTint) {
        this(title, url, icon, iconTint, false, false, false);
    }

    public MoreListDataItem(String title, String url, int icon, int iconTint, boolean settingsLink, boolean fixturesLink, boolean groundsLink) {
        this.title = title;
        this.url = url;
        this.icon = icon;
        this.iconTint = iconTint;
        this.settingsLink = settingsLink;
        this.fixturesLink = fixturesLink;
        this.groundsLink = groundsLink;
    }
}
