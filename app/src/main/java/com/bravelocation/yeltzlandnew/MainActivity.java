package com.bravelocation.yeltzlandnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    public static final String LAST_TAB_PREF_FILE = "LastTabPrefFile";
    public static final String LAST_TAB_PREF_NAME = "lastSelectedTab";

    private BottomNavigationView bottomNavigation;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        this.bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        this.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                LoadFragment(item.getItemId());
                return true;
            }
        });

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Set the system navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        // Set tab to last shown tab
        int selectedTabItem = R.id.menu_forum;

        SharedPreferences settings = getSharedPreferences(LAST_TAB_PREF_FILE, 0);
        Integer lastTab = settings.getInt(LAST_TAB_PREF_NAME, 0);

        switch (lastTab) {
            case 0:
                selectedTabItem = R.id.menu_forum;
                break;
            case 1:
                selectedTabItem = R.id.menu_official_site;
                break;
            case 2:
                selectedTabItem = R.id.menu_yeltz_tv;
                break;
            case 3:
                selectedTabItem = R.id.menu_twitter;
                break;
            case 4:
                selectedTabItem = R.id.menu_more;
                break;
        }

        this.bottomNavigation.setSelectedItemId(selectedTabItem);
        this.LoadFragment(selectedTabItem);
    }

    private void LoadFragment(int id) {
        this.currentFragment = WebPageFragment.newInstance("https://yeltz.co.uk");
        int position = 0;

        switch (id) {
            case R.id.menu_forum:
                this.currentFragment = WebPageFragment.newInstance("https://yeltz.co.uk");
                position = 0;
                break;
            case R.id.menu_official_site:
                this.currentFragment = WebPageFragment.newInstance("https://www.ht-fc.co.uk");;
                position = 1;
                break;
            case R.id.menu_yeltz_tv:
                this.currentFragment = WebPageFragment.newInstance("https://www.youtube.com/channel/UCGZMWQtMsC4Tep6uLm5V0nQ");
                position = 2;
                break;
            case R.id.menu_twitter:
                this.currentFragment = WebPageFragment.newInstance("https://mobile.twitter.com/halesowentownfc");
                position = 3;
                break;
            case R.id.menu_more:
                this.currentFragment = new MoreFragment();
                position = 4;
                break;
        }

        // Save selected tab
        SharedPreferences settings = getSharedPreferences(LAST_TAB_PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LAST_TAB_PREF_NAME, position);
        editor.commit();

        if (this.currentFragment != null) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, this.currentFragment).commit();
        }

        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.webactionbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.currentFragment == null) {
            return super.onPrepareOptionsMenu(menu);
        }

        int currentTab = this.bottomNavigation.getSelectedItemId();

        if (currentTab == R.id.menu_more) {
            // More tab - remove all
            menu.removeItem(R.id.action_home);
            menu.removeItem(R.id.action_back);
            menu.removeItem(R.id.action_forward);
            menu.removeItem(R.id.action_reload);
            menu.removeItem(R.id.action_browser);
            return true;
        }

        // Web page, so enable/disable back and forward buttons appropriately
        WebPageFragment webPageFragment = (WebPageFragment) this.currentFragment;
        if (webPageFragment.webView == null) {
            // Web page not initialised
            return true;
        }

        MenuItem backButton = menu.findItem(R.id.action_back);
        MenuItem forwardButton = menu.findItem(R.id.action_forward);

        if (webPageFragment.webView.canGoBack()) {
            backButton.setEnabled(true);
            backButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.whiteOverlay), PorterDuff.Mode.MULTIPLY);
        } else {
            backButton.setEnabled(false);
            backButton.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greyOverlay), PorterDuff.Mode.MULTIPLY);
        }

        if (webPageFragment.webView.canGoForward()) {
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
        if (this.currentFragment == null) {
            return super.onOptionsItemSelected(item);
        }

        int currentTab = this.bottomNavigation.getSelectedItemId();

        WebPageFragment webPageFragment = null;
        WebView webView = null;

        if (currentTab == R.id.menu_forum || currentTab == R.id.menu_official_site || currentTab == R.id.menu_yeltz_tv || currentTab == R.id.menu_twitter) {
            webPageFragment = (WebPageFragment) this.currentFragment;
            webView = webPageFragment.webView;
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
        int currentTab = this.bottomNavigation.getSelectedItemId();

        if (currentTab == R.id.menu_forum || currentTab == R.id.menu_official_site || currentTab == R.id.menu_yeltz_tv) {
            WebPageFragment webPageFragment = (WebPageFragment) this.currentFragment;

            if (webPageFragment.webView != null && (keyCode == KeyEvent.KEYCODE_BACK) && webPageFragment.webView.canGoBack()) {
                webPageFragment.webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
