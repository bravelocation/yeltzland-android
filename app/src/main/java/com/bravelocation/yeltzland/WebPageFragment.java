package com.bravelocation.yeltzland;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public View rootView;

    public WebPageFragment() {
        // Required empty public constructor
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
        WebView webView = (WebView) this.rootView.findViewById(R.id.fragmentWebView);
        webView.setWebViewClient(new YeltzlandWebViewClient(progressBar));
        webView.setWebChromeClient(new YeltzlandWebChromeClient(progressBar));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        // Load home URL
        progressBar.setProgress(0);
        webView.loadUrl(this.homeUrl);

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        }

        return this.rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState ){
        WebView webView = (WebView) this.rootView.findViewById(R.id.fragmentWebView);
        webView.saveState(outState);
    }

    private class YeltzlandWebViewClient extends WebViewClient {
        private ProgressBar progressBar;

        public YeltzlandWebViewClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            this.progressBar.setProgress(0);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

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

            Toast.makeText(getContext(), getContext().getString(R.string.webpage_error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Always load pages in place
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
