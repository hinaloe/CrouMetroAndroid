package com.innerlogic.croumetro.net;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.innerlogic.croumetro.tools.Constants;

/**
 * Created by Timothy on 14/02/18.
 */


public class GetRequest extends AsyncTask<Void, Void, String> {
    private SharedPreferences prefs;
    private OAuth2Helper oAuth2Helper;
    private String mUrl;

    public GetRequest(String mUrl, OAuth2Helper oAuth2Helper, SharedPreferences prefs) {
        this.mUrl = mUrl;
        this.oAuth2Helper = oAuth2Helper;
        this.prefs = prefs;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String apiResponse = oAuth2Helper.executeGetRequest(mUrl);
            Log.i(Constants.TAG, "Received response from API : " + apiResponse);
            return apiResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            String apiResponse = ex.getMessage();
            return apiResponse;
        }
    }

}
