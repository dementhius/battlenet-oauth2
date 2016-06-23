package com.dementh.lib.bndroidoauth.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dementh.lib.battlenet_oauth2.BnConstants;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Helper;
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Params;
import com.dementh.lib.bndroidoauth.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinyActivity extends AppCompatActivity {

    public static final int REST_WOW = 1;
    public static final int REST_SC2 = 2;
    public static final int REST_BATTLETAG = 3;

    private Button btnWoW;
    private Button btnSC2;
    private Button btnBattletag;

    private BnOAuth2Params bnOAuth2Params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destiny);

        bnOAuth2Params = this.getIntent().getExtras().getParcelable(BnConstants.BUNDLE_BNPARAMS);

        // UI
        btnWoW = (Button) findViewById(R.id.btn_wow);
        btnSC2 = (Button) findViewById(R.id.btn_sc2);
        btnBattletag = (Button) findViewById(R.id.btn_battletag);

        btnWoW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRestService(REST_WOW);
            }
        });

        btnSC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRestService(REST_SC2);
            }
        });

        btnBattletag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRestService(REST_BATTLETAG);
            }
        });
    }

    private void callRestService(final int option) {
        final Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("option", option);
        intent.putExtra(BnConstants.BUNDLE_BNPARAMS, bnOAuth2Params);
        startActivity(intent);
    }
}
