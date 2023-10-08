package com.pyonsnalcolor.member.oauth;

import com.pyonsnalcolor.member.dto.LoginRequestDto;
import com.pyonsnalcolor.member.enumtype.OAuthType;

public interface OAuthClient {

    OAuthType oAuthType();

    String getEmail(LoginRequestDto loginRequestDto);
}