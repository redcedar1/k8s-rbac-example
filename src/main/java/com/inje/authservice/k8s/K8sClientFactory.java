package com.inje.authservice.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;

public class K8sClientFactory {

    public static ApiClient forUserToken(String userJwt) throws Exception {
        // in-cluster의 host/CA는 cluster()가 알아서 세팅
        ClientBuilder cb = ClientBuilder.cluster();

        cb.setAuthentication(new AccessTokenAuthentication(userJwt));

        ApiClient client = cb.build();
        client.setConnectTimeout(30_000);
        client.setReadTimeout(30_000);
        client.setWriteTimeout(30_000);
        return client;
    }
}