package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.bravelocation.yeltzlandnew.tweet.TweetEntity;
import com.bravelocation.yeltzlandnew.tweet.TweetPart;
import com.bravelocation.yeltzlandnew.tweet.Media;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
        tweetTextView.setMovementMethod(LinkMovementMethod.getInstance());
        tweetTextView.setLinkTextColor(ContextCompat.getColor(context, R.color.yeltzBlue));

        String tweetHtml = this.getTweetHtml(tweet);
        Html.ImageGetter imageFetcher = new PicassoImageGetter(tweetTextView, context.getResources());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            tweetTextView.setText(Html.fromHtml(tweetHtml, Html.FROM_HTML_MODE_COMPACT, imageFetcher, null));
        } else {
            tweetTextView.setText(Html.fromHtml(tweetHtml, imageFetcher, null));
        }

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

    private List<TweetPart> textParts(DisplayTweet tweet) {
        // Find all the enities
        ArrayList<TweetEntity> entityParts = new ArrayList<TweetEntity>();

        entityParts.addAll(tweet.getEntities().hashtags);
        entityParts.addAll(tweet.getEntities().urls);
        entityParts.addAll(tweet.getEntities().userMentions);
        entityParts.addAll(tweet.getEntities().symbols);

        if (tweet.getExtendedEntities() != null) {
            entityParts.addAll(tweet.getExtendedEntities().media);
        }

        // Sort the entities in order of first index
        Collections.sort(entityParts, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int x1 = ((TweetEntity) o1).getIndices().get(0);
                int x2 = ((TweetEntity) o2).getIndices().get(0);

                return x1 - x2;
            }
        });

        ArrayList<TweetPart> textParts = new ArrayList<TweetPart>();

        String fullText = tweet.getFullText();
        int currentPoint = 0;
        int endPoint = fullText.length();

        for (int i = 0; i < entityParts.size(); i++) {
            TweetEntity entityPart = entityParts.get(i);

            int entityStart = entityPart.getIndices().get(0);
            int entityEnd = entityPart.getIndices().get(1);

            if (currentPoint <= entityStart) {
                // Add the tweet text up to the entity
                String textUpToEntityStart = fullText.substring(currentPoint, entityStart);
                textParts.add(new TweetPart(textUpToEntityStart, null));

                // Add the display text of the entity
                textParts.add(new TweetPart(entityPart.displayText(), entityPart.linkUrl()));

                // Move the current point past the entity
                currentPoint = entityEnd;
            }
        }

        // Finally add any remaining text
        if (currentPoint < endPoint) {
            String textUpToEntityStart = fullText.substring(currentPoint, endPoint - 1);
            textParts.add(new TweetPart(textUpToEntityStart, null));
        }

        return textParts;
    }

    private String getTweetHtml(DisplayTweet tweet) {
        StringBuilder sb = new StringBuilder();

        // Add the text parts
        List<TweetPart> textParts = this.textParts(tweet);

        for (int i = 0; i < textParts.size(); i++) {
            TweetPart textPart = textParts.get(i);

            if (textPart.highlight()) {
                sb.append("<a href='" + textPart.linkUrl + "'>" + textPart.text + "</a>");
            } else {
                sb.append(textPart.text.replaceAll("\\R", "<br />"));
            }
        }

        // Add the media
        if (tweet.getExtendedEntities() != null) {
            List<Media> media = tweet.getExtendedEntities().media;

            if (media != null) {
                for (int i = 0; i < media.size(); i++) {
                    Media currentMedia = media.get(i);

                    String mediaUrl = currentMedia.smallMediaUrl();
                    if (mediaUrl != null) {
                        sb.append("<br /><p><img src='" + mediaUrl + "' width='100%' /></p>");
                    }
                }
            }
        }


        return sb.toString();
    }

    private class PicassoImageGetter implements Html.ImageGetter {
        private TextView textView = null;
        private Resources resources = null;

        public PicassoImageGetter(TextView target, Resources resources) {
            this.textView = target;
            this.resources = resources;
        }

        @Override
        public Drawable getDrawable(String source) {
            BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
            Picasso.get()
                    .load(source)
                    .placeholder(R.drawable.blank_team)
                    .into(drawable);
            return drawable;
        }

        private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

            protected Drawable drawable;

            @Override
            public void draw(final Canvas canvas) {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            }

            public void setDrawable(Drawable drawable) {
                this.drawable = drawable;
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                drawable.setBounds(0, 0, width, height);
                setBounds(0, 0, width, height);
                if (textView != null) {
                    textView.setText(textView.getText());
                }
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                setDrawable(new BitmapDrawable(resources, bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        }
    }
}
