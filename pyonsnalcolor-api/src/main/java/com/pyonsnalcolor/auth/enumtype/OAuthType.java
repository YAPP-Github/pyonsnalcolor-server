package com.pyonsnalcolor.auth.enumtype;

public enum OAuthType {
    APPLE("apple-"), KAKAO("kakao-");

    private String header;

    OAuthType(String header) {
        this.header = header;
    }

    public String addOAuthTypeHeaderWithEmail(String email) {
        return this.header + email;
    }
}
