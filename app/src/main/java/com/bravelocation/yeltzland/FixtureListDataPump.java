package com.bravelocation.yeltzland;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by John on 21/06/2016.
 */
public class FixtureListDataPump {
    private static String LOCALFILENAME = "matches.json";

    private static LinkedHashMap<String, List<FixtureListDataItem>> fixtures = new LinkedHashMap<String, List<FixtureListDataItem>>();

    public static LinkedHashMap<String, List<FixtureListDataItem>> getData() {
        return FixtureListDataPump.fixtures;
    }

    public static void updateFixtures(Context context) {
        // Copy bundled matches to cache
        FixtureListDataPump.moveBundleFileToAppDirectory(context);

        // Load data from cached JSON file
        FixtureListDataPump.loadDataFromCachedJson(context);

        // Refresh data from server
        FixtureListDataPump.refreshFixturesFromServer(context);
    }

    private static void moveBundleFileToAppDirectory(Context context) {
        try {
            // Does the file already exist?
            File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
            if (cacheFile.exists()) {
                Log.d("FixtureListDataPump", "Asset file already exists in file cache");
                return;
            }

            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(LOCALFILENAME);
            OutputStream out = new FileOutputStream(cacheFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;
            Log.d("FixtureListDataPump", "Asset file copied to file cache");
        } catch (Exception e) {
            Log.d("FixtureListDataPump", "Error copying asset to cache:" + e.toString());
        }
    }

    private static void loadDataFromCachedJson(Context context) {
        try {
            LinkedHashMap<String, List<FixtureListDataItem>> newFixtures = new LinkedHashMap<String, List<FixtureListDataItem>>();

            // Load the JSON data
            File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
            FileInputStream in = new FileInputStream(cacheFile);

            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();

            JSONObject parsedJson = new JSONObject(new String(buffer, "UTF-8"));
            JSONArray matchesArray = parsedJson.getJSONArray("Matches");

            // Add each fixture into the appropriate month
            List<FixtureListDataItem> monthly = new ArrayList<FixtureListDataItem>();
            for (int i=0; i < matchesArray.length(); i++) {
                JSONObject match = matchesArray.getJSONObject(i);

                // Pulling items from the array
                String matchDateTime = match.getString("MatchDateTime");
                String opponent = match.getString("Opponent");
                String home = match.getString("Home");
                String teamScore = match.getString("TeamScore");
                String opponentScore = match.getString("OpponentScore");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date convertedMatchDate = dateFormat.parse(matchDateTime);

                FixtureListDataItem fixture = null;
                if (teamScore == null || teamScore == "null" || opponentScore == null || opponentScore == "null") {
                    fixture = new FixtureListDataItem(convertedMatchDate, opponent, (home == "1"));
                } else {
                    fixture = new FixtureListDataItem(convertedMatchDate, opponent, (home == "1"), new Integer(teamScore), new Integer(opponentScore));
                }

                if (fixture != null) {
                    // Do we already have a fixture for this month
                    String monthKey = fixture.monthKey();
                    List<FixtureListDataItem> monthFixtures = newFixtures.get(monthKey);

                    if (monthFixtures == null) {
                        monthFixtures = new ArrayList<FixtureListDataItem>();
                        newFixtures.put(monthKey, monthFixtures);
                    }

                    monthFixtures.add(fixture);
                }
            }

            // Reset the data, and add keys in order
            List<String> monthKeys = new LinkedList<String>(newFixtures.keySet());
            Collections.sort(monthKeys);

            FixtureListDataPump.fixtures.clear();
            for(String monthKey: monthKeys) {
                List<FixtureListDataItem> monthFixtures = newFixtures.get(monthKey);
                Collections.sort(monthFixtures);

                FixtureListDataItem firstFixture = monthFixtures.get(0);
                FixtureListDataPump.fixtures.put(firstFixture.fixtureMonth(), monthFixtures);
            }
        } catch (Exception e) {
            Log.d("FixtureListDataPump", "Error parsing JSON:" + e.toString());
        }
    }

    public static void refreshFixturesFromServer(Context context) {
        try {
            URL requestUrl = new URL("http://yeltz.co.uk/fantasyisland/matches.json.php");
        } catch (MalformedURLException e) {

        }

    }
}
