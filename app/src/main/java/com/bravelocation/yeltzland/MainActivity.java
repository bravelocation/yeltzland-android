package com.bravelocation.yeltzland;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabChangeListener pageChangeListener = new TabChangeListener(this);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(mViewPager);
        this.changeTabsFont();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Set height
        View appBar = findViewById(R.id.appbar);
        View container = findViewById(R.id.container);

        appBar.getHeight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.webactionbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int currentTab = this.tabLayout.getSelectedTabPosition();

        if (currentTab == 3) {
            // Twitter tab - remove all but reload
            menu.removeItem(R.id.action_home);
            menu.removeItem(R.id.action_back);
            menu.removeItem(R.id.action_forward);
            menu.removeItem(R.id.action_browser);
        }  else if (currentTab == 4) {
            // More tab - remove all
            menu.removeItem(R.id.action_home);
            menu.removeItem(R.id.action_back);
            menu.removeItem(R.id.action_forward);
            menu.removeItem(R.id.action_reload);
            menu.removeItem(R.id.action_browser);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentTab = mViewPager.getCurrentItem();
        Fragment currentFragment = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, currentTab);

        WebPageFragment webPageFragment = null;
        WebView webView = null;
        TwitterFragment twitterFragment = null;

        if (currentTab <= 2) {
            webPageFragment = (WebPageFragment) currentFragment;
            webView = (WebView) webPageFragment.rootView.findViewById(R.id.fragmentWebView);
        } else if (currentTab == 3) {
            twitterFragment = (TwitterFragment) currentFragment;
        }

        switch (item.getItemId()) {
            case R.id.action_home:
                if (webView != null) {
                    webView.loadUrl(webPageFragment.homeUrl);
                }
                return true;
            case R.id.action_back:
                if (webView != null) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                }

                return true;
            case R.id.action_forward:
                if (webView != null) {
                    if (webView.canGoForward()) {
                        webView.goForward();
                    }
                }

                return true;
            case R.id.action_reload:
                if (webView != null) {
                    webView.reload();
                } else if (twitterFragment != null) {
                    twitterFragment.reload();
                }

                return true;
            case R.id.action_browser:
                if (webView != null && webView.getUrl() != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                    startActivity(browserIntent);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeTabsFont() {

        Typeface tabFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "american_typewriter_regular.ttf");

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tabFont, Typeface.NORMAL);
                }
            }
        }
    }

    public class TabChangeListener extends ViewPager.SimpleOnPageChangeListener {

        AppCompatActivity parentActivity;

        public TabChangeListener(AppCompatActivity activity) {
            parentActivity = activity;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // Reset the action options
            parentActivity.invalidateOptionsMenu();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WebPageFragment.newInstance("http://yeltz.co.uk/0/");
                case 1:
                    return WebPageFragment.newInstance("http://www.ht-fc.com");
                case 2:
                    return WebPageFragment.newInstance("https://www.youtube.com/user/HalesowenTownFC");
                case 3:
                    return new TwitterFragment();
                case 4:
                    return new MoreFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.forum_name);
                case 1:
                    return getString(R.string.official_site);
                case 2:
                    return getString(R.string.yeltz_tv);
                case 3:
                    return getString(R.string.twitter);
                case 4:
                    return getString(R.string.more);
            }
            return null;
        }
    }
}
