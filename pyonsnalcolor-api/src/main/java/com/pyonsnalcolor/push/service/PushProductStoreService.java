package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.auth.AuthUserDetails;
import com.pyonsnalcolor.member.Member;
import com.pyonsnalcolor.product.enumtype.ProductStoreType;
import com.pyonsnalcolor.push.PushProductStore;
import com.pyonsnalcolor.push.dto.PushProductStoreRequestDto;
import com.pyonsnalcolor.push.dto.PushProductStoreResponseDto;
import com.pyonsnalcolor.push.repository.PushProductStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PushProductStoreService {

    private final PushProductStoreRepository pushProductStoreRepository;

    public void createPushProductStores (Member member) {
        List<PushProductStore> pushProductStores = Arrays.stream(ProductStoreType.values())
                .map( i -> PushProductStore.builder()
                        .productStoreType(i)
                        .member(member)
                        .isSubscribed(true)
                        .updatedTime(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        pushProductStoreRepository.saveAll(pushProductStores);
    }

    public List<PushProductStoreResponseDto> getPushProductStores (AuthUserDetails authUserDetails) {
        Member member = authUserDetails.getMember();
        List<PushProductStore> pushProductStores = pushProductStoreRepository.findByMember(member);

        return pushProductStores.stream()
                .map(p -> {
                    return PushProductStoreResponseDto.builder()
                            .productStore(p.getProductStoreType().name())
                            .isSubscribed(p.isSubscribed())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void subscribePushProductStores (
            AuthUserDetails authUserDetails,
            PushProductStoreRequestDto pushProductStoreRequestDtos)
    {
        Member member = authUserDetails.getMember();
        updateSubscribedStatus(member, pushProductStoreRequestDtos, true);
    }

    public void unsubscribePushProductStores (
            AuthUserDetails authUserDetails,
            PushProductStoreRequestDto pushProductStoreRequestDtos)
    {
        Member member = authUserDetails.getMember();
        updateSubscribedStatus(member, pushProductStoreRequestDtos, false);
    }

    private void updateSubscribedStatus(
            Member member,
            PushProductStoreRequestDto pushProductStoreRequestDtos,
            boolean updatedSubscribedStatus)
    {
        pushProductStoreRequestDtos.getPushProductStores().stream()
                .map(pushProductStoreString -> {
                    PushProductStore pushProductStore = pushProductStoreRepository
                            .findByMemberAndProductStoreType(member, ProductStoreType.valueOf(pushProductStoreString));
                    pushProductStore.updateSubscribedStatus(updatedSubscribedStatus);
                    return pushProductStore;
                })
                .forEach(pushProductStoreRepository::save);
    }
}