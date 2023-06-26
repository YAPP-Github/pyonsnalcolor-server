package com.pyonsnalcolor.member.auth.oauth.apple.dto;

import lombok.Getter;

@Getter
public class ApplePublicKeyDto {

    private String alg;

    private String kid;

    private String kty;

    private String use;

    private String n;

    private String e;
}