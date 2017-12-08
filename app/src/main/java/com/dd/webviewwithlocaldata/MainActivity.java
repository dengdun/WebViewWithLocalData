package com.dd.webviewwithlocaldata;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = findViewById(R.id.webview);
        webview.addJavascriptInterface(new JavaScriptInterface(webview), "AndroidJSInterfaceV2");
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webview.evaluateJavascript("javascript:"+getStringFromFile(getAssets(),"WAJavascriptBridge.js","UTF-8"),null);
                }
            }
        });
        webview.loadUrl("file:///android_asset/mobile_api.html");


    }


    public static String getStringFromFile(AssetManager assetManager, String fileName, String encodeing)
    {
        try {
            InputStream is = assetManager.open(fileName);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int count = -1;
            while((count = is.read(data,0,4096)) != -1)
                outStream.write(data, 0, count);

            data = null;
            return new String(outStream.toByteArray(), encodeing);
        }catch (Exception e)
        {
            return null;
        }
    }
}
