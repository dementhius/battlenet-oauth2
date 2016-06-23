package com.dementh.lib.bndroidoauth.example;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ResultActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private ConnectionService.RequestApiInterface requestApiInterface;

    private TextView textView;

    private BnOAuth2Helper bnOAuth2Helper;
    private BnOAuth2Params bnOAuth2Params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView = (TextView) findViewById(R.id.tv_result);

        // Battlenet rest calls
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final Bundle bundle =  this.getIntent().getExtras();
        bnOAuth2Params = bundle.getParcelable(BnConstants.BUNDLE_BNPARAMS);
        bnOAuth2Helper = new BnOAuth2Helper(this.prefs, bnOAuth2Params);

        requestApiInterface = ConnectionService.getRequestApiInterface(bnOAuth2Params.getZone());

        final int option = bundle.getInt("option");

        Call<ResponseBody> call = null;
        try {
            final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");
            switch (option) {
                case DestinyActivity.REST_WOW:
                    call = requestApiInterface.getWowCharacters(bnOAuth2Helper.getAccessToken());
                    break;
                case DestinyActivity.REST_SC2:
                    call = requestApiInterface.getSC2Profile(bnOAuth2Helper.getAccessToken());
                    break;
                case DestinyActivity.REST_BATTLETAG:
                    call = requestApiInterface.getBattlenetProfile(bnOAuth2Helper.getAccessToken());
                    break;
            }

            if (null != call) {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.cancel();
                        try {
                            if (null != response.body()) {
                                textView.setText(response.body().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dialog.cancel();
                        t.getLocalizedMessage();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
