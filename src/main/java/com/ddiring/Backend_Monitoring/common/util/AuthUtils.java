package com.ddiring.Backend_Monitoring.common.util;

import com.ddiring.Backend_Monitoring.common.exception.ForbiddenException;

public class AuthUtils {

    public static String requireRole(String requiredRole) {
        String role = GatewayRequestHeaderUtils.getRole();
        if (!requiredRole.equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=" + requiredRole + ")");
        }
        return GatewayRequestHeaderUtils.getUserSeq(); // 필요하면 userSeq 리턴
    }

    public static String requireAdmin() {
        return requireRole("ADMIN");
    }

    public static String requireCreator() {
        return requireRole("CREATOR");
    }

    public static String requireUser() {
        return requireRole("USER");
    }
}

