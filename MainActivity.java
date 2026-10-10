package com.webdisplay.browser;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;
    private EditText address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(12, 20, 35));

        TextView title = new TextView(this);
        title.setText("WEBDISPLAY");
        title.setTextColor(Color.rgb(70, 220, 255));
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setPadding(16, 12, 16, 12);
        title.setBackgroundColor(Color.rgb(12, 20, 35));
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout bar = new LinearLayout(this);
        bar.setPadding(8, 6, 8, 6);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        address = new EditText(this);
        address.setSingleLine(true);
        address.setText("https://www.google.com");
        address.setHint("https://example.com");
        address.setTextColor(Color.WHITE);
        address.setHintTextColor(Color.LTGRAY);
        address.setSelectAllOnFocus(true);
        address.setTextSize(14);
        address.setPadding(10, 0, 10, 0);
        address.setBackgroundColor(Color.rgb(35, 47, 66));
        bar.addView(address, new LinearLayout.LayoutParams(0, 48, 1));

        Button go = new Button(this);
        go.setText("IR");
        bar.addView(go, new LinearLayout.LayoutParams(-2, 48));
        root.addView(bar);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String scheme = request.getUrl().getScheme();
                return !(scheme.equals("https") || scheme.equals("http"));
            }
        });
        root.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);

        go.setOnClickListener(v -> loadAddress());
        address.setOnEditorActionListener((v, actionId, event) -> {
            loadAddress();
            return true;
        });
        loadUrl("https://www.google.com");
    }

    private void loadAddress() {
        String value = address.getText().toString().trim();
        if (value.isEmpty()) {
            Toast.makeText(this, "Escribe una dirección web", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!value.matches("(?i)^https?://.*")) {
            value = "https://" + value;
        }
        if (!value.matches("(?i)^https?://[^\\s]+\\.[^\\s]+.*")) {
            Toast.makeText(this, "Revisa el enlace (ej. https://example.com)", Toast.LENGTH_LONG).show();
            return;
        }
        loadUrl(value);
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(address.getWindowToken(), 0);
    }

    private void loadUrl(String url) {
        address.setText(url);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
