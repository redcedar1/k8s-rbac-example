package com.inje.authservice.k8s;

import com.inje.authservice.k8s.K8sBaseService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigMapsService extends K8sBaseService {

    public V1ConfigMap applyConfigMap(String ns, String name, Map<String, String> data,
                                      String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1ConfigMap body = new V1ConfigMap()
                .metadata(new V1ObjectMeta().name(name).namespace(ns))
                .data(data);

        try {
            V1ConfigMap exists = api.readNamespacedConfigMap(name, ns, null);
            body.getMetadata().setResourceVersion(exists.getMetadata().getResourceVersion());
            return api.replaceNamespacedConfigMap(name, ns, body, null, null, null, null);
        } catch (ApiException ae) {
            if (ae.getCode() == 404) {
                return api.createNamespacedConfigMap(ns, body, null, null, null, null);
            }
            throw ae;
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }

    public void deleteConfigMap(String ns, String name, String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);
        try {
            api.deleteNamespacedConfigMap(name, ns, null, null, null, null, null, null);
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }
}