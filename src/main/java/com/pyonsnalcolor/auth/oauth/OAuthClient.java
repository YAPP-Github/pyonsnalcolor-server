package com.pyonsnalcolor.auth.oauth;

import com.pyonsnalcolor.auth.dto.LoginRequestDto;
import com.pyonsnalcolor.auth.enumtype.OAuthType;

public interface OAuthClient {

    OAuthType oAuthType();

    String getEmail(LoginRequestDto loginRequestDto);
}