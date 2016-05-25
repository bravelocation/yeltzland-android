package com.bravelocation.yeltzland;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import android.view.View;

public class WebPageActivity extends AppCompatActivity {

    private WebView webView;
    private String homeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        // Setup web view
        this.webView = (WebView) findViewById(R.id.webView);
        this.webView.setWebViewClient(new YeltzlandWebViewClient());
        this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = this.webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        // Load home URL
        this.homeUrl = "http://yeltz.co.uk/0/";
        this.webView.loadUrl(this.homeUrl);
    }

    private class YeltzlandWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Always load pages in place
            return false;
        }
    }
}
