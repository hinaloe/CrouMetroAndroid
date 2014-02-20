package com.innerlogic.croumetro.net;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.innerlogic.croumetro.tools.Constants;

import java.util.Map;

/**
 * Created by Timothy on 14/02/19.
 */
public class PostRequest extends AsyncTask<Void, Void, String> {
    private SharedPreferences prefs;
    private Map<String, String> paramsMap;
    private OAuth2Helper oAuth2Helper;
    private String mUrl;

    public PostRequest(String mUrl, Map<String, String> paramsMap, OAuth2Helper oAuth2Helper, SharedPreferences prefs) {
        this.mUrl = mUrl;
        this.oAuth2Helper = oAuth2Helper;
        this.prefs = prefs;
        this.paramsMap = paramsMap;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String apiResponse = oAuth2Helper.executePostRequest(mUrl, paramsMap);
            Log.i(Constants.TAG, "Received response from API : " + apiResponse);
            return apiResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            String apiResponse = ex.getMessage();
            return apiResponse;
        }
    }

}
