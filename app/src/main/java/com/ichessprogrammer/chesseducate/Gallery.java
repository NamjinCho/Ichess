package com.ichessprogrammer.chesseducate;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 남지니 on 2016-08-31.
 */
public class Gallery extends Activity {
    WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_gallery);
        browser = (WebView) findViewById(R.id.webView);
        browser.getSettings().setJavaScriptEnabled(true); // allow scripts
        browser.setWebViewClient(new WebViewClient() ); // page navigation
        browser.loadUrl("http://mchess.cafe24.com/webGallery/index.php");

    }
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        System.gc();
    }
}
