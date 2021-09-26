package com.woomoolmarket.service.member;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import com.woomoolmarket.service.member.mapper.ModifyRequestMapper;
import com.woomoolmarket.service.member.mapper.SignUpRequestMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        return memberResponseMapper.toDto(memberRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND)));
    }

    public MemberResponse findMemberByPhone(String phone) {
        return memberResponseMapper.toDto(memberRepository.findByPhone(phone)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND)));
    }

    public MemberResponse findMemberByNickname(String nickname) {
        return memberResponseMapper.toDto(memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND)));
    }

    public Page<MemberResponse> findAllMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(memberRepository.findAll(pageRequest)
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(toList()));
    }

    // 그냥 쿼리에서 걸러서 가져오는게 나을듯?!
    // -> findAll().stream().filter() 방식에서 findActiveMembers()로 쿼리에서 걸러서 가져오는 방식으로 변경
    public Page<MemberResponse> findMembersByStatus(Status status,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(memberRepository.findMembersByStatus(status, pageRequest)
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    @Transactional
    public MemberResponse joinAsMember(SignUpRequest signUpRequest) {
        return memberResponseMapper.toDto(join(signUpRequest, Authority.ROLE_USER));
    }

    @Transactional
    public MemberResponse joinAsSeller(SignUpRequest signUpRequest) {
        return memberResponseMapper.toDto(join(signUpRequest, Authority.ROLE_SELLER));
    }

    private Member join(SignUpRequest signUpRequest, Authority authority) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException(ExceptionUtil.DUPLICATED_USER);
        }
        Member member = signUpRequestMapper.toEntity(signUpRequest);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        member.registerAuthority(authority);
        return memberRepository.save(member);
    }

    // (Command) 변경을 가하는 메서드는 반환값이 없어야 할텐데, 로직 상 수정된 회원을 바로 보여주고 싶은데 ?!
    // 이런 경우에 반환값 없애면 조회 쿼리 또 날려야 하니 일단 반환값 주고 필요할 때 고칠 것
    @Transactional
    public MemberResponse editMemberInfo(Long id, ModifyRequest modifyRequest) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND));
        modifyRequestMapper.updateFromDto(modifyRequest, member);
        return memberResponseMapper.toDto(member);
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @Transactional
    public void leaveSoftly(Long id) {
        memberRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionUtil.USER_NOT_FOUND))
            .leave();
    }
}

/**
 * @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllActiveMembers", unless = "#result==null") -> redis <-> entity
 * model 안 맞아.. 일단 캐시 다 걷어내고 hateoas 형식만 맞춰서 내보내자
 */

//    @Cacheable 적용 버전
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllMembers", unless = "#result==null")
//    public List<MemberResponse> findAllMembers() {
//        return memberRepository.findAll()
//            .stream()
//            .map(memberResponseMapper::toDto)
//            .collect(toList());
//    }