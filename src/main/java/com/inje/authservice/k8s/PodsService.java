package com.inje.authservice.k8s;

import com.inje.authservice.k8s.K8sBaseService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PodsService extends K8sBaseService {

    public V1Pod createPod(String ns, String name, String image,
                           Map<String, String> labels, String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1ObjectMeta meta = new V1ObjectMeta().name(name).namespace(ns);
        if (labels != null && !labels.isEmpty()) meta.setLabels(labels);

        V1Container container = new V1Container()
                .name("app")
                .image(image)
                .addPortsItem(new V1ContainerPort().name("http").containerPort(80));

        V1Pod body = new V1Pod()
                .metadata(meta)
                .spec(new V1PodSpec().addContainersItem(container));

        try {
            return api.createNamespacedPod(ns, body, null, null, null, null);
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }

    public void deletePod(String ns, String name, String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        V1DeleteOptions opts = new V1DeleteOptions();

        try {
            api.deleteNamespacedPod(
                name, ns,
                null, null, null, null,
                "Background",
                opts
            );
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }

    public List<V1Pod> listPods(String ns, String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);
        try {
            V1PodList list = api.listNamespacedPod(
                    ns, null, null, null, null, null,
                    null, null, null, null, null, null
            );
            return list.getItems();
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }

    public String podLogs(String ns, String pod, Integer tail, String bearerToken) throws Exception {
        ApiClient c = clientFor(bearerToken);
        CoreV1Api api = new CoreV1Api(c);

        Integer tailLines = (tail != null && tail > 0) ? tail : null;
        try {
            return api.readNamespacedPodLog(
                    pod, ns,
                    null, false, null, null, null,
                    null, null, tailLines, null
            );
        } catch (Exception e) {
            throw wrapIfNeeded(e);
        }
    }
}