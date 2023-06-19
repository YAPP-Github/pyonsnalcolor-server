package com.pyonsnalcolor.domain.member.enumtype;

import java.util.Arrays;

public enum OAuthType {
    APPLE("apple-"), KAKAO("kakao-");

    private String header;

    OAuthType(String header) {
        this.header = header;
    }

    public String addOAuthTypeHeaderWithEmail(String email) {
        return this.header + email;
    }

    public static OAuthType parseOAuthType(String oauthId) {
        String parsedHeader = oauthId.substring(0, APPLE.header.length());

        return Arrays.stream(OAuthType.values())
                .filter(o -> o.header.equals(parsedHeader))
                .findFirst()
                .get();
    }

    public static String parseEmail(String oauthId) {
        return oauthId.substring(APPLE.header.length());
    }
}