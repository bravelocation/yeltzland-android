package com.bravelocation.yeltzlandnew;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebPageFragment extends Fragment {
    public String homeUrl;
    public WebView webView;
    private View rootView;
    private CookieManager cookieManager;

    public WebPageFragment() {
    }

    public static WebPageFragment newInstance(String homeUrl) {
        WebPageFragment fragment = new WebPageFragment();
        fragment.homeUrl = homeUrl;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_web_page, container, false);

        // Get progress bar
        ProgressBar progressBar = (ProgressBar) this.rootView.findViewById(R.id.progressBar);
        progressBar.setMax(100);

        // Setup web view
        this.webView = (WebView) this.rootView.findViewById(R.id.fragmentWebView);

        // Init cookie manager after after init web view
        this.cookieManager = CookieManager.getInstance();
        this.cookieManager.setAcceptCookie(true);
        this.cookieManager.setAcceptThirdPartyCookies(this.webView, true);

        this.webView.setWebViewClient(new YeltzlandWebViewClient(progressBar, this.cookieManager));
        this.webView.setWebChromeClient(new YeltzlandWebChromeClient(progressBar));
        this.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = this.webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Load home URL
        progressBar.setProgress(0);
        this.webView.loadUrl(this.homeUrl);

        if (savedInstanceState != null) {
            this.webView.restoreState(savedInstanceState);
        }

        return this.rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.webView != null) {
            if (isVisibleToUser) {
                Log.d("WebPageFragment", "Reloading fragment now visible: " + this.homeUrl);
                this.webView.reload();
            } else {
                Log.d("WebPageFragment", "Pausing fragment now not visible: " + this.homeUrl);
                this.webView.onPause();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.webView != null && this.cookieManager != null) {
            this.cookieManager.flush();
            this.webView.onPause();
            Log.d("WebPageFragment", "Pausing web fragment: " + this.homeUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.webView != null && this.cookieManager != null) {
            this.webView.onResume();
            this.cookieManager.flush();
            Log.d("WebPageFragment", "Resumed web fragment: " + this.homeUrl);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState ){
        this.webView.saveState(outState);
    }

    private class YeltzlandWebViewClient extends WebViewClient {
        private ProgressBar progressBar;
        CookieManager cookieManager;

        public YeltzlandWebViewClient(ProgressBar progressBar, CookieManager cookieManager) {
            this.progressBar = progressBar;
            this.cookieManager = cookieManager;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            this.progressBar.setProgress(0);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (this.cookieManager != null) {
                this.cookieManager.flush();
            }

            this.progressBar.setProgress(this.progressBar.getMax());

            // Reset options
            Activity currentActivity = getActivity();
            if (currentActivity != null) {
                currentActivity.invalidateOptionsMenu();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            // Show toast on connection error
            Activity currentActivity = getActivity();
            if (currentActivity != null) {
                Toast.makeText(currentActivity, currentActivity.getString(R.string.webpage_error), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Always load pages in place
            Log.d("WebPageFragment", "In shouldOverrideUrlLoading for " + url);
            return false;
        }
    }

    private class YeltzlandWebChromeClient extends WebChromeClient {
        private ProgressBar progressBar;

        public YeltzlandWebChromeClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            this.progressBar.setProgress(newProgress);
        }
    }
}
