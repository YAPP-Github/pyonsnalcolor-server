package com.pyonsnalcolor.member.oauth.apple.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ApplePublicKeysDto {

    private List<ApplePublicKeyDto> keys;

    public ApplePublicKeyDto getApplePublicKeyMatchesAlgAndKid(String alg, String kid) {
        return keys
                .stream()
                .filter(k -> k.getAlg().equals(alg) && k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자의 identity token에 맞는 Apple 공개키가 존재하지 않습니다.")
                );
    }
}