package com.bravelocation.yeltzlandnew;

import android.util.Base64;
import android.util.Log;

import com.bravelocation.yeltzlandnew.tweet.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class TwitterDataProvider {
    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String accountName;
    private int tweetCount;

    private List<Tweet> tweets = new ArrayList<Tweet>();

    TwitterDataProvider(String twitterConsumerKey, String twitterConsumerSecret, String accountName, int tweetCount) {
        this.twitterConsumerKey = twitterConsumerKey;
        this.twitterConsumerSecret = twitterConsumerSecret;
        this.accountName = accountName;
        this.tweetCount = tweetCount;

        this.refreshData(null);
    }

    public List<Tweet> getTweets() {
        return this.tweets;
    }

    public void refreshData(Runnable completion) {
        String userTimelineURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=" +
                this.accountName + "&count=" + Integer.toString(this.tweetCount) +  "&exclude_replies=true&tweet_mode=extended";

        this.getBearerToken(new TweetTokenHandler(this.tweets, userTimelineURL, completion));
    }

    void getBearerToken(TokenHandler handler) {

        // Fetch bearer token on background thread
        OkHttpClient client = new OkHttpClient();

        String authorizationValue = this.getBase64EncodeString();

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url("https://api.twitter.com/oauth2/token")
                .header("Authorization", "Basic " + authorizationValue)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.onCompletion(null);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("TwitterDataProvider", "Unexpected code " + response);
                } else {

                    String result = response.body().string();

                    try {
                        // The response should be JSON including "access_token" field, which we will return
                        JSONObject parsedJson = new JSONObject(result);
                        String token = parsedJson.getString("access_token");
                        handler.onCompletion(token);
                    } catch (Exception e) {
                        Log.d("TwitterDataProvider", "Error parsing JSON " + e);
                        handler.onCompletion(null);
                    }
                }
            }
        });
    }

    private String getBase64EncodeString() {
        try {
            String consumerKeyRFC1738 = URLEncoder.encode(this.twitterConsumerKey, "utf-8");
            String consumerSecretRFC1738 = URLEncoder.encode(this.twitterConsumerSecret, "utf-8");
            String concatenateKeyAndSecret = consumerKeyRFC1738 + ":" + consumerSecretRFC1738;

            byte[] secretAndKeyData = concatenateKeyAndSecret.getBytes("ascii");
            String base64EncodeKeyAndSecret = Base64.encodeToString(secretAndKeyData, Base64.NO_WRAP);

            return base64EncodeKeyAndSecret;
        } catch (Exception e) {
            Log.d("TwitterDataProvider", "Encoding access token: " + e.getMessage());
        }

        return null;
    }

    private class TweetTokenHandler implements TokenHandler {
        private List<Tweet> tweets;
        private String url;
        private Runnable completion;

        TweetTokenHandler(List<Tweet> tweets, String url, Runnable completion) {
            this.tweets = tweets;
            this.url = url;
            this.completion = completion;
        }

        @Override
        public void onCompletion(String token) {
            if (token == null) {
                if (this.completion != null) {
                    this.completion.run();
                }

                return;
            }

            // Fetch bearer token on background thread
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + token)
                    .build();

            client.newCall(request).enqueue(new TweetFetcher(this.tweets, this.completion));
        }
    }

    private class TweetFetcher implements Callback {
        private Runnable completion;
        private List<Tweet> tweets;

        TweetFetcher(List<Tweet> tweets, Runnable completion) {
            this.completion = completion;
            this.tweets = tweets;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (this.completion != null) {
                this.completion.run();
            }

            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful()) {
                Log.d("TwitterDataProvider", "Unexpected code " + response);
            } else {
                try {
                    String result = response.body().string();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                    Gson gson = gsonBuilder.create();

                    Tweet[] parsedTweets = gson.fromJson(result, Tweet[].class);
                    this.tweets.clear();
                    this.tweets.addAll(Arrays.asList(parsedTweets));

                    Log.d("TwitterDataProvider", "Fetched tweet data successfully");
                } catch (Exception e) {
                    Log.d("TwitterDataProvider", "Problem parsing Twitter JSON " + e.getMessage());
                }
            }

            if (this.completion != null) {
                this.completion.run();
            }
        }
    }

    private interface TokenHandler {
        void onCompletion(String token);
    }
}
