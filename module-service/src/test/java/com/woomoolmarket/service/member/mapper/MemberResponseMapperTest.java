package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// TODO QueryDsl 의존성 때문에 @DataJpaTest 불가, 수정할 방법이 있으려나?
@Log4j2
@SpringBootTest
@Transactional
class MemberResponseMapperTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    private MemberResponseMapperImpl memberResponseMapper = new MemberResponseMapperImpl();

    @Test
    @DisplayName("createdDate 받아온다")
    void memberMapperTest() {
        Member member = Member.builder()
            .email("panda@gmail.com")
            .nickname("panda")
            .password("123456")
            .build();

        Member savedMember = memberRepository.save(member);
        LocalDateTime createdDate = savedMember.getCreatedDate();
        MemberResponse memberResponse = memberResponseMapper.toDto(savedMember);
        Assertions.assertThat(createdDate).isEqualTo(memberResponse.getCreatedDate());
    }
}