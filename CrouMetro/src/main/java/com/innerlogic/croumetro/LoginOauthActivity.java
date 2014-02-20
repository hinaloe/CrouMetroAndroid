package com.innerlogic.croumetro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.tools.Constants;

import java.net.URLDecoder;

public class LoginOauthActivity extends Activity {

    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private WebView webview;
    private Boolean handled;
    private boolean hasLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "Starting task to retrieve request token.");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oAuth2Helper = new OAuth2Helper(this.prefs);
        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVisibility(View.VISIBLE);
        setContentView(webview);

        String authorizationUrl = oAuth2Helper.getAuthorizationUrl();

        handled = false;
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {
                Log.d(Constants.TAG, "onPageStarted : " + url + " handled = " + handled);
                if (url.startsWith(Constants.RESPONSE_URL)) {
                    webview.setVisibility(View.INVISIBLE);

                    if (!handled) {
                        new ProcessToken(url, oAuth2Helper).execute();
                    }
                } else {
                    webview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                Log.d(Constants.TAG, "onPageFinished : " + url + " handled = " + handled);
            }
        });
        webview.loadUrl(authorizationUrl);
    }

    private class ProcessToken extends AsyncTask<Uri, Void, Void> {

        String url;
        boolean startActivity = false;


        public ProcessToken(String url, OAuth2Helper oAuth2Helper) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Uri... params) {
            if (url.startsWith(Constants.RESPONSE_URL)) {
                Log.i(Constants.TAG, "Redirect URL found" + url);
                handled = true;
                try {
                    if (url.indexOf("code=") != -1) {
                        Uri uri = Uri.parse(url);
                        String authorizationCode = extractCodeFromUrl(uri);

                        Log.i(Constants.TAG, "Found code = " + authorizationCode);

                        oAuth2Helper.retrieveAndStoreAccessToken(authorizationCode);
                        startActivity = true;
                        hasLoggedIn = true;

                    } else if (url.indexOf("error=") != -1) {
                        startActivity = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.i(Constants.TAG, "Failed: " + url);
            }
            return null;
        }

        private String extractCodeFromUrl(Uri uri) throws Exception {
            String encodedCode = uri.getQueryParameter("code");
            return URLDecoder.decode(encodedCode, "UTF-8");
        }

        protected void onPostExecute(Void result) {
            if (startActivity) {
                Log.i(Constants.TAG, "Return to main activity");
                startActivity(new Intent(LoginOauthActivity.this, TimelineActivity.class));
                finish();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_oauth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login_oauth, container, false);
            return rootView;
        }
    }

}
