package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String profileImage;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes,
        String nameAttributeKey, String name,
        String email, String picture) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = name;
        this.email = email;
        this.profileImage = picture;
    }

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName,
        Map<String, Object> attributes) {

        switch (registrationId) {
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
            case "naver":
                return ofNaver(userNameAttributeName, attributes);
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "github":
                return ofGithub(userNameAttributeName, attributes);
            case "facebook":
                return ofFacebook(userNameAttributeName, attributes);
            default:
                return null;
        }
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuth2Attributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("avatar_url"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuth2Attributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        /* naver는 user_name_attribute -> response 로 받아옴, 다른 곳들은 id */
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .picture((String) response.get("profile_image"))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        /** 주의 */
         /* email -> kakaoAccount에 있고, nickname & profile_image_url -> kakaoProfile에 있음 */

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .picture((String) kakaoProfile.get("profile_image_url"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public Member toEntity() {
        return Member.builder()
            .nickname(nickname)
            .email(email)
            .profileImage(profileImage)
            .authority(Authority.ROLE_USER)
            .build();
    }
}
