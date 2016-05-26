package com.bravelocation.yeltzland;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        int barColor = ContextCompat.getColor(getContext(), R.color.yeltzBlue);
        progressBar.getProgressDrawable().setColorFilter(barColor, android.graphics.PorterDuff.Mode.SRC_IN);

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

        return this.rootView;
    }

    private class YeltzlandWebViewClient extends WebViewClient {
        private ProgressBar progressBar;

        public YeltzlandWebViewClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setProgress(0);
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
        public void onProgressChanged(WebView view, int newProgress){
            this.progressBar.setProgress(newProgress);
        }
    }
}
