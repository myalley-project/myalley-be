package com.myalley.member.jwt;

public class JwtProperties {
    public static final long EXPIRATION_TIME = 1000L*2*60*60; // 2시간
    public static final long REFRESH_EXPIRATION_TIME = 1000L*30*60*60*24; // 30일

}
