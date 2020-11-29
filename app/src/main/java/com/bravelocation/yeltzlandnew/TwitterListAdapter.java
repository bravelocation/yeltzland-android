package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bravelocation.yeltzlandnew.tweet.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        TextView tweetTextView = (TextView) convertView.findViewById(R.id.tweet);
        tweetTextView.setText(tweet.fullText);

        ImageButton userProfileImageButton = (ImageButton) convertView.findViewById(R.id.profile_image_button);
        Picasso.get().load(tweet.user.profileImageUrl).placeholder(R.drawable.ic_person).into(userProfileImageButton); // TODO: Get a placeholder

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
}
