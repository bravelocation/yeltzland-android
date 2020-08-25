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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GameScoreDataPump {
    private static String LOCALFILENAME = "gamescore.json";

    private static FixtureListDataItem latestScore = null;

    public static FixtureListDataItem getLatestScore() {
        return GameScoreDataPump.latestScore;
    }

    public static FixtureListDataItem getNextFixture() {
        List<FixtureListDataItem> nextGames = FixtureListDataPump.getNextFixtures(1);
        if (nextGames.size() > 0) {
            return nextGames.get(0);
        }

        return null;
    }

    public static FixtureListDataItem getLastResult() {
        List<FixtureListDataItem> lastResults = FixtureListDataPump.getLastResults(1);
        if (lastResults.size() > 0) {
            return lastResults.get(0);
        }

        return null;
    }

    public static boolean IsGameScoreForLatestGame() {
        if (GameScoreDataPump.latestScore == null) {
            return false;
        }

        FixtureListDataItem nextGame = GameScoreDataPump.getNextFixture();
        if (nextGame != null && nextGame.equals(GameScoreDataPump.latestScore)) {
            return true;
        }

        return false;
    }

    public static void updateGameScore(Context context, Runnable completion) {
        // Copy bundled matches to cache
        GameScoreDataPump.moveBundleFileToAppDirectory(context);

        // Load data from cached JSON file
        GameScoreDataPump.loadDataFromCachedJson(context, completion);

        // Refresh data from server
        GameScoreDataPump.refreshFixturesFromServer(context, completion);

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
                Log.d("GameScoreDataPump", "Asset file already exists in file cache");
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

            Log.d("GameScoreDataPump", "Asset file copied to file cache");
        } catch (Exception e) {
            Log.e("GameScoreDataPump", "Error copying asset to cache:" + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    // Ignore cleanup error
                }
            }

            if (out != null) {
                try {
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    // Ignore cleanup error
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

            GameScoreDataPump.parseJSON(new String(buffer, "UTF-8"), completion);
        } catch (Exception e) {
            Log.e("GameScoreDataPump", "Error parsing JSON:" + e.toString());
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

    private static boolean parseJSON(String input, Runnable completion) {
        try {
            if (input.length() == 0) {
                return false;
            }

            JSONObject parsedJson = new JSONObject(input);

            JSONObject match = parsedJson.getJSONObject("match");

            // Pulling items from the match
            String matchDateTime = match.getString("MatchDateTime");
            String opponent = match.getString("Opponent");
            String home = match.getString("Home");

            // Pull latest score
            String teamScore = parsedJson.getString("yeltzScore");
            String opponentScore = parsedJson.getString("opponentScore");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.UK);
            Date convertedMatchDate = dateFormat.parse(matchDateTime);

            FixtureListDataItem latestScore = null;
            FixtureListDataItem nextFixture = null;

            if (teamScore == null || teamScore.equals("null") || opponentScore == null || opponentScore.equals("null")) {
                latestScore = new FixtureListDataItem(convertedMatchDate, opponent, home.equals("1"));
            } else {
                latestScore = new FixtureListDataItem(convertedMatchDate, opponent, home.equals("1"), Integer.valueOf(teamScore), Integer.valueOf(opponentScore));
            }

            List<FixtureListDataItem> nextFixtures = FixtureListDataPump.getNextFixtures(1);
            if (nextFixtures.size() > 0) {
                nextFixture = nextFixtures.get(0);
            }

            // Is the game in progress
            if (latestScore != null && nextFixture != null) {
                // If same game
                if (latestScore.equals(nextFixture)) {
                    GameScoreDataPump.latestScore = latestScore;
                } else {
                    // Are we after kickoff?
                    Date now = new Date();
                    if (now.after(nextFixture.fixtureDate)) {
                        // If so, we are in progress with no score yet
                        GameScoreDataPump.latestScore = nextFixture;
                    } else {
                        // Get last result
                        FixtureListDataItem lastResult = FixtureListDataPump.getLastResult();
                        GameScoreDataPump.latestScore = lastResult;
                    }
                }
            }


            Log.d("GameScoreDataPump", "Game score updated");

            if (completion != null) {
                Log.d("GameScoreDataPump", "Running completion after game score update");
                completion.run();
            }

            return true;
        } catch (Exception e) {
            Log.e("GameScoreDataPump", "Error parsing JSON:" + e.toString());
            return false;
        }
    }

    public static void refreshFixturesFromServer(Context context, Runnable completion) {
        // Fetch server data on background thread
        new Thread(new GameScoreDataPump.FetchGameScoreFromServer(context, completion)).start();
    }

    private static class FetchGameScoreFromServer implements Runnable {

        private Context context;
        private Runnable completion;

        public FetchGameScoreFromServer(Context context, Runnable completion) {
            this.context = context;
            this.completion = completion;
        }

        public void run() {
            InputStream in = null;
            FileOutputStream out = null;

            try {
                // Fetch the server matches JSON
                URL url = new URL("https://bravelocation.com/automation/feeds/gamescore.json");
                in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Check it's valid JSON, and if so, replace cache
                if (GameScoreDataPump.parseJSON(result.toString(), completion)) {
                    File cacheFile = new File(context.getExternalFilesDir(null), LOCALFILENAME);
                    out = new FileOutputStream(cacheFile);
                    out.write(result.toString().getBytes());
                    Log.d("GameScoreDataPump", "Written server game score data to cache");
                } else {
                    Log.d("GameScoreDataPump", "No game score found in server data");
                }
            } catch (Exception e) {
                Log.e("GameScoreDataPump", "Problem occurred getting server data: " + e.toString());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException e) {
                        // Ignore cleanup error
                    }
                }
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        // Ignore cleanup error
                    }
                }
            }
        }
    }
}
