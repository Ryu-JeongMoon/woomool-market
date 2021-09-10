package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.controller.member.model.MemberResponseModel;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.service.MemberService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping(value = "/api/members",
    produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    @PostMapping
    public ResponseEntity joinMember(@RequestBody @Validated SignUpMemberRequest signUpMemberRequest,
        BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(bindingResult.getAllErrors());
        }

        MemberResponse memberResponse = memberService.findMember(memberService.join(signUpMemberRequest));

        URI createdUri = linkTo(MemberController.class).slash(memberResponse.getId()).toUri();
        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(linkTo(MemberController.class).withRel("modify-member"));
        memberResponseModel.add(linkTo(MemberController.class).withRel("leave-member"));

        return ResponseEntity.created(createdUri)
            .body(memberResponseModel);
    }

    /**
     * TODO 고민해봅시당
     * 어떤 자료를 보여줄 것인고?
     * 수정 페이지
     */
    @GetMapping("/{id}")
    public ResponseEntity getMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        URI defaultURI = linkTo(MemberController.class).slash(memberResponse.getId())
            .toUri();
        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(linkTo(MemberController.class).slash(memberResponse.getId()).withRel("modify-member"));
        memberResponseModel.add(linkTo(MemberController.class).slash(memberResponse.getId()).withRel("leave-member"));

        return ResponseEntity.ok()
            .location(defaultURI)
            .body(memberResponseModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editMemberInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyMemberRequest modifyMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(bindingResult.getAllErrors());
        }

        MemberResponse memberResponse = memberService.editInfo(id, modifyMemberRequest);

        WebMvcLinkBuilder builder = linkTo(MemberController.class).slash(memberResponse.getId());
        URI createdURI = builder.toUri();
        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(linkTo(MemberController.class).withRel("delete"));

        return ResponseEntity.created(createdURI)
            .body(memberResponseModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.ok("ok");
    }


    /* FOR ADMIN */
    @GetMapping("/admin-only/{id}")
    public ResponseEntity getMemberByAdmin(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        Long previousId = memberService.findPreviousId(memberResponse)
            .getId();
        Long nextId = memberService.findNextId(memberResponse)
            .getId();

        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(linkTo(MemberController.class).slash(previousId).withRel("previous-member"));
        memberResponseModel.add(linkTo(MemberController.class).slash(nextId).withRel("next-member"));
        memberResponseModel.add(linkTo(MemberController.class).slash(memberResponse.getId()).withRel("modify-member"));
        memberResponseModel.add(linkTo(MemberController.class).slash(memberResponse.getId()).withRel("leave-member"));

        return ResponseEntity.ok()
            .body(memberResponseModel);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/all")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    /* TODO 얘는 뭔가 중복스러운데 관리자를 위해 남겨두어야 할까?
     *  화면단에서 보여줘야 하는게 다르면 내비두장 */
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/active")
    public ResponseEntity<List<MemberResponse>> getAllActiveMembers() {
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/inactive")
    public ResponseEntity<List<MemberResponse>> getAllInactiveMembers() {
        return ResponseEntity.ok(memberService.findAllInactiveMembers());
    }
}