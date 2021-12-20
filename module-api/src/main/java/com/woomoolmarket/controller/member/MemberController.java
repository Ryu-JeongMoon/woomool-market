package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.util.constant.MemberConstants;
import java.net.URI;
import javax.validation.Valid;
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
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class MemberController {

    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberQueryResponse> assembler;

    @GetMapping("/{id}")
    @PreAuthorize("@checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<MemberResponse>> getBy(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMemberById(id);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(MemberController.class).getBy(id));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            defaultLink.withSelfRel(),
            defaultLink.withRel(MemberConstants.MODIFY),
            defaultLink.withRel(MemberConstants.LEAVE)
        );

        return ResponseEntity.ok(responseModel);
    }

    @PostMapping
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<EntityModel<MemberResponse>> join(
        @Valid @RequestBody SignupRequest signUpRequest, @Nullable MultipartFile file) {

        MemberResponse memberResponse;
        if (StringUtils.hasText(signUpRequest.getLicense())) {
            memberResponse = memberService.joinAsSeller(signUpRequest, file);
        } else {
            memberResponse = memberService.joinAsMember(signUpRequest, file);
        }

        EntityModel<MemberResponse> memberModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getBy(memberResponse.getId())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(memberModel);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> edit(@PathVariable Long id, @Valid @RequestBody ModifyRequest modifyRequest) {
        URI createdUri = linkTo(methodOn(MemberController.class).getBy(id)).withSelfRel().toUri();
        memberService.editMemberInfo(id, modifyRequest);
        return ResponseEntity.created(createdUri).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@checker.isSelf(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> leave(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        memberService.restore(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<MemberResponse>> getForAdminBy(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMemberById(id);
        Long previousId = memberService.findPreviousId(id);
        Long nextId = memberService.findNextId(id);

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getBy(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getBy(previousId)).withRel(MemberConstants.PREVIOUS_MEMBER),
            linkTo(methodOn(MemberController.class).getBy(nextId)).withRel(MemberConstants.NEXT_MEMBER),
            linkTo(methodOn(MemberController.class).getBy(id)).withRel(MemberConstants.MODIFY));

        return ResponseEntity.ok().body(responseModel);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<MemberQueryResponse>>> getPageForAdminBy(
        MemberSearchCondition condition, @PageableDefault Pageable pageable) {

        Page<MemberQueryResponse> queryResponsePage = memberService.searchForAdminBy(condition, pageable);
        return ResponseEntity.ok(assembler.toModel(queryResponsePage));
    }
}