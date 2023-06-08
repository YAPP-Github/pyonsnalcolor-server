package com.pyonsnalcolor.member.entity.enumtype;

public enum LoginType {
    APPLE("apple-"), KAKAO("kakao-");

    private String header;

    LoginType(String header) {
        this.header = header;
    }

    public String addLoginTypeHeaderWithEmail(String email) {
        return this.header + email;
    }
}
