package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
            .getUserNameAttributeName();

        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = registerOrEdit(registrationId, attributes);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getAuthorityKey())),
            attributes.getAttributes(),
            attributes.getNameAttributeKey());
    }

    private Member registerOrEdit(String registrationId, OAuth2Attributes attributes) {
        if (memberRepository.findByEmail(attributes.getEmail()).isEmpty()) {
            return registerNewMember(registrationId, attributes);
        }

        return editExistingMember(attributes);
    }

    private Member registerNewMember(String registrationId, OAuth2Attributes attributes) {
        Member member = Member.builder()
            .email(attributes.getEmail())
            .nickname(attributes.getNickname())
            .profileImage(attributes.getProfileImage())
            .authority(Authority.ROLE_USER)
            .provider(AuthProvider.valueOf(registrationId.toUpperCase()))
            .build();

        return memberRepository.save(member);
    }

    private Member editExistingMember(OAuth2Attributes attributes) {
        return memberRepository.findByEmail(attributes.getEmail())
            .map(member -> member.editNicknameAndProfileImage(attributes.getNickname(), attributes.getProfileImage()))
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND));
    }
}
