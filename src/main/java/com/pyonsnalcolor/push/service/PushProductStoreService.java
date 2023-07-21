package com.pyonsnalcolor.push.service;

import com.pyonsnalcolor.auth.MemberRepository;
import com.pyonsnalcolor.auth.Member;
import com.pyonsnalcolor.product.enumtype.ProductStoreType;
import com.pyonsnalcolor.push.PushProductStore;
import com.pyonsnalcolor.push.dto.PushProductStoreRequestDto;
import com.pyonsnalcolor.push.dto.PushProductStoreResponseDto;
import com.pyonsnalcolor.push.repository.PushProductStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PushProductStoreService {

    private final PushProductStoreRepository pushProductStoreRepository;
    private final MemberRepository memberRepository;

    public List<PushProductStoreResponseDto> getPushProductStores (Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
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
            Long memberId,
            PushProductStoreRequestDto pushProductStoreRequestDtos)
    {
        Member member = memberRepository.getReferenceById(memberId);
        updateSubscribedStatus(member, pushProductStoreRequestDtos, true);
    }

    public void unsubscribePushProductStores (
            Long memberId,
            PushProductStoreRequestDto pushProductStoreRequestDtos)
    {
        Member member = memberRepository.getReferenceById(memberId);
        updateSubscribedStatus(member, pushProductStoreRequestDtos, false);
    }

    private void updateSubscribedStatus(
            Member member,
            PushProductStoreRequestDto pushProductStoreRequestDtos,
            boolean updatedSubscribedStatus)
    {
        pushProductStoreRequestDtos.getProductStores().stream()
                .map(pushProductStoreString -> {
                    PushProductStore pushProductStore = pushProductStoreRepository
                            .findByMemberAndProductStoreType(member, ProductStoreType.valueOf(pushProductStoreString));
                    pushProductStore.updateSubscribedStatus(updatedSubscribedStatus);
                    return pushProductStore;
                })
                .forEach(pushProductStoreRepository::save);
    }
}
