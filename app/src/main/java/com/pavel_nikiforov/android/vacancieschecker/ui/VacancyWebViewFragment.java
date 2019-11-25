package com.pavel_nikiforov.android.vacancieschecker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pavel_nikiforov.android.vacanciescheckerforandroid.R;

public class VacancyWebViewFragment extends Fragment {
    private String mUrl;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        mUrl = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancy_web_view, container, false);

        mWebView = (WebView) view.findViewById(R.id.vacwebview_web_view);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);

        return view;
    }
}
