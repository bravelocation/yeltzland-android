package com.bravelocation.yeltzlandnew;

/**
 * Created by John on 07/07/2016.
 */
public class LocationDataItem {
    public String opponent;
    public Double latitude;
    public Double longitude;
    public String description;

    public LocationDataItem(String opponent, Double latitude, Double longitude, String description) {
        this.opponent = opponent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }
}
