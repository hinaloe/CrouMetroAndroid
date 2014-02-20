package com.innerlogic.croumetro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.innerlogic.croumetro.net.GetRequest;
import com.innerlogic.croumetro.net.OAuth2Helper;
import com.innerlogic.croumetro.tools.Constants;
import com.innerlogic.croumetro.user.UserEntity;

import java.util.concurrent.ExecutionException;

public class SplashScreenActivity extends ActionBarActivity {

    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private AsyncTask<Void, Void, String> loginTesting = null;
    UserEntity userEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        try {
            loginTest();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loginTest() throws ExecutionException, InterruptedException {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oAuth2Helper = new OAuth2Helper(this.prefs);
        loginTesting = new GetRequest(Constants.VERIFY, oAuth2Helper, prefs) {
            @Override
            protected void onPostExecute(String json) {
                if (json.contains("401")) {
                    Intent i = new Intent(SplashScreenActivity.this, SignInActivity.class);
                    startActivity(i);
                }
                userEntity = new UserEntity(json);
                userEntity.setIsCurrentUser(true);
                Intent i = new Intent(SplashScreenActivity.this, TimelineActivity.class);
                i.putExtra("currentUserEntity", userEntity);
                startActivity(i);
            }
        }.execute();
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
            View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
            return rootView;
        }
    }

}
