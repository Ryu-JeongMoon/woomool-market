package com.woomoolmarket.service.member;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import com.woomoolmarket.service.member.mapper.ModifyRequestMapper;
import com.woomoolmarket.service.member.mapper.SignUpRequestMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
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
    private final SignUpRequestMapper signUpRequestMapper;
    private final ModifyRequestMapper modifyRequestMapper;

    // 앞뒤 번호 찾을 때 캐시 안 쓰면 쿼리 3방 나간다 이건 어떻게 해결해야 할까?!
    // orElseGet -> else 일 때만 실행, orElse -> 무조건 실행, 성능 상 유리하니 orElseGet 씁시다
    public Long findPreviousId(Long id) {
        return memberRepository.findPreviousId(id)
            .orElseGet(() -> id);
    }

    public Long findNextId(Long id) {
        return memberRepository.findNextId(id)
            .orElseGet(() -> id);
    }

    public MemberResponse findMemberById(Long id) {
        return memberRepository.findById(id)
            .map(memberResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)
    public MemberResponse joinAsMember(SignUpRequest signUpRequest) {
        return memberResponseMapper.toDto(join(signUpRequest, Authority.ROLE_USER));
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)
    public MemberResponse joinAsSeller(SignUpRequest signUpRequest) {
        return memberResponseMapper.toDto(join(signUpRequest, Authority.ROLE_SELLER));
    }

    @Transactional
    public Member join(SignUpRequest signUpRequest, Authority authority) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException(ExceptionUtil.DUPLICATED_USER);
        }

        Member member = signUpRequestMapper.toEntity(signUpRequest);
        member.changePassword(passwordEncoder.encode(member.getPassword()));
        member.registerAuthority(authority);

        return memberRepository.save(member);
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)
    public void editMemberInfo(Long id, ModifyRequest modifyRequest) {
        Member member = memberRepository.findByIdAndStatus(id, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));
        modifyRequestMapper.updateFromDto(modifyRequest, member);
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)
    public void leaveSoftly(Long id) {
        memberRepository.findByIdAndStatus(id, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND))
            .leave();
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)
    public void restore(Long id) {
        memberRepository.findByIdAndStatus(id, Status.INACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND))
            .restore();
    }


    /* FOR ADMIN */
    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", unless = "#result==null")
    public List<MemberResponse> getListBySearchConditionForAdmin(MemberSearchCondition condition) {
        return memberRepository.findByConditionForAdmin(condition)
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(Collectors.toList());
    }
}

/*
앞뒤 번호 고민..
 */