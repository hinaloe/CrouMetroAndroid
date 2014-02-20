package com.innerlogic.croumetro.net;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.innerlogic.croumetro.tools.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timothy on 14/02/07.
 */
public class OAuth2Helper {
    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    private final CredentialStore credentialStore;

    private AuthorizationCodeFlow flow;

    public OAuth2Helper(SharedPreferences sharedPreferences) {
        this.credentialStore = new SharedPreferencesCredentialStore(sharedPreferences);
        this.flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(), HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(Constants.OAUTH_TOKEN), new ClientParametersAuthentication(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET), Constants.CONSUMER_KEY, Constants.AUTH_URL).setCredentialStore(this.credentialStore).build();
    }

    public String getAuthorizationUrl() {
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(Constants.RESPONSE_URL).setScopes(convertScopesToString(Constants.VERIFY)).build();
        return authorizationUrl;
    }

    public void retrieveAndStoreAccessToken(String authorizationCode) throws IOException {
        Log.i(Constants.TAG, "retrieveAndStoreAccessToken for code " + authorizationCode);
        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode).setTokenServerUrl(new GenericUrl(Constants.OAUTH_TOKEN)).execute();
        flow.createAndStoreCredential(tokenResponse, "CrouMetro");
    }

    public String executeGetRequest(String apiUrl) throws IOException {
        Log.i(Constants.TAG, "Executing API call at url " + apiUrl);
        return HTTP_TRANSPORT.createRequestFactory(loadCredential()).buildGetRequest(new GenericUrl(apiUrl)).execute().parseAsString();
    }

    public String executePostRequest(String apiUrl, Map<String, String> paramsMap) throws IOException {
        Log.i(Constants.TAG, "Executing API call at url " + apiUrl);
        if(paramsMap == null)
        {
            paramsMap = new HashMap<String, String>();
        }
        UrlEncodedContent urlEncodedContent = new UrlEncodedContent(paramsMap);
        return HTTP_TRANSPORT.createRequestFactory(loadCredential()).buildPostRequest(new GenericUrl(apiUrl), urlEncodedContent)
                .execute().parseAsString();
    }

    public Credential loadCredential() throws IOException {
        return flow.loadCredential("CrouMetro");
    }

    public void clearCredentials() throws IOException {
        flow.getCredentialStore().delete("CrouMetro", null);
    }

    private Collection<String> convertScopesToString(String scopesConcat) {
        String[] scopes = scopesConcat.split(",");
        Collection<String> collection = new ArrayList<String>();
        Collections.addAll(collection, scopes);
        return collection;
    }

}
