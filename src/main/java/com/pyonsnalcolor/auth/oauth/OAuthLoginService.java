package com.pyonsnalcolor.auth.oauth;

import com.pyonsnalcolor.auth.dto.LoginRequestDto;
import com.pyonsnalcolor.auth.enumtype.OAuthType;
import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.OAUTH_UNSUPPORTED;

@Component
public class OAuthLoginService {

    private final List<OAuthClient> oAuthClients;

    private OAuthLoginService(List<OAuthClient> oAuthClients) {
        this.oAuthClients = oAuthClients;
    }

    public OAuthClient getOAuthLoginClient(LoginRequestDto loginRequestDto) {
        String oAuthTypeString = loginRequestDto.getOauthType().toUpperCase();
        OAuthType oAuthType = getOAuthType(oAuthTypeString);

        return this.oAuthClients.stream()
                .filter(o-> o.oAuthType() == oAuthType)
                .findFirst()
                .orElseThrow(() -> new PyonsnalcolorAuthException(OAUTH_UNSUPPORTED));
    }

    private static OAuthType getOAuthType(String oAuthTypeString) {

        return Arrays.stream(OAuthType.values())
                .filter(o -> o.name().equals(oAuthTypeString))
                .findFirst()
                .orElseThrow(() -> new PyonsnalcolorAuthException(OAUTH_UNSUPPORTED));
    }
}