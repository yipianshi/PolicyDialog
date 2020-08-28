package com.d.policydialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WebViewActivity extends AppCompatActivity {

    private static final String PARAMS1 = "PARAMS1";
    private static final String PARAMS2 = "PARAMS2";
    private WebView webView;
    private Toolbar toolbar;
    private String url;

    public static void start(Context context, String title, String url) {
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(PARAMS1, title);
        starter.putExtra(PARAMS2, url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initViews();
        getData();
        initWebView();
    }

    private void initViews() {
        webView = findViewById(R.id.webview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    private void getData() {
        String title = getIntent().getStringExtra(PARAMS1);
        url = getIntent().getStringExtra(PARAMS2);
        toolbar.setTitle(title);
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}