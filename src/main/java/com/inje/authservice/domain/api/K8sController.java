package com.inje.authservice.k8s;

import io.kubernetes.client.openapi.models.V1Pod;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/k8s")
@SecurityRequirement(name = "bearerAuth") // Swagger: 이 컨트롤러는 bearerAuth 필요
public class K8sController {

    private final K8sService k8sService;

    public K8sController(K8sService k8sService) {
        this.k8sService = k8sService;
    }

    private String extractToken(HttpServletRequest req) {
        String authz = req.getHeader("Authorization");
        if (authz == null || !authz.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization: Bearer <JWT> 헤더가 필요합니다.");
        }
        return authz.substring("Bearer ".length());
    }

    @GetMapping("/{ns}/pods")
    public ResponseEntity<?> listPods(@PathVariable("ns") String ns,
                                      @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        List<V1Pod> pods = k8sService.listPods(ns, token);

        List<Map<String, Object>> resp = pods.stream()
                .map(p -> Map.<String, Object>of(
                        "name", p.getMetadata().getName(),
                        "phase", p.getStatus() != null ? p.getStatus().getPhase() : ""
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{ns}/pods/{name}/logs")
    public ResponseEntity<?> podLogs(@PathVariable("ns") String ns,
                                     @PathVariable("name") String pod,
                                     @RequestParam(value = "tail", required = false) Integer tail,
                                     @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        String logs = k8sService.podLogs(ns, pod, tail, token);
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/{ns}/configmaps/{name}")
    public ResponseEntity<?> upsertConfigMap(@PathVariable("ns") String ns,
                                             @PathVariable("name") String name,
                                             @RequestBody Map<String,String> data,
                                             @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        var cm = k8sService.applyConfigMap(ns, name, data, token);
        return ResponseEntity.ok(Map.of("name", cm.getMetadata().getName(), "ns", ns));
    }

    @DeleteMapping("/{ns}/configmaps/{name}")
    public ResponseEntity<?> deleteConfigMap(@PathVariable("ns") String ns,
                                            @PathVariable("name") String name,
                                            @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        k8sService.deleteConfigMap(ns, name, token);
        return ResponseEntity.noContent().build();
    }
}