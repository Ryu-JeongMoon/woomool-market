package com.woomoolmarket.service.member;

import com.woomoolmarket.common.constant.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.image.ImageProcessor;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import com.woomoolmarket.service.member.mapper.ModifyRequestMapper;
import com.woomoolmarket.service.member.mapper.SignupRequestMapper;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ImageProcessor imageProcessor;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SignupRequestMapper signupRequestMapper;
    private final ModifyRequestMapper modifyRequestMapper;
    private final MemberResponseMapper memberResponseMapper;

    @Transactional(readOnly = true)
    public Long findPreviousId(Long id) {
        return memberRepository.findPreviousId(id)
            .orElseGet(() -> id);
    }

    @Transactional(readOnly = true)
    public Long findNextId(Long id) {
        return memberRepository.findNextId(id)
            .orElseGet(() -> id);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberById(Long id) {
        return memberRepository.findById(id)
            .map(memberResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "membersForAdmin", allEntries = true)
    public MemberResponse joinAsMember(SignupRequest signUpRequest, MultipartFile file) {
        Member member = join(signUpRequest, Authority.ROLE_USER, file);
        return memberResponseMapper.toDto(member);
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "membersForAdmin", allEntries = true)
    public MemberResponse joinAsSeller(SignupRequest signUpRequest, MultipartFile file) {
        Member member = join(signUpRequest, Authority.ROLE_SELLER, file);
        return memberResponseMapper.toDto(member);
    }

    @Transactional
    public Member join(SignupRequest signUpRequest, Authority authority, MultipartFile file) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException(ExceptionConstants.MEMBER_EMAIL_DUPLICATED);
        }

        Image image = imageProcessor.parse(file);

        Member member = signupRequestMapper.toEntity(signUpRequest);
        member.changePassword(passwordEncoder.encode(member.getPassword()));
        member.assignAuthority(authority);
        member.addImage(image);
        return memberRepository.save(member);
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "membersForAdmin", allEntries = true)
    public void editMemberInfo(Long id, ModifyRequest modifyRequest) {
        Member member = memberRepository.findByIdAndStatus(id, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));
        modifyRequestMapper.updateFromDto(modifyRequest, member);
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "membersForAdmin", allEntries = true)
    public void leaveSoftly(Long id) {
        memberRepository.findByIdAndStatus(id, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND))
            .leave();
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "membersForAdmin", allEntries = true)
    public void restore(Long id) {
        memberRepository.findByIdAndStatus(id, Status.INACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND))
            .restore();
    }


    /* FOR ADMIN */
    @Cacheable(keyGenerator = "customKeyGenerator", value = "membersForAdmin", unless = "#result==null")
    @Transactional(readOnly = true)
    public Page<MemberQueryResponse> searchForAdminBy(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchForAdminBy(condition, pageable);
    }
}