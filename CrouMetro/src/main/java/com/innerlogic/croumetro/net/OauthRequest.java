package com.innerlogic.croumetro.net;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.innerlogic.croumetro.tools.Constants;

import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class OauthRequest {

    private OAuth2Helper oAuth2Helper;
    private Map<String, String> paramsMap;
    private String url;

    public OauthRequest(SharedPreferences pref, OAuth2Helper helper) {
        SharedPreferences prefs = pref;
        this.oAuth2Helper = helper;
    }

    public String PostRequest(String url, Map<String, String> paramsMap) throws ExecutionException, InterruptedException {
        PostRequestAsync oauthAsyncTask = new PostRequestAsync();
        this.url = url;
        this.paramsMap = paramsMap;
        oauthAsyncTask.execute();
        try {
            oauthAsyncTask.get();
            return oauthAsyncTask.getResponseMsg();
        } catch (CancellationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class PostRequestAsync extends AsyncTask<String, Void, String> {
        private String apiResponse = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                Log.i(Constants.TAG, "Received response from API : " + apiResponse);
                apiResponse = oAuth2Helper.executePostRequest(url, paramsMap);
                return apiResponse;
            } catch (Exception ex) {
                ex.printStackTrace();
                apiResponse = ex.getMessage();
                return apiResponse;
            }
        }

        @Override
        protected void onPostExecute(String result) {
        }

        public String getResponseMsg() {
            return apiResponse;
        }


    }

    public class GetRequestAsync extends AsyncTask<String, Void, String> {
        private String apiResponse = null;
        private String mUrl;

        public GetRequestAsync(String url) {
            mUrl = url;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String uri = params[0];
                apiResponse = oAuth2Helper.executeGetRequest(uri);
                Log.i(Constants.TAG, "Received response from API : " + apiResponse);
                return apiResponse;
            } catch (Exception ex) {
                ex.printStackTrace();
                apiResponse = ex.getMessage();
                return apiResponse;
            }
        }


        public String getResponseMsg() {
            return apiResponse;
        }

    }
}
