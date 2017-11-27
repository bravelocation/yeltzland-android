package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by John on 07/07/2016.
 */
public class LocationsDataPump {
    private static String LOCALFILENAME = "locations.json";

    private static final List<LocationDataItem> locations = new ArrayList<LocationDataItem>();

    public static List<LocationDataItem> getData() {
        return LocationsDataPump.locations;
    }

    public static LatLng getCenter() {
        Double smallestLat = 190.0;
        Double largestLat = -190.0;
        Double smallestLong = 190.0;
        Double largestLong = -190.0;

        for (int i = 0; i < locations.size(); i++) {
            LocationDataItem location = locations.get(i);

            if (location.latitude < smallestLat) {
                smallestLat = location.latitude;
            } else if (location.latitude > largestLat) {
                largestLat = location.latitude;
            }

            if (location.longitude < smallestLong) {
                smallestLong = location.longitude;
            } else if (location.longitude > largestLong) {
                largestLong = location.longitude;
            }
        }

        Double centerLat = (smallestLat + largestLat) / 2.0;
        Double centerLong = (smallestLong + largestLong) / 2.0;
        Log.d("LocationsDataPump", "Center:" + centerLat.toString() + "," + centerLong.toString());

        return new LatLng(centerLat, centerLong);
    }

    public static void updateLocations(Context context) {
        // Load data from cached JSON file
        LocationsDataPump.loadDataFromJson(context);
    }

    private static void loadDataFromJson(Context context) {
        InputStream in = null;

        try {
            AssetManager assetManager = context.getAssets();
            in = assetManager.open(LOCALFILENAME);

            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);

            LocationsDataPump.parseJSON(new String(buffer, "UTF-8"));
        } catch (Exception e) {
            Log.d("LocationsDataPump", "Error parsing JSON:" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    // Ignore cleanup error
                }
            }
        }
    }

    private static boolean parseJSON(String input) {
        try {
            if (input.length() == 0) {
                return false;
            }

            JSONObject parsedJson = new JSONObject(input);
            JSONArray locationsArray = parsedJson.getJSONArray("Locations");

            // If no matches, stick with what we've got
            if (locationsArray.length() == 0) {
                return false;
            }

            // Get all the matches into one list
            ArrayList<FixtureListDataItem> matches = new ArrayList<FixtureListDataItem>();

            LinkedHashMap<String, List<FixtureListDataItem>> fixtures = FixtureListDataPump.getData();
            ArrayList<String> months = new ArrayList<String>(fixtures.keySet());

            for (int m = 0; m < months.size(); m++) {
                List<FixtureListDataItem> monthFixtures = fixtures.get(months.get(m));
                matches.addAll(monthFixtures);
            }

            // Clear the existing locations
            synchronized(LocationsDataPump.locations) {
                LocationsDataPump.locations.clear();

                for (int i = 0; i < locationsArray.length(); i++) {
                    JSONObject location = locationsArray.getJSONObject(i);

                    // Pulling items from the array
                    String opponent = location.getString("Team");
                    Double latitude = location.getDouble("Latitude");
                    Double longitude = location.getDouble("Longitude");

                    // Find the match details
                    String description = "";
                    for (int j = 0; j < matches.size(); j++) {
                        FixtureListDataItem match = matches.get(j);

                        if (match.opponent.startsWith(opponent) && match.home == false) {
                            if (description != "") {
                                description = description + ", ";
                            }

                            if (match.teamScore == null || match.opponentScore == null) {
                                description = description + match.fullKickoffTime();
                            } else {
                                description = description + match.score();
                            }
                        }
                    }

                    LocationDataItem locationItem = new LocationDataItem(opponent, latitude, longitude, description);
                    LocationsDataPump.locations.add(locationItem);
                }
            }

            return true;
        } catch (Exception e) {
            Log.d("LocationsDataPump", "Error parsing JSON:" + e.toString());
            return false;
        }
    }
}
