package com.woomoolmarket.helper;

import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberTestHelper {

    public static final String MEMBER_EMAIL = "panda@naver.com";
    public static final String MEMBER_PASSWORD = "123456";
    public static final String MEMBER_NICKNAME = "panda";
    public static final String MEMBER_PHONE = "01012345678";
    public static final Address MEMBER_ADDRESS = new Address("seoul", "yeonhui", "01023");

    public static final String SELLER_EMAIL = "bear@gmail.com";
    public static final String SELLER_PASSWORD = "123456";
    public static final String SELLER_NICKNAME = "bear";
    public static final String SELLER_PHONE = "01087654321";
    public static final Address SELLER_ADDRESS = new Address("incheon", "yeonhui", "45678");

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember() {
        Member member = Member.builder()
            .email(MEMBER_EMAIL)
            .nickname(MEMBER_NICKNAME)
            .authority(Authority.ROLE_USER)
            .password(passwordEncoder.encode(MEMBER_PASSWORD))
            .address(MEMBER_ADDRESS)
            .phone(MEMBER_PHONE)
            .build();
        return memberRepository.save(member);
    }

    public Member createSeller() {
        Member member = Member.builder()
            .email(SELLER_EMAIL)
            .nickname(SELLER_NICKNAME)
            .authority(Authority.ROLE_SELLER)
            .password(passwordEncoder.encode(SELLER_PASSWORD))
            .phone(SELLER_PHONE)
            .address(SELLER_ADDRESS)
            .build();
        return memberRepository.save(member);
    }
}
