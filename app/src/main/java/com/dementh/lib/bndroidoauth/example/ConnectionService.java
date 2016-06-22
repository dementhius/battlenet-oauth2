package com.dementh.lib.bndroidoauth.example;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ConnectionService {

    private static final String BASE_URL = "https://zone.api.battle.net/";

    private static RequestApiInterface requestApiInterface;

    public static RequestApiInterface getRequestApiInterface(final String zone) {
        if (null == requestApiInterface) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response;
                }
            }).build();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL.replace("zone", zone))
                    .client(okHttpClient)
                    .build();
            requestApiInterface = client.create(RequestApiInterface.class);
        }

        return requestApiInterface;
    }

    public interface RequestApiInterface {
        // CHARACTERS DATA
        @GET("/wow/user/characters")
        Call<ResponseBody> getWowCharacters(@Query("access_token") String token);

        // USER PROFILE DATA
        @GET("/sc2/profile/user")
        Call<ResponseBody> getSC2Profile(@Query("access_token") String token);

        // BATTLENET PROFILE
        @GET("/account/user")
        Call<ResponseBody> getBattlenetProfile(@Query("access_token") String token);
    }


}
