package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.security.exception.OAuth2AuthenticationProcessingException;
import com.woomoolmarket.security.oauth2.user.OAuth2UserInfo;
import com.woomoolmarket.security.oauth2.user.OAuth2UserInfoFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User)
        throws OAuth2AuthenticationProcessingException {



        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Member> memberOptional = memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            if (!member.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {

                throw new OAuth2AuthenticationProcessingException(
                    String.format("Looks like you're signed up with %s account. Please use your %s account to login.",
                        member.getProvider(), member.getProvider()));
            }
            member = updateExistingUser(member.getId(), oAuth2UserInfo);
        } else {
            member = registerNewMember(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(member, oAuth2User.getAttributes());
    }

    private Member registerNewMember(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.builder()
            .email(oAuth2UserInfo.getEmail())
            .nickname(oAuth2UserInfo.getName())
            .profileImage(oAuth2UserInfo.getImageUrl())
            .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
            .build();

        return memberRepository.save(member);
    }

    private Member updateExistingUser(Long existingMemberId, OAuth2UserInfo oAuth2UserInfo) {
        Member result = memberRepository.findById(existingMemberId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));

        result.editNickname(oAuth2UserInfo.getName());
        result.editProfileImage(oAuth2UserInfo.getImageUrl());

        return result;
    }

}
