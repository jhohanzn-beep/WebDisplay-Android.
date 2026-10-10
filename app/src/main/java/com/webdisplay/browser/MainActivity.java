package com.webdisplay.browser;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.*;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        EditText url = new EditText(this);
        url.setSingleLine(true);
        url.setText("https://www.google.com");

        Button go = new Button(this);
        go.setText("Ir");

        WebView web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());

        layout.addView(url);
        layout.addView(go);
        layout.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(layout);

        go.setOnClickListener(v -> {
            String address = url.getText().toString().trim();
            if (!address.startsWith("http://") &&
                !address.startsWith("https://")) {
                address = "https://" + address;
            }
            web.loadUrl(address);
        });

        web.loadUrl("https://www.google.com");
    }
}
