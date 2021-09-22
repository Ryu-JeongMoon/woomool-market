package com.woomoolmarket.service.member.service;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.woomoolmarket.common.util.LocalDateTimeUtil;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import com.woomoolmarket.service.member.mapper.ModifyMemberRequestMapper;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final SignUpMemberRequestMapper signUpRequestMapper;
    private final ModifyMemberRequestMapper modifyMemberRequestMapper;

    // orElseGet -> else 일 때만 실행, orElse -> 무조건 실행, 성능 상 유리하니 orElseGet 씁시다
    public Long findPreviousId(Long id) {
        return memberRepository.findAll()
            .stream()
            .parallel()
            .sorted(Comparator.comparingLong(Member::getId).reversed())
            .filter(member -> member.getId() < id)
            .findFirst()
            .map(Member::getId)
            .orElseGet(() -> id);
    }

    // TODO 앞뒤 번호 찾기 -> refactoring 필요 뭔가 아쉽네
    // 앞뒤 번호 찾을 때 캐시 안 쓰면 쿼리 3방 나간다 이건 어떻게 해결해야 할까?!
    // 번호만 캐시하는 방법 없나~~~~ 페이징으로 하면 달라질라나
    public Long findNextId(Long id) {
        return memberRepository.findAll()
            .stream()
            .filter(member -> member.getId() > id)
            .findFirst()
            .map(Member::getId)
            .orElseGet(() -> id);
    }

//    public Optional<Long> findPreviousId(Long id) {
//        return memberRepository.findPreviousId(id);
//    }
//    public Optional<Long> findNextId(Long id) {
//        return memberRepository.findNextId(id);
//    }

    public MemberResponse findMember(Long id) {
        return memberResponseMapper.toDto(memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다")));
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
    public Page<MemberResponse> findAllActiveMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(memberRepository.findActiveMembers(pageRequest)
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<MemberResponse> findAllInactiveMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(memberRepository.findInactiveMembers(pageRequest)
            .stream()
            .map(memberResponseMapper::toDto)
            .collect(toList()));
    }

    /* 얘가 필요한감? */
    public Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다"));
    }

    // Authority.ROLE_USER -> DB default 값으로 해결할 수 있을거 같은데?
    @Transactional
    public Long join(SignUpMemberRequest signUpRequest) {

        Member member = signUpRequestMapper.toEntity(signUpRequest);
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        member.registerAuthority(Authority.ROLE_USER);

        log.info("passwordEncoder = {}", passwordEncoder.getClass());
        return memberRepository.save(member).getId();
    }

    // (Command) 변경을 가하는 메서드는 반환값이 없어야 할텐데, 로직 상 수정된 회원을 바로 보여주고 싶은데 ?!
    // 이런 경우에 반환값 없애면 쿼리 두번 날려야 하잖아, 일단 반환값 주고 필요할 때 고치장
    // dirty checking 에 의해 바뀐당
    @Transactional
    public MemberResponse editInfo(Long id, ModifyMemberRequest modifyMemberRequest) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));
        modifyMemberRequestMapper.updateFromDto(modifyMemberRequest, member);
        return memberResponseMapper.toDto(member);
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @Transactional
    public void leaveSoftly(Long id) {
        memberRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"))
            .leave();
    }

    /* TODO Batch Job 으로 돌릴 것 */
    // memberRepository::delete 메서드 참조로 바꿀 수 있는데 그냥 풀어놓는게 보기 편한뎅.. 익숙해지면 바꾸자
    @Transactional
    public void leaveHardly() {
        memberRepository.findAll()
            .stream()
            .parallel()
            .filter(member -> LocalDateTimeUtil.compareMonth(LocalDateTime.now(), member.getLeaveDateTime()) >= 6)
            .forEach(member -> memberRepository.delete(member));
    }
}

/**
 * @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllActiveMembers", unless = "#result==null")
 * <p>
 * -> redis <-> entity model 안 맞아.. 일단 캐시 다 걷어내고 hateoas 형식만 맞춰서 내보내자
 */

//    @Cacheable 적용 버전
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "findAllMembers", unless = "#result==null")
//    public List<MemberResponse> findAllMembers() {
//        return memberRepository.findAll()
//            .stream()
//            .map(memberResponseMapper::toDto)
//            .collect(toList());
//    }