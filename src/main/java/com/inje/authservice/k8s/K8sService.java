package com.inje.authservice.k8s;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class K8sService {

    public V1Pod createPod(String ns, String name, String image, Map<String, String> labels, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1ObjectMeta meta = new V1ObjectMeta().name(name).namespace(ns);
        if (labels != null && !labels.isEmpty()) meta.setLabels(labels);

        V1PodSpec spec = new V1PodSpec()
                .addContainersItem(new V1Container()
                        .name("app")
                        .image(image)
                        .addPortsItem(new V1ContainerPort().name("http").containerPort(80)));

        V1Pod body = new V1Pod().metadata(meta).spec(spec);

        try {
            return api.createNamespacedPod(ns, body, null, null, null, null);
        } catch (ApiException ae) {
            throw ae;
        }
    }

    public void deletePod(String ns, String name, Integer graceSeconds, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1DeleteOptions opts = new V1DeleteOptions();
        if (graceSeconds != null) opts.setGracePeriodSeconds(Long.valueOf(graceSeconds));

        // PropagationPolicy: "Background" | "Foreground" | "Orphan"
        String propagationPolicy = "Background";
        api.deleteNamespacedPod(
                name, ns,
                null, null, null, null,
                propagationPolicy, opts
        );
    }

    public List<V1Pod> listPods(String ns, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);
        try {
            V1PodList list = api.listNamespacedPod(
                ns, null, null, null, null, null,
                null, null, null, null, null, null
            );
            return list.getItems();
        } catch (ApiException ae) {
            throw ae; // k8s가 준 코드/바디 그대로
        } catch (Exception e) {
            // 네트워크/파라미터 등 일반 예외는 ApiException(500)으로 래핑
            throw new ApiException(500, "{\"message\":\"client error: "+e.getClass().getSimpleName()+"\", \"detail\":\""+(e.getMessage()!=null?e.getMessage():"")+"\"}");
        }
    }

    // ConfigMap upsert (서버사이드 apply 대신 단순 upsert)
    public V1ConfigMap applyConfigMap(String ns, String name, Map<String, String> data, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1ConfigMap body = new V1ConfigMap()
                .metadata(new V1ObjectMeta().name(name).namespace(ns))
                .data(data);

        try {
            // 존재하면 replace
            V1ConfigMap exists = api.readNamespacedConfigMap(name, ns, null);
            body.getMetadata().setResourceVersion(exists.getMetadata().getResourceVersion());
            return api.replaceNamespacedConfigMap(name, ns, body, null, null, null, null);
        } catch (Exception notFound) {
            // 없으면 create
            return api.createNamespacedConfigMap(ns, body, null, null, null, null);
        }
    }

    public void deleteConfigMap(String ns, String name, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);
        api.deleteNamespacedConfigMap(name, ns, null, null, null, null, null, null);
    }

    public String podLogs(String ns, String pod, Integer tail, String bearerToken) throws Exception {
        ApiClient c = K8sClientFactory.forUserToken(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        Integer tailLines = (tail != null && tail > 0) ? tail : null;
        return api.readNamespacedPodLog(
            pod, ns,
            null,    // container
            false,   // follow
            null,    // insecureSkipTLSVerifyBackend
            null,    // limitBytes
            null,    // pretty
            null,    // previous
            null,    // sinceSeconds
            tailLines,
            null     // timestamps
        );
    }
}