package com.dryxtech.software.sample.access;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

public class OrganizationPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object resourceObject, Object permission) {
        if (Objects.isNull(resourceObject)){
            return false;
        }
        return hasPrivilege(auth, (Serializable) resourceObject, resourceObject.getClass().getSimpleName(), permission);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable orgId, String resourceType, Object permission) {
        return hasPrivilege(auth, orgId, resourceType, permission);
    }

    boolean hasPrivilege(Authentication auth, Serializable orgId, String resourceType, Object permission) {

        if (Objects.isNull(auth) || Objects.isNull(resourceType) || !(permission instanceof String)) {
            return false;
        }

        String perm = ((String) permission).toUpperCase();
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            String role = grantedAuth.getAuthority().trim().toUpperCase();
            if (("*".equals(perm) || role.startsWith(perm)) && role.endsWith("=" + orgId)) {
                return true;
            }
        }

        return false;
    }
}
