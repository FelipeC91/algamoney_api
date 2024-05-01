package com.myportifolio.algamoneyapi.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private static final String PERMISSION_CLAIM_NAME = "usrRoles";


    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    public boolean hasUserRole(Authentication auth, String permission) {
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("custom permission evaluator");
        System.out.println("----------------------------------------");

        var jwt = (Jwt) auth.getPrincipal();

        ArrayList<String> userRolesList = jwt.getClaim(PERMISSION_CLAIM_NAME);

        return userRolesList.contains(permission);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}

