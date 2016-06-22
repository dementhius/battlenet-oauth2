package com.dementh.lib.battlenet_oauth2.connections;

import android.content.SharedPreferences;
import android.util.Log;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;

import java.io.IOException;

public class BnSharedPreferencesCredentialStore implements CredentialStore {

    private static final String ACCESS_TOKEN = "_access_token";
    private static final String EXPIRES_IN = "_expires_in";
    private static final String REFRESH_TOKEN = "_refresh_token";
    private static final String SCOPE = "_scope";

    private SharedPreferences prefs;

    public BnSharedPreferencesCredentialStore(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public boolean load(String userId, Credential credential) throws IOException {
        Log.i(BnConstants.TAG, "Loading credential for userId ".concat(userId));
        Log.i(BnConstants.TAG, "Loaded access token = ".concat(prefs.getString(userId + ACCESS_TOKEN, "")));

        credential.setAccessToken(prefs.getString(userId + ACCESS_TOKEN, null));

        if (prefs.contains(userId + EXPIRES_IN)) {
            credential.setExpirationTimeMilliseconds(prefs.getLong(userId + EXPIRES_IN,0));
        }
        credential.setRefreshToken(prefs.getString(userId + REFRESH_TOKEN, null));

        return true;
    }

    @Override
    public void store(String userId, Credential credential) throws IOException {
        Log.i(BnConstants.TAG, "Storing credential for userId ".concat(userId));
        Log.i(BnConstants.TAG, "Access Token = ".concat(credential.getAccessToken()));
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(userId + ACCESS_TOKEN,credential.getAccessToken());

        if (credential.getExpirationTimeMilliseconds()!=null) {
            editor.putLong(userId + EXPIRES_IN,credential.getExpirationTimeMilliseconds());
        }

        if (credential.getRefreshToken()!=null) {
            editor.putString(userId + REFRESH_TOKEN,credential.getRefreshToken());
        }
        editor.commit();
    }

    @Override
    public void delete(String userId, Credential credential) throws IOException {
        Log.i(BnConstants.TAG, "Deleting credential for userId ".concat(userId));
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(userId + ACCESS_TOKEN);
        editor.remove(userId + EXPIRES_IN);
        editor.remove(userId + REFRESH_TOKEN);
        editor.remove(userId + SCOPE);
        editor.commit();
    }
}
