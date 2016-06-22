package com.dementh.lib.battlenet_oauth2.connections;

import android.content.SharedPreferences;
import android.util.Log;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BnOAuth2Helper {

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final CredentialStore credentialStore;

    private AuthorizationCodeFlow flow;

    private BnOAuth2Params oauth2Params;

    public BnOAuth2Helper(SharedPreferences sharedPreferences, BnOAuth2Params oauth2Params) {
        this.credentialStore = new BnSharedPreferencesCredentialStore(sharedPreferences);
        this.oauth2Params = oauth2Params;
        this.flow = new AuthorizationCodeFlow.Builder(oauth2Params.getAccessMethod() , HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(oauth2Params.getTokenServerUrl()), new ClientParametersAuthentication(oauth2Params.getClientId(),oauth2Params.getClientSecret()), oauth2Params.getClientId(), oauth2Params.getAuthorizationServerEncodedUrl()).setCredentialStore(this.credentialStore).build();
    }

    public String getAuthorizationUrl() {
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(oauth2Params.getRederictUri()).setScopes(convertScopesToString(oauth2Params.getScope())).build();
        return authorizationUrl;
    }

    public void retrieveAndStoreAccessToken(String authorizationCode) throws IOException {
        Log.i(BnConstants.TAG, "retrieveAndStoreAccessToken for code ".concat(authorizationCode));
        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode).setScopes(convertScopesToString(oauth2Params.getScope())).setRedirectUri(oauth2Params.getRederictUri()).execute();
        Log.i(BnConstants.TAG, "Found tokenResponse");
        Log.i(BnConstants.TAG, "Access Token : ".concat(tokenResponse.getAccessToken()));
        Log.i(BnConstants.TAG, "Refresh Token : ".concat(tokenResponse.getRefreshToken()));
        flow.createAndStoreCredential(tokenResponse, oauth2Params.getUserId());
    }

    public String getAccessToken() throws IOException {
        return flow.loadCredential(oauth2Params.getUserId()).getAccessToken();
    }

    public Credential loadCredential() throws IOException {
        return flow.loadCredential(oauth2Params.getUserId());
    }

    public void clearCredentials() throws IOException {
        flow.getCredentialStore().delete(oauth2Params.getUserId(), null);
    }

    private Collection<String> convertScopesToString(String scopesConcat) {
        String[] scopes = scopesConcat.split(",");
        Collection<String> collection = new ArrayList<String>();
        Collections.addAll(collection, scopes);
        return collection;
    }
}
