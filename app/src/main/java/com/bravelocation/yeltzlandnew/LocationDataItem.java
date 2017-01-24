package com.bravelocation.yeltzlandnew;

/**
 * Created by John on 07/07/2016.
 */
public class LocationDataItem {
    public String opponent;
    public Double latitude;
    public Double longitude;

    public LocationDataItem(String opponent, Double latitude, Double longitude) {
        this.opponent = opponent;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
