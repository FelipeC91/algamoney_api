package com.myportifolio.algamoneyapi.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.Serializable;
import java.util.ArrayList;


@AllArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private static final String PERMISSION_CLAIM_NAME = "usrRoles";


    public PermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("custom permission evaluator");
        System.out.println("----------------------------------------");
        if ((authentication == null)  || !(permission instanceof String)){
            return false;
        }


        return hasUserRole(authentication, (String) permission);

    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("custom permission evaluator");
        System.out.println("----------------------------------------");
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasUserRole(authentication, (String) permission);
    }

    private boolean hasUserRole(Authentication auth,  String permission) {
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("custom permission evaluator");
        System.out.println("----------------------------------------");

        var jwt = (Jwt) auth.getPrincipal();

        ArrayList<String> userRolesList = jwt.getClaim(PERMISSION_CLAIM_NAME);

        return userRolesList.contains(permission);
    }
}
