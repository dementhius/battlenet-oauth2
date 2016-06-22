package com.dementh.lib.battlenet_oauth2.connections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;

import java.io.IOException;

public class BnQueryParamsAccessMethod implements Credential.AccessMethod {

    private static final String PARAM_NAME = "oauth_token";

    private static final BnQueryParamsAccessMethod instance = new BnQueryParamsAccessMethod();

    private BnQueryParamsAccessMethod() {}

    public static BnQueryParamsAccessMethod getInstance() {
        return instance;
    }

    @Override
    public void intercept(HttpRequest request, String accessToken) throws IOException {
        request.getUrl().set(PARAM_NAME, accessToken);
    }

    @Override
    public String getAccessTokenFromRequest(HttpRequest request) {
        Object param = request.getUrl().get(PARAM_NAME);
        return param == null ? null : param.toString();
    }
}
