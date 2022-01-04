package com.woomoolmarket.domain.member.query;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.DataJpaTestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.helper.MemberTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class MemberQueryResponseTest extends DataJpaTestConfig {

  private Member member;

  @BeforeEach
  void setUp() {
    member = MemberTestHelper.createUser();
    memberRepository.save(member);
  }

  @AfterEach
  void tearDown() {

  }

  @Test
  @DisplayName("member -> memberQueryResponse of 변환 성공")
  void conversionOf() {
    MemberQueryResponse memberQueryResponse = MemberQueryResponse.of(member);

    assertThat(memberQueryResponse.getId()).isEqualTo(member.getId());
    assertThat(memberQueryResponse.getEmail()).isEqualTo(member.getEmail());
    assertThat(memberQueryResponse.getPhone()).isEqualTo(member.getPhone());
    assertThat(memberQueryResponse.getStatus()).isEqualTo(member.getStatus());
    assertThat(memberQueryResponse.getLicense()).isEqualTo(member.getLicense());
    assertThat(memberQueryResponse.getAddress()).isEqualTo(member.getAddress());
    assertThat(memberQueryResponse.getNickname()).isEqualTo(member.getNickname());
    assertThat(memberQueryResponse.getAuthority()).isEqualTo(member.getAuthority());
    assertThat(memberQueryResponse.getAuthProvider()).isEqualTo(member.getAuthProvider());
    assertThat(memberQueryResponse.getProfileImage()).isEqualTo(member.getProfileImage());
    assertThat(memberQueryResponse.getLeaveDateTime()).isEqualTo(member.getLeaveDateTime());
    assertThat(memberQueryResponse.getCreatedDateTime()).isEqualTo(member.getCreatedDateTime());
    assertThat(memberQueryResponse.getLastModifiedDateTime()).isEqualTo(member.getLastModifiedDateTime());
  }

  @Test
  @DisplayName("member -> memberQueryResponse from 변환 성공")
  void conversionFrom() {
    MemberQueryResponse memberQueryResponse = MemberQueryResponse.from(member.getEmail());

    assertThat(memberQueryResponse.getEmail()).isEqualTo(member.getEmail());
  }

  @Test
  @DisplayName("EMAIL 검색 성공")
  void searchForAdminBy() {
    MemberSearchCondition condition = MemberSearchCondition.builder()
      .email(MEMBER_EMAIL)
      .build();
    Page<MemberQueryResponse> queryResponsePage = memberRepository.searchForAdminBy(condition, Pageable.ofSize(10));

    assertThat(queryResponsePage.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("EMAIL 검색 - 잘못된 이메일 결과 없음")
  void searchForAdminByNoResult() {
    MemberSearchCondition condition = MemberSearchCondition.builder()
      .email(MEMBER_EMAIL + 1)
      .build();
    Page<MemberQueryResponse> queryResponsePage = memberRepository.searchForAdminBy(condition, Pageable.ofSize(10));

    assertThat(queryResponsePage.getTotalElements()).isEqualTo(0);
  }

  @Test
  @DisplayName("toString")
  void toStringTest() {
    Page<MemberQueryResponse> page = memberRepository.searchForAdminBy(new MemberSearchCondition(), Pageable.ofSize(10));
    MemberQueryResponse memberQueryResponse = page.getContent().get(0);
    System.out.println("memberQueryResponse = " + memberQueryResponse);
  }
}

/*
TODO
네이밍 변경이 필요할 것 같다
from / of 뭔 차이여?
overloading 하는게 나을 것인가
*/
