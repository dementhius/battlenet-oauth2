package com.dementh.lib.battlenet_oauth2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Helper;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Params;

import java.net.URLDecoder;

public class BnOAuthAccessTokenActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private BnOAuth2Helper oAuth2Helper;
    private WebView  webview;
    boolean handled = false;
    private boolean hasLoggedIn;
    private BnOAuth2Params bnOAuth2Params;
    private Class redirectActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(BnConstants.TAG, "Starting task to retrieve request token");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundle = this.getIntent().getExtras();
        // Receiving redirection activity class
        redirectActivity = (Class) bundle.get(BnConstants.BUNDLE_REDIRECT_ACTIVITY);
        // Receiving BnOAuth2Params from intent
        bnOAuth2Params = bundle.getParcelable(BnConstants.BUNDLE_BNPARAMS);

        // Init helper and webview
        oAuth2Helper = new BnOAuth2Helper(this.prefs, bnOAuth2Params);
        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVisibility(View.VISIBLE);
        setContentView(webview);

        final String authorizationUrl = oAuth2Helper.getAuthorizationUrl();
        Log.i(BnConstants.TAG, "Using authorizationUrl = " + authorizationUrl);

        handled = false;
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url,Bitmap bitmap)  {
                Log.d(BnConstants.TAG, "onPageStarted : " + url + " handled = " + handled);
            }
            @Override
            public void onPageFinished(final WebView view, final String url)  {
                Log.d(BnConstants.TAG, "onPageFinished : " + url + " handled = " + handled);

                if (url.startsWith(bnOAuth2Params.getRederictUri())) {
                    webview.setVisibility(View.INVISIBLE);

                    if (!handled) {
                        new ProcessToken(url).execute();
                    }
                } else {
                    webview.setVisibility(View.VISIBLE);
                }
            }
        });
        webview.loadUrl(authorizationUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(BnConstants.TAG, "onResume called with " + hasLoggedIn);
        if (hasLoggedIn) {
            finish();
        }
    }

    private class ProcessToken extends AsyncTask<Uri, Void, Void> {

        String url;
        boolean startActivity = false;

        public ProcessToken(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Uri...params) {
            if (url.startsWith(bnOAuth2Params.getRederictUri())) {
                Log.i(BnConstants.TAG, "Redirect URL found: ".concat(url));
                handled=true;
                try {
                    if (url.indexOf("code=") != -1) {
                        String authorizationCode = extractCodeFromUrl(url);

                        Log.i(BnConstants.TAG, "Found code = ".concat(authorizationCode));

                        oAuth2Helper.retrieveAndStoreAccessToken(authorizationCode);
                        startActivity=true;
                        hasLoggedIn=true;

                    } else if (url.indexOf("error=") != -1) {
                        startActivity=true;
                    }

                } catch (Exception e) {
                    Log.e(BnConstants.TAG, "Error processing token", e);
                }

            } else {
                Log.i(BnConstants.TAG, "Not doing anything for url ".concat(url));
            }
            return null;
        }

        private String extractCodeFromUrl(String url) throws Exception {
            final String encodedCode = url.substring(bnOAuth2Params.getRederictUri().length() + 6, url.length());
            return URLDecoder.decode(encodedCode, "UTF-8");
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
            if (startActivity) {
                Log.i(BnConstants.TAG," Redirect to the activity you want: ".concat(redirectActivity.getName()));
                final Intent intent = new Intent(BnOAuthAccessTokenActivity.this, redirectActivity);
                intent.putExtra(BnConstants.BUNDLE_BNPARAMS, bnOAuth2Params);
                startActivity(intent);
                finish();
            }
        }
    }
}
