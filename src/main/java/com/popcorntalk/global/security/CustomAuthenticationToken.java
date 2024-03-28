package com.popcorntalk.global.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 611L;// serialVersion 인터페이스를 구현해야하는데 버전올릴 때 이전에 버전에 토큰을 사용하지 못해 넣는 것(버전관리)
    private final Object principal;
    private Object credentials; //자격증명

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
