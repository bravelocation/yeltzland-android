package com.bravelocation.yeltzlandnew;

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
    public boolean latestScoreLink;
    public boolean groundsLink;

    public MoreListDataItem(String title, String url, int icon, int iconTint) {
        this(title, url, icon, iconTint, false, false, false, false);
    }

    public MoreListDataItem(String title, String url, int icon, int iconTint, boolean settingsLink, boolean fixturesLink, boolean latestScoreLink, boolean groundsLink) {
        this.title = title;
        this.url = url;
        this.icon = icon;
        this.iconTint = iconTint;
        this.settingsLink = settingsLink;
        this.fixturesLink = fixturesLink;
        this.latestScoreLink = latestScoreLink;
        this.groundsLink = groundsLink;
    }
}
