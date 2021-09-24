package com.woomoolmarket.controller.member;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class MemberController {

    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberResponse> assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {

        Page<MemberResponse> pagedResponse = memberService.findMembersByStatus(Status.ACTIVE, pageRequest);
        return ResponseEntity.ok(assembler.toModel(pagedResponse));
    }

    @PostMapping
    public ResponseEntity joinMember(
        @Validated @RequestBody SignUpMemberRequest signUpMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        MemberResponse memberResponse = memberService.findMember(memberService.joinMember(signUpMemberRequest));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).withSelfRel());

        URI createUri = linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).toUri();

        return ResponseEntity.created(createUri).body(responseModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MemberResponse>> getMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(MemberController.class).getMember(memberResponse.getId()));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            defaultLink.withSelfRel(),
            defaultLink.withRel("modify-member"),
            defaultLink.withRel("leave-member")
        );

        return ResponseEntity.ok(responseModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editMemberInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyMemberRequest modifyMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Link createUri = linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel();

        MemberResponse memberResponse = memberService.editInfo(id, modifyMemberRequest);
        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse, createUri);

        return ResponseEntity.created(createUri.toUri()).body(responseModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    /* TODO Query 3방 해결하세요 */
    //@Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/{id}")
    public ResponseEntity<EntityModel<MemberResponse>> getMemberByAdmin(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        Long previousId = memberService.findPreviousId(id);
        Long nextId = memberService.findNextId(id);

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getMember(previousId)).withRel("previous-member"),
            linkTo(methodOn(MemberController.class).getMember(nextId)).withRel("next-member"),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel("modify-member"));

        return ResponseEntity.ok().body(responseModel);
    }

    /* 얘네 메서드 새개는 나가는 쿼리가 달라서 중복은 아닌데 형태가 중복이다 정리할 수 있나?
     * -> Member 전부 보여주는 메서드 제외, Status 조건에 따라 다른 결과 나오도록 변경함! */
    //@Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/all")
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getAllMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {

        Page<MemberResponse> pagedResponse = memberService.findAllMembers(pageRequest);
        return ResponseEntity.ok(assembler.toModel(pagedResponse));
    }

    //@Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/active")
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getAllActiveMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {

        Page<MemberResponse> pagedResponse = memberService.findMembersByStatus(Status.ACTIVE, pageRequest);
        return ResponseEntity.ok(assembler.toModel(pagedResponse));
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only/inactive")
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getAllInactiveMembers(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {

        Page<MemberResponse> pagedResponse = memberService.findMembersByStatus(Status.INACTIVE, pageRequest);
        return ResponseEntity.ok(assembler.toModel(pagedResponse));
    }
}

/*
 MemberResponseModel 반환하면 문제 없듬, 캐시도 안 쓰는데 LinkedHashMap 요놈은 뭘까?
 EntityModel ->
 java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class
 com.woomoolmarket.service.member.dto.response.MemberResponse
*/
