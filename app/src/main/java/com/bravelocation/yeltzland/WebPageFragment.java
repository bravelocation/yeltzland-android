package com.bravelocation.yeltzland;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


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

        // Setup web view
        WebView webView = (WebView) this.rootView.findViewById(R.id.fragmentWebView);
        webView.setWebViewClient(new YeltzlandWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);

        // Load home URL
        webView.loadUrl(this.homeUrl);

        return this.rootView;
    }


    private class YeltzlandWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Always load pages in place
            return false;
        }
    }
}
