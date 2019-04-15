package com.example.resumegeneratorbackend.security;

public class SecurityCockpit {

    public static final String SIGN_UP_URLS = "/api/**";
    public static final String SECRET ="SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 300_000; //30 seconds TOKEN EXPIRATION TIME

}
