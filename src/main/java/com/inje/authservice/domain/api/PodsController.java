package com.inje.authservice.k8s;

import com.inje.authservice.k8s.K8sWebBase;
import com.inje.authservice.k8s.PodsService;
import io.kubernetes.client.openapi.models.V1Pod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/k8s")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pods")
public class PodsController extends K8sWebBase {

    private final PodsService podsService;

    public PodsController(PodsService podsService) {
        this.podsService = podsService;
    }

    @GetMapping("/{ns}/pods")
    public ResponseEntity<?> listPods(@PathVariable("ns") String ns,
                                      @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        var pods = podsService.listPods(ns, token);
        var resp = pods.stream().map(p -> Map.of(
                "name", p.getMetadata().getName(),
                "phase", p.getStatus() != null ? p.getStatus().getPhase() : ""
        )).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{ns}/pods/{name}/logs")
    public ResponseEntity<?> podLogs(@PathVariable("ns") String ns,
                                     @PathVariable("name") String pod,
                                     @RequestParam(value = "tail", required = false) Integer tail,
                                     @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        String logs = podsService.podLogs(ns, pod, tail, token);
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/{ns}/pods/{name}")
    public ResponseEntity<?> createPod(@PathVariable("ns") String ns,
                                       @PathVariable("name") String name,
                                       @RequestParam("image") String image,
                                       @RequestBody(required = false) Map<String, String> labels,
                                       @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        V1Pod pod = podsService.createPod(ns, name, image, labels, token);
        return ResponseEntity.ok(Map.of(
                "name", pod.getMetadata().getName(),
                "ns", pod.getMetadata().getNamespace(),
                "phase", pod.getStatus() != null ? pod.getStatus().getPhase() : ""
        ));
    }

    @DeleteMapping("/{ns}/pods/{name}")
    public ResponseEntity<?> deletePod(@PathVariable String ns,
                                       @PathVariable String name,
                                       @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        podsService.deletePod(ns, name, token);
        return ResponseEntity.noContent().build();
    }
}