package com.bravelocation.yeltzlandnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LAST_TAB_PREF_FILE = "LastTabPrefFile";
    private static final String LAST_TAB_PREF_NAME = "lastSelectedTab";

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager tabViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        this.sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        this.tabViewPager = (ViewPager) findViewById(R.id.container);
        this.tabViewPager.setAdapter(sectionsPagerAdapter);

        TabChangeListener pageChangeListener = new TabChangeListener(this);
        this.tabViewPager.addOnPageChangeListener(pageChangeListener);

        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.tabViewPager);
        this.changeTabsFont();

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Set tab to last shown tab
        SharedPreferences settings = getSharedPreferences(LAST_TAB_PREF_FILE, 0);
        Integer lastTab = settings.getInt(LAST_TAB_PREF_NAME, 0);
        this.tabViewPager.setCurrentItem(lastTab);
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
            return true;
        }  else if (currentTab == 4) {
            // More tab - remove all
            menu.removeItem(R.id.action_home);
            menu.removeItem(R.id.action_back);
            menu.removeItem(R.id.action_forward);
            menu.removeItem(R.id.action_reload);
            menu.removeItem(R.id.action_browser);
            return true;
        }

        // Web page, so enable/disable back and forward buttons appropriately
        Fragment currentFragment = (Fragment) this.tabViewPager.getAdapter().instantiateItem(this.tabViewPager, currentTab);

        WebPageFragment webPageFragment = (WebPageFragment) currentFragment;
        WebView webView = (WebView) webPageFragment.rootView.findViewById(R.id.fragmentWebView);

        MenuItem backButton = menu.findItem(R.id.action_back);
        MenuItem forwardButton = menu.findItem(R.id.action_forward);

        if (webView.canGoBack()) {
            backButton.setEnabled(true);
            backButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteOverlay), PorterDuff.Mode.MULTIPLY);
        } else {
            backButton.setEnabled(false);
            backButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greyOverlay), PorterDuff.Mode.MULTIPLY);
        }

        if (webView.canGoForward()) {
            forwardButton.setEnabled(true);
            forwardButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteOverlay), PorterDuff.Mode.MULTIPLY);
        } else {
            forwardButton.setEnabled(false);
            forwardButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greyOverlay), PorterDuff.Mode.MULTIPLY);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentTab = this.tabViewPager.getCurrentItem();
        Fragment currentFragment = (Fragment) this.tabViewPager.getAdapter().instantiateItem(this.tabViewPager, currentTab);

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
                    webView.clearCache(true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        int currentTab = this.tabViewPager.getCurrentItem();
        Fragment currentFragment = (Fragment) this.tabViewPager.getAdapter().instantiateItem(tabViewPager, currentTab);

        WebPageFragment webPageFragment = null;
        WebView webView = null;

        if (currentTab <= 2) {
            webPageFragment = (WebPageFragment) currentFragment;
            webView = (WebView) webPageFragment.rootView.findViewById(R.id.fragmentWebView);

            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void changeTabsFont() {

        Typeface tabFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "american_typewriter_regular.ttf");

        ViewGroup vg = (ViewGroup) this.tabLayout.getChildAt(0);
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

        MainActivity parentActivity;

        public TabChangeListener(MainActivity activity) {
            this.parentActivity = activity;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // Reset the action options
            this.parentActivity.invalidateOptionsMenu();

            // Save selected tab
            SharedPreferences settings = getSharedPreferences(LAST_TAB_PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(LAST_TAB_PREF_NAME, position);
            editor.commit();
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
