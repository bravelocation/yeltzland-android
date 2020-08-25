package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FixtureListDataPump {
    private static String LOCALFILENAME = "matches.json";

    private static final LinkedHashMap<String, List<FixtureListDataItem>> fixtures = new LinkedHashMap<String, List<FixtureListDataItem>>();

    public static LinkedHashMap<String, List<FixtureListDataItem>> getData() {
        return FixtureListDataPump.fixtures;
    }

    public static List<FixtureListDataItem> getAwayMatches() {
        ArrayList<FixtureListDataItem> matches = new ArrayList<FixtureListDataItem>();

        List<FixtureListDataItem> allFixtures = getAllMatches();
        for (int m = 0; m < allFixtures.size(); m++) {
            FixtureListDataItem fixture = allFixtures.get(m);

            if (fixture.home == false) {
                matches.add(fixture);
            }
        }

        return matches;
    }

    public static List<FixtureListDataItem> getLastResults(int maxCount) {
        ArrayList<FixtureListDataItem> matches = new ArrayList<FixtureListDataItem>();

        List<FixtureListDataItem> allFixtures = getAllMatches();

        // Go in reverse order to find the games with scores
        for (int m = allFixtures.size() - 1; m >=0 ; m--) {
            FixtureListDataItem fixture = allFixtures.get(m);

            if (fixture.teamScore != null && fixture.opponentScore != null) {
                matches.add(fixture);
            }

            if (matches.size() >= maxCount) {
                break;
            }
        }

        // Put the matches back in order
        ArrayList<FixtureListDataItem> sorted = new ArrayList<FixtureListDataItem>();
        for (int m = matches.size() - 1; m >=0 ; m--) {
            FixtureListDataItem fixture = matches.get(m);
            sorted.add(fixture);
        }

        return sorted;
    }

    public static List<FixtureListDataItem> getNextFixtures(int maxCount) {
        ArrayList<FixtureListDataItem> matches = new ArrayList<FixtureListDataItem>();

        List<FixtureListDataItem> allFixtures = getAllMatches();
        for (int m = 0; m < allFixtures.size(); m++) {
            FixtureListDataItem fixture = allFixtures.get(m);

            if (fixture.teamScore == null && fixture.opponentScore == null) {
                matches.add(fixture);
            }

            if (matches.size() >= maxCount) {
                break;
            }
        }

        return matches;
    }

    public static FixtureListDataItem getLastGame() {
        List<FixtureListDataItem> allFixtures = getAllMatches();
        if (allFixtures.size() == 0) {
            return null;
        }

        return allFixtures.get(allFixtures.size() - 1);
    }

    public static FixtureListDataItem getLastResult() {
        List<FixtureListDataItem> lastResults = getLastResults(1);
        if (lastResults.size() == 0) {
            return null;
        }

        return lastResults.get(0);
    }

    private static List<FixtureListDataItem> getAllMatches() {
        ArrayList<FixtureListDataItem> matches = new ArrayList<FixtureListDataItem>();

        ArrayList<String> months = new ArrayList<String>(fixtures.keySet());

        for (int m = 0; m < months.size(); m++) {
            List<FixtureListDataItem> monthFixtures = fixtures.get(months.get(m));
            matches.addAll(monthFixtures);
        }

        return matches;
    }

    public static void updateFixtures(Context context, Runnable completion) {
        // Copy bundled matches to cache
        FixtureListDataPump.moveBundleFileToAppDirectory(context);

        // Load data from cached JSON file
        FixtureListDataPump.loadDataFromCachedJson(context, completion);

        // Refresh data from server
        FixtureListDataPump.refreshFixturesFromServer(context, completion);

        // Update timeline on data refreshed
        TimelineManager.getInstance().loadLatestData();
    }

    private static void moveBundleFileToAppDirectory(Context context) {
        InputStream in = null;
        OutputStream out = null;

        try {
            // Does the file already exist?
            File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
            if (cacheFile.exists()) {
                Log.d("FixtureListDataPump", "Asset file already exists in file cache");
                return;
            }

            AssetManager assetManager = context.getAssets();
            in = assetManager.open(LOCALFILENAME);
            out = new FileOutputStream(cacheFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }

            Log.d("FixtureListDataPump", "Asset file copied to file cache");
        } catch (Exception e) {
            Log.e("FixtureListDataPump", "Error copying asset to cache:" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Ignore cleanup error
                } finally {
                    in = null;
                }
            }

            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // Ignore cleanup error
                } finally {
                    out = null;
                }
            }
        }
    }

    private static void loadDataFromCachedJson(Context context, Runnable completion) {
        FileInputStream in = null;
        try {
            // Load the JSON data
            File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
            in = new FileInputStream(cacheFile);

            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);

            FixtureListDataPump.parseJSON(new String(buffer, "UTF-8"), completion);
        } catch (Exception e) {
            Log.e("FixtureListDataPump", "Error parsing JSON:" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Ignore cleanup error
                } finally {
                    in = null;
                }
            }
        }
    }

    private static boolean parseJSON(String input, Runnable completion) {
        try {
            if (input.length() == 0) {
                return false;
            }

            LinkedHashMap<String, List<FixtureListDataItem>> newFixtures = new LinkedHashMap<String, List<FixtureListDataItem>>();

            JSONObject parsedJson = new JSONObject(input);
            JSONArray matchesArray = parsedJson.getJSONArray("Matches");

            // If no matches, stick with what we've got
            if (matchesArray.length() == 0) {
                return false;
            }

            // Add each fixture into the appropriate month
            List<FixtureListDataItem> monthly = new ArrayList<FixtureListDataItem>();
            for (int i = 0; i < matchesArray.length(); i++) {
                JSONObject match = matchesArray.getJSONObject(i);

                // Pulling items from the array
                String matchDateTime = match.getString("MatchDateTime");
                String opponent = match.getString("Opponent");
                String home = match.getString("Home");
                String teamScore = match.getString("TeamScore");
                String opponentScore = match.getString("OpponentScore");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.UK);
                Date convertedMatchDate = dateFormat.parse(matchDateTime);

                FixtureListDataItem fixture = null;
                if (teamScore == null || teamScore.equals("null") || opponentScore == null || opponentScore.equals("null")) {
                    fixture = new FixtureListDataItem(convertedMatchDate, opponent, home.equals("1"));
                } else {
                    fixture = new FixtureListDataItem(convertedMatchDate, opponent, home.equals("1"), Integer.valueOf(teamScore), Integer.valueOf(opponentScore));
                }

                // Do we already have a fixture for this month
                String monthKey = fixture.monthKey();
                List<FixtureListDataItem> monthFixtures = newFixtures.get(monthKey);

                if (monthFixtures == null) {
                    monthFixtures = new ArrayList<FixtureListDataItem>();
                    newFixtures.put(monthKey, monthFixtures);
                }

                monthFixtures.add(fixture);
            }

            // Reset the data, and add keys in order
            List<String> monthKeys = new LinkedList<String>(newFixtures.keySet());
            Collections.sort(monthKeys);

            synchronized(FixtureListDataPump.fixtures) {
                FixtureListDataPump.fixtures.clear();
                for (String monthKey : monthKeys) {
                    List<FixtureListDataItem> monthFixtures = newFixtures.get(monthKey);
                    Collections.sort(monthFixtures);

                    FixtureListDataItem firstFixture = monthFixtures.get(0);
                    FixtureListDataPump.fixtures.put(firstFixture.fixtureMonth(), monthFixtures);
                }
            }

            Log.d("FixtureListDataPump", "Fixtures updated");

            if (completion != null) {
                Log.d("FixtureListDataPump", "Running completion after fixtures update");
                completion.run();
            }

            return true;
        } catch (Exception e) {
            Log.e("FixtureListDataPump", "Error parsing JSON:" + e.toString());
            return false;
        }
    }

    public static void refreshFixturesFromServer(Context context, Runnable completion) {
        // Fetch server data on background thread
        new Thread(new FetchFixturesFromServer(context, completion)).start();
    }

    private static class FetchFixturesFromServer implements Runnable {

        private Context context;
        private Runnable completion;

        public FetchFixturesFromServer(Context context, Runnable completion) {
            this.context = context;
            this.completion = completion;
        }

        public void run() {
            InputStream in = null;
            FileOutputStream out = null;

            try {
                // Fetch the server matches JSON
                URL url = new URL("https://bravelocation.com/automation/feeds/matches.json");
                in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Check it's valid JSON, and if so, replace cache
                if (FixtureListDataPump.parseJSON(result.toString(), completion)) {
                    File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
                    out = new FileOutputStream(cacheFile);
                    out.write(result.toString().getBytes());
                    Log.d("FixtureListDataPump", "Written server matches data to cache");
                } else {
                    Log.d("FixtureListDataPump", "No matches found in server data");
                }
            } catch (Exception e) {
                Log.e("FixtureListDataPump", "Problem occurred getting server data: " + e.toString());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // Ignore cleanup error
                    } finally {
                        in = null;
                    }
                }
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        // Ignore cleanup error
                    } finally {
                        out = null;
                    }
                }
            }
        }
    }
}
