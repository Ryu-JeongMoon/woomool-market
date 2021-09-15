package com.woomoolmarket.service.member.service;

import static java.util.stream.Collectors.toList;

import com.woomoolmarket.common.util.LocalDateTimeUtil;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.entity.MemberStatus;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import com.woomoolmarket.service.member.mapper.ModifyMemberRequestMapper;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberResponseMapper memberResponseMapper;
    private final SignUpMemberRequestMapper signUpRequestMapper;
    private final ModifyMemberRequestMapper modifyMemberRequestMapper;

    // TODO 앞뒤번호 찾기 -> refactoring 필요 뭔가 아쉽네
    @Cacheable(keyGenerator = "customKeyGenerator", value = "findPreviousId", unless = "#result==null")
    public MemberResponse findPreviousId(MemberResponse memberResponse) {
        return memberRepository.findAll()
            .stream()
            .parallel()
            .sorted(Comparator.comparingLong(Member::getId).reversed())
            .filter(member -> member.getId() < memberResponse.getId())
            .findFirst()
            .map(memberResponseMapper::toDto)
            .orElseGet(() -> memberResponse);
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findNextId", unless = "#result==null")
    public MemberResponse findNextId(MemberResponse memberResponse) {
        return memberRepository.findAll()
            .stream()
            .filter(member -> member.getId() > memberResponse.getId())
            .findFirst()
            .map(memberResponseMapper::toDto)
            .orElseGet(() -> memberResponse);
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findNextIdByLong", unless = "#result==null")
    public Optional<Long> findPreviousId(Long id) {
        return memberRepository.findPreviousId(id);
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findNextIdByLong", unless = "#result==null")
    public Optional<Long> findNextId(Long id) {
        return memberRepository.findNextId(id);
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findMember", unless = "#result==null")
    public MemberResponse findMember(Long id) {
        return memberResponseMapper.toDto(memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다")));
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllMembers", unless = "#result==null")
    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll()
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(toList());
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllInactiveMembers", unless = "#result==null")
    public List<MemberResponse> findAllInactiveMembers() {
        return memberRepository.findAll()
            .stream()
            .filter(member -> member.getLeaveDate() != null)
            .map(memberResponseMapper::toDto)
            .collect(toList());
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllActiveMembers", unless = "#result==null")
    public List<MemberResponse> findAllActiveMembers() {
        return memberRepository.findAll()
            .stream()
            .filter(member -> member.getLeaveDate() == null)
            .map(memberResponseMapper::toDto)
            .collect(toList());
    }

    /* 얘가 필요한감? */
    public Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다"));
    }

    @Transactional
    public Long join(SignUpMemberRequest signUpRequest) {

        Member member = signUpRequestMapper.toEntity(signUpRequest);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        member.registerAuthority(Authority.ROLE_USER);

        log.info("passwordEncoder = {}", passwordEncoder.getClass());
        return memberRepository.save(member).getId();
    }

    @Transactional
    public MemberResponse editInfo(Long id, ModifyMemberRequest modifyMemberRequest) {
        return memberResponseMapper.toDto(memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다"))
            .editMemberInfo(modifyMemberRequestMapper.toEntity(modifyMemberRequest)));
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "findMember")
    @Transactional
    public void leaveSoftly(Long id) {
        memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다"))
            .leave(MemberStatus.INACTIVE, LocalDateTime.now());
    }

    /* TODO Batch Job 으로 돌릴 것 */
    @Transactional
    public void leaveHardly() {
        memberRepository.findAll()
            .stream()
            .parallel()
            .filter(member -> LocalDateTimeUtil.compareMonth(LocalDateTime.now(), member.getLeaveDate()) >= 6)
            .forEach(member -> memberRepository.delete(member));
    }


}

/**
 * data 가공의 기준을 어디까지 잡아야 할까 service 에서는 무슨 일까지 해도 되는 걸까 화면 단에 맞춘 로직까지 넣어도 되는건가
 */