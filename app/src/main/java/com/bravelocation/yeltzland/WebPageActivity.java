package com.bravelocation.yeltzland;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import android.view.View;

public class WebPageActivity extends AppCompatActivity {

    private WebView webView;
    private String homeUrl;
    private String pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        // Get page parameters from intent extra parameters
        this.homeUrl = "http://yeltz.co.uk/0/";
        this.pageTitle = getString(R.string.forum_name);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.homeUrl = b.getString("homeUrl", this.homeUrl);
            this.pageTitle = b.getString("pageTitle", this.pageTitle);
        }

        // Setup action bar
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(this.pageTitle);

        // Setup web view
        this.webView = (WebView) findViewById(R.id.webView);
        this.webView.setWebViewClient(new YeltzlandWebViewClient());
        this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = this.webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        // Load home URL
        this.webView.loadUrl(this.homeUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webactionbar, menu);

        //TODO: Find a way to set fonts
        // Set fonts and titles
        /*
        Typeface fontAwesomeFont = FontManager.getTypeface( getApplicationContext(), FontManager.FONTAWESOME);

        MenuItem homeItem = menu.findItem(R.id.action_home);
        SpannableStringBuilder homeTitle = new SpannableStringBuilder(getString(R.string.fa_icon_home));
        homeTitle.setSpan(fontAwesomeFont, 0, homeTitle.length(), 0);
        homeItem.setTitle(homeTitle);
        */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                this.webView.loadUrl(this.homeUrl);
                return true;
            case R.id.action_back:
                if (this.webView.canGoBack()) {
                    this.webView.goBack();
                }

                return true;
            case R.id.action_forward:
                if (this.webView.canGoForward()) {
                    this.webView.goForward();
                }

                return true;
            case R.id.action_reload:
                this.webView.reload();

                return true;
            case R.id.action_browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.webView.getUrl()));
                startActivity(browserIntent);

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }



    private class YeltzlandWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Always load pages in place
            return false;
        }
    }
}
