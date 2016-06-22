package com.dementh.lib.bndroidoauth.example;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

    private final int REST_WOW = 1;
    private final int REST_SC2 = 2;
    private final int REST_BATTLETAG = 3;

    private Button btnWoW;
    private Button btnSC2;
    private Button btnBattletag;
    private TextView tvResult;

    private ConnectionService.RequestApiInterface requestApiInterface;

    private SharedPreferences prefs;
    private BnOAuth2Helper bnOAuth2Helper;
    private BnOAuth2Params bnOAuth2Params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destiny);

        // Battlenet rest calls
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        bnOAuth2Params = this.getIntent().getExtras().getParcelable(BnConstants.BUNDLE_BNPARAMS);
        bnOAuth2Helper = new BnOAuth2Helper(this.prefs, bnOAuth2Params);

        requestApiInterface = ConnectionService.getRequestApiInterface(bnOAuth2Params.getZone());

        // UI
        btnWoW = (Button) findViewById(R.id.btn_wow);
        btnSC2 = (Button) findViewById(R.id.btn_sc2);
        btnBattletag = (Button) findViewById(R.id.btn_battletag);

        tvResult = (TextView) findViewById(R.id.tv_rest_result);

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
        Call<ResponseBody> call = null;
        try {
            switch (option) {
                case REST_WOW:
                    call = requestApiInterface.getWowCharacters(bnOAuth2Helper.getAccessToken());
                    break;
                case REST_SC2:
                    call = requestApiInterface.getSC2Profile(bnOAuth2Helper.getAccessToken());
                    break;
                case REST_BATTLETAG:
                    call = requestApiInterface.getBattlenetProfile(bnOAuth2Helper.getAccessToken());
                    break;
            }

            if (null != call) {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                tvResult.setText(response.body().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.getLocalizedMessage();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
