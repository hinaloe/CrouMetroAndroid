package com.innerlogic.croumetro.net;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.innerlogic.croumetro.tools.Constants;

import java.io.IOException;

/**
 * Created by Timothy on 14/02/07.
 */
public class SharedPreferencesCredentialStore implements com.google.api.client.auth.oauth2.CredentialStore {
    private static final String ACCESS_TOKEN = "_access_token";
    private static final String EXPIRES_IN = "_expires_in";
    private static final String REFRESH_TOKEN = "_refresh_token";
    private static final String SCOPE = "_scope";

    private SharedPreferences prefs;

    public SharedPreferencesCredentialStore(SharedPreferences sharedPreferences) {
        this.prefs = sharedPreferences;
    }

    @Override
    public boolean load(String s, Credential credential) throws IOException {
        Log.i(Constants.TAG, "Loading credential for userId " + s);
        Log.i(Constants.TAG, "Loaded access token = " + prefs.getString(s + ACCESS_TOKEN, ""));

        credential.setAccessToken(prefs.getString(s + ACCESS_TOKEN, null));

        if (prefs.contains(s + EXPIRES_IN)) {
            credential.setExpirationTimeMilliseconds(prefs.getLong(s + EXPIRES_IN, 0));
        }
        credential.setRefreshToken(prefs.getString(s + REFRESH_TOKEN, null));

        return true;
    }

    @Override
    public void store(String s, Credential credential) throws IOException {
        Log.i(Constants.TAG, "Storing credential for userId " + s);
        Log.i(Constants.TAG, "Access Token = " + credential.getAccessToken());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(s + ACCESS_TOKEN, credential.getAccessToken());

        if (credential.getExpirationTimeMilliseconds() != null) {
            editor.putLong(s + EXPIRES_IN, credential.getExpirationTimeMilliseconds());
        }

        if (credential.getRefreshToken() != null) {
            editor.putString(s + REFRESH_TOKEN, credential.getRefreshToken());
        }
        editor.commit();
    }

    @Override
    public void delete(String s, Credential credential) throws IOException {
        Log.i(Constants.TAG, "Deleting credential for userId " + s);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(s + ACCESS_TOKEN);
        editor.remove(s + EXPIRES_IN);
        editor.remove(s + REFRESH_TOKEN);
        editor.remove(s + SCOPE);
        editor.commit();
    }
}
