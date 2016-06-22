package com.dementh.lib.bndroidoauth.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.dementh.lib.battlenet_oauth2.activities.BnOAuthAccessTokenActivity;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Helper;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Params;
import com.dementh.lib.bndroidoauth.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button btnBattlenetLogin;
    private Button btnBattlenetClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        btnBattlenetLogin = (Button) findViewById(R.id.btn_login);
        btnBattlenetClean = (Button) findViewById(R.id.btn_clear);

        // Create BnOAuthParams to send
        final BnOAuth2Params bnOAuth2Params = new BnOAuth2Params("Your clientID", "Your client secret", "Zone you want", "Your redirectUri", "Your userId", "Scopes you want");
        /*
        * Example
        * */
        //bnOAuth2Params = new BnOAuth2Params("111XXXXYYYYZZZZ", "222XXXYYYYZZZZ333", BnConstants.ZONE_EUROPE, "https://localhost", "MyAppName", BnConstants.SCOPE_WOW, BnConstants.SCOPE_SC2);

        // Launch webview to connect in Battlenet
        btnBattlenetLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startOauthFlow(bnOAuth2Params);
            }
        });

        // Clean current credentials
        btnBattlenetClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCredentials(bnOAuth2Params);
            }
        });
    }

    /**
     * Starts the activity that takes care of the OAuth2 flow
     *
     */
    private void startOauthFlow(final BnOAuth2Params bnOAuth2Params) {
        final Intent intent = new Intent(this, BnOAuthAccessTokenActivity.class);
        // Send BnOAuth2Params
        intent.putExtra(BnConstants.BUNDLE_BNPARAMS, bnOAuth2Params);
        // Send redirect Activity
        intent.putExtra(BnConstants.BUNDLE_REDIRECT_ACTIVITY, DestinyActivity.class);
        startActivity(intent);
    }

    /**
     * Clears our credentials (token and token secret) from the shared preferences.
     * We also setup the authorizer (without the token).
     * After this, no more authorized API calls will be possible.
     * @throws java.io.IOException
     */
    private void clearCredentials(final BnOAuth2Params bnOAuth2Params)  {
        try {
            new BnOAuth2Helper(sharedPreferences, bnOAuth2Params).clearCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
