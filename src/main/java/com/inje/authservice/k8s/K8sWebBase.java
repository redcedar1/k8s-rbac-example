package com.inje.authservice.k8s;

import jakarta.servlet.http.HttpServletRequest;

public abstract class K8sWebBase {

    protected String extractToken(HttpServletRequest req) {
        String authz = req.getHeader("Authorization");
        if (authz == null || !authz.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization: Bearer <JWT> 헤더가 필요합니다.");
        }
        return authz.substring("Bearer ".length());
    }
}