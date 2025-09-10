package com.inje.authservice.k8s;

import com.inje.authservice.k8s.K8sWebBase;
import com.inje.authservice.k8s.ConfigMapsService;
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
@Tag(name = "ConfigMaps")
public class ConfigMapsController extends K8sWebBase {

    private final ConfigMapsService configMapsService;

    public ConfigMapsController(ConfigMapsService configMapsService) {
        this.configMapsService = configMapsService;
    }

    @PostMapping("/{ns}/configmaps/{name}")
    public ResponseEntity<?> upsertConfigMap(@PathVariable("ns") String ns,
                                             @PathVariable("name") String name,
                                             @RequestBody Map<String,String> data,
                                             @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        var cm = configMapsService.applyConfigMap(ns, name, data, token);
        return ResponseEntity.ok(Map.of("name", cm.getMetadata().getName(), "ns", ns));
    }

    @DeleteMapping("/{ns}/configmaps/{name}")
    public ResponseEntity<?> deleteConfigMap(@PathVariable("ns") String ns,
                                             @PathVariable("name") String name,
                                             @Parameter(hidden = true) HttpServletRequest req) throws Exception {
        String token = extractToken(req);
        configMapsService.deleteConfigMap(ns, name, token);
        return ResponseEntity.noContent().build();
    }
}