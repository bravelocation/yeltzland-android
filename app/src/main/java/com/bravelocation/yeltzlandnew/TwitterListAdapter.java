package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bravelocation.yeltzlandnew.tweet.DisplayTweet;
import com.bravelocation.yeltzlandnew.tweet.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class TwitterListAdapter extends BaseAdapter {

    private Context context;
    private TwitterDataProvider dataProvider;

    public TwitterListAdapter(Context context, TwitterDataProvider dataProvider) {
        this.context = context;
        this.dataProvider = dataProvider;
    }

    @Override
    public int getCount() {
        return this.dataProvider.getTweets().size();
    }

    @Override
    public Object getItem(int position) {
        return this.dataProvider.getTweets().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = (Tweet) getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.tweet_list_item, null);
        }

        // Set tweet details
        if (tweet.hasRetweet()) {
            this.loadTweetDetailsIntoView(tweet.retweet, convertView);
        } else {
            this.loadTweetDetailsIntoView(tweet, convertView);
        }

        return convertView;
    }

    public void refresh(Runnable completion) {
        this.dataProvider.refreshData(new Runnable() {
            @Override
            public void run() {
                if (completion != null) {
                    completion.run();
                }
            }
        });
    }

    private void loadTweetDetailsIntoView(DisplayTweet tweet, View convertView) {
        // Set tweet details
        TextView tweetTextView = (TextView) convertView.findViewById(R.id.tweet);
        tweetTextView.setText(tweet.getFullText());

        ImageButton userProfileImageButton = (ImageButton) convertView.findViewById(R.id.profile_image_button);
        Picasso.get().load(tweet.getUser().profileImageUrl).placeholder(R.drawable.ic_person).into(userProfileImageButton);

        TextView userNameView = (TextView) convertView.findViewById(R.id.userName);
        userNameView.setText(tweet.getUser().name);
        userNameView.setOnTouchListener(new UserNameTouchHandler(tweet.userTwitterUrl()));

        TextView userScreenNameView = (TextView) convertView.findViewById(R.id.userScreenName);
        userScreenNameView.setText("@" + tweet.getUser().screenName);
        userScreenNameView.setOnTouchListener(new UserNameTouchHandler(tweet.userTwitterUrl()));

        ImageView retweetView = (ImageView) convertView.findViewById(R.id.retweet_image);
        if (tweet.isRetweet()) {
            retweetView.setImageResource(R.drawable.ic_retweet);
        } else {
            retweetView.setImageResource(0);
        }

        userProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweet.userTwitterUrl()));
                    ContextCompat.startActivity(context, browserIntent, null);
                } catch (Exception e) {
                    Log.d("TwitterListAdapter","Couldn't open user profile link");
                }
            }
        });

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm", Locale.UK);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE\nHH:mm", Locale.UK);

        TextView tweetTimeView = (TextView) convertView.findViewById(R.id.tweetTime);

        // Format created date based on whether today
        Date tweetDate = tweet.getCreatedDate();
        if (DateHelper.dayNumber(tweetDate) == DateHelper.dayNumber(new Date())) {
            tweetTimeView.setText("Today\n" + hourFormat.format(tweet.getCreatedDate()));
        } else {
            tweetTimeView.setText(dateFormat.format(tweet.getCreatedDate()));
        }
    }

    private class UserNameTouchHandler implements View.OnTouchListener {
        private String userTwitterUrl;

        UserNameTouchHandler(String userTwitterUrl) {
            this.userTwitterUrl = userTwitterUrl;
        }

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.userTwitterUrl));
            ContextCompat.startActivity(context, browserIntent, null);
            return false;
        }
    }
}
