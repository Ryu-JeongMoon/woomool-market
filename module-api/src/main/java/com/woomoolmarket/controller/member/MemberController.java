package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.annotation.LogExecutionTime;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.util.PageUtil;
import com.woomoolmarket.util.constant.MemberConstants;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class MemberController {

    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberResponse> assembler;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<MemberResponse>> getMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMemberById(id);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(MemberController.class).getMember(id));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            defaultLink.withSelfRel(),
            defaultLink.withRel(MemberConstants.MODIFY),
            defaultLink.withRel(MemberConstants.LEAVE)
        );

        return ResponseEntity.ok(responseModel);
    }

    @PostMapping
    public ResponseEntity joinMember(
        @Validated @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        MemberResponse memberResponse = memberService.joinAsMember(signUpRequest);
        EntityModel<MemberResponse> memberModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(memberModel);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity editMemberInfo(@PathVariable Long id, @Validated @RequestBody ModifyRequest modifyRequest,
        BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        URI createdUri = linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel().toUri();
        memberService.editMemberInfo(id, modifyRequest);
        return ResponseEntity.created(createdUri).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> restoreMember(@PathVariable Long id) {
        memberService.restore(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /* FOR ADMIN */
    /* TODO Query 3방 해결하세요 */
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<MemberResponse>> getOneForAdmin(@PathVariable Long id) {

        MemberResponse memberResponse = memberService.findMemberById(id);
        Long previousId = memberService.findPreviousId(id);
        Long nextId = memberService.findNextId(id);

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getMember(previousId)).withRel(MemberConstants.PREVIOUS_MEMBER),
            linkTo(methodOn(MemberController.class).getMember(nextId)).withRel(MemberConstants.NEXT_MEMBER),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel(MemberConstants.MODIFY));

        return ResponseEntity.ok().body(responseModel);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getListBySearchConditionForAdmin(
        MemberSearchCondition condition, @PageableDefault Pageable pageable) {

        List<MemberResponse> memberResponses = memberService.getListBySearchConditionForAdmin(condition);
        Page<MemberResponse> responsePage = PageUtil.toPage(memberResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }
}