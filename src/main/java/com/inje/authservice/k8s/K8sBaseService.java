package com.inje.authservice.k8s;

import com.inje.authservice.k8s.K8sClientFactory;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;

public abstract class K8sBaseService {

    protected ApiClient clientFor(String bearerToken) throws Exception {
        return K8sClientFactory.forUserToken(bearerToken);
    }

    protected ApiException wrapIfNeeded(Exception e) {
        if (e instanceof ApiException) return (ApiException) e;
        return new ApiException(
                500,
                "{\"message\":\"client error: " + e.getClass().getSimpleName() + "\"," +
                "\"detail\":\"" + (e.getMessage() != null ? e.getMessage() : "") + "\"}"
        );
    }
}