package com.bravelocation.yeltzland;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import android.widget.TextView;

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

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(getString(R.string.forum_name));
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

        if (currentTab <= 2) {
            webPageFragment = (WebPageFragment) currentFragment;
            webView = (WebView) webPageFragment.rootView.findViewById(R.id.fragmentWebView);
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
                }
                // TODO: Implement the real Twitter reload

                return true;
            case R.id.action_browser:
                if (webView != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                    startActivity(browserIntent);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
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

            String title = getString(R.string.app_name);

            switch (position) {
                case 0:
                    title =  getString(R.string.forum_name);
                    break;
                case 1:
                    title = getString(R.string.official_site);
                    break;
                case 2:
                    title = getString(R.string.yeltz_tv);
                    break;
                case 3:
                    title = getString(R.string.twitter);
                    break;
                case 4:
                    title = getString(R.string.more);
                    break;
            }

            // Set title for new tab
            ActionBar actionBar = parentActivity.getSupportActionBar();
            actionBar.setTitle(title);

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
            String pageUrl = "http://yeltz.co.uk/0/";

            switch (position) {
                case 0:
                    pageUrl = "http://yeltz.co.uk/0/";
                    break;
                case 1:
                    pageUrl = "http://www.ht-fc.com";
                    break;
                case 2:
                    pageUrl = "https://www.youtube.com/user/HalesowenTownFC";
                    break;
                case 3:
                    pageUrl = "http://twitter.com/halesowentownfc";
                    break;
                case 4:
                    pageUrl = "http://google.com";
                    break;
            }

            return WebPageFragment.newInstance(pageUrl);
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
