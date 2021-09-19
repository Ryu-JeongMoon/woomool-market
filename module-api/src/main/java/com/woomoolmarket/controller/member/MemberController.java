package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.controller.member.model.MemberResponseModel;
import com.woomoolmarket.controller.wrapper.Result;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.service.MemberService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.hal.HalLinkRelation;
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
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    @PostMapping
    public ResponseEntity joinMember(
        @Validated @RequestBody SignUpMemberRequest signUpMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }

        MemberResponse memberResponse = memberService.findMember(memberService.join(signUpMemberRequest));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse);
        URI createdUri = linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).toUri();
        return ResponseEntity.created(createdUri).body(responseModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<EntityModel<MemberResponse>>> getMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel("modify-member"),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel("leave-member")
        );

        return ResponseEntity.ok(Result.of(responseModel));
    }

    @PatchMapping("/{id}")
    public ResponseEntity editMemberInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyMemberRequest modifyMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }

        MemberResponse memberResponse = memberService.editInfo(id, modifyMemberRequest);
        MemberResponseModel responseModel = new MemberResponseModel(memberResponse);
        URI createdUri = linkTo(methodOn(MemberController.class).getMember(id)).toUri();

        return ResponseEntity.created(createdUri).body(responseModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.ok("delete ok!");
    }


    /* FOR ADMIN */
    @GetMapping("/admin-only/{id}")
    public ResponseEntity<Result<EntityModel>> getMemberByAdmin(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        Long previousId = memberService.findPreviousId(memberResponse).getId();
        Long nextId = memberService.findNextId(memberResponse).getId();

        EntityModel<MemberResponse> responseEntityModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getMember(previousId)).withRel("previous-member"),
            linkTo(methodOn(MemberController.class).getMember(nextId)).withRel("next-member"),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel("modify-member"));

        return ResponseEntity.ok().body((new Result<>(responseEntityModel)));
    }

    //    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/all")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    /* TODO 얘는 뭔가 중복스러운데 관리자를 위해 남겨두어야 할까?
     *  화면단에서 보여줘야 하는게 다르면 내비두장 */
//    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/active")
    public ResponseEntity<List<MemberResponse>> getAllActiveMembers() {
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only/inactive")
    public ResponseEntity<List<MemberResponse>> getAllInactiveMembers() {
        return ResponseEntity.ok(memberService.findAllInactiveMembers());
    }
}

// redis @Cacheable을 사용하면 Custom Wrapper 객체 Result로 감싸줘야 에러가 안 터지고
// _links -> links 배열 되면서 rel : href 관계가 깨진다
// redis 쓸 것인지, hateoas 쓸 것인지 결정..?