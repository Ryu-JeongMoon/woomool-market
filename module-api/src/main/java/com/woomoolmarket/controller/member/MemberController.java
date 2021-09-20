package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.controller.member.model.MemberCollectionModel;
import com.woomoolmarket.controller.member.model.MemberResponseModel;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.service.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    public ResponseEntity<MemberCollectionModel> getMembers() {
        return ResponseEntity.ok(new MemberCollectionModel(memberService.findAllActiveMembers()));
    }

    @PostMapping
    public ResponseEntity joinMember(
        @Validated @RequestBody SignUpMemberRequest signUpMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }

        MemberResponse memberResponse = memberService.findMember(memberService.join(signUpMemberRequest));

        EntityModel<MemberResponse> responseModel = EntityModel.of(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).withSelfRel());

        URI createUri = linkTo(methodOn(MemberController.class).getMember(memberResponse.getId())).toUri();

        return ResponseEntity.created(createUri).body(responseModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseModel> getMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(MemberController.class).getMember(memberResponse.getId()));

        MemberResponseModel responseModel = new MemberResponseModel(memberResponse,
            defaultLink.slash(id).withSelfRel(),
            defaultLink.slash(id).withRel("modify-member"),
            defaultLink.slash(id).withRel("leave-member")
        );

        return ResponseEntity.ok(responseModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editMemberInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyMemberRequest modifyMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }

        MemberResponse memberResponse = memberService.editInfo(id, modifyMemberRequest);

        MemberResponseModel responseModel = new MemberResponseModel(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel());

        return ResponseEntity.ok().body(responseModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.ok("delete ok!");
    }


    /* FOR ADMIN */
    @GetMapping("/admin-only/{id}")
    public ResponseEntity<MemberResponseModel> getMemberByAdmin(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        Long previousId = memberService.findPreviousId(memberResponse).getId();
        Long nextId = memberService.findNextId(memberResponse).getId();

        MemberResponseModel responseModel = new MemberResponseModel(memberResponse,
            linkTo(methodOn(MemberController.class).getMember(id)).withSelfRel(),
            linkTo(methodOn(MemberController.class).getMember(previousId)).withRel("previous-member"),
            linkTo(methodOn(MemberController.class).getMember(nextId)).withRel("next-member"),
            linkTo(methodOn(MemberController.class).getMember(id)).withRel("modify-member"));

        return ResponseEntity.ok().body((responseModel));
    }

    //    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/all")
    public ResponseEntity<CollectionModel<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(CollectionModel.of(memberService.findAllMembers()));
    }

    /* TODO 얘는 뭔가 중복스러운데 관리자를 위해 남겨두어야 할까?
     *  화면단에서 보여줘야 하는게 다르면 내비두장 */
//    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/active")
    public ResponseEntity<CollectionModel<MemberResponse>> getAllActiveMembers() {
        return ResponseEntity.ok(CollectionModel.of(memberService.findAllActiveMembers()));
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only/inactive")
    public ResponseEntity<CollectionModel<MemberResponse>> getAllInactiveMembers() {
        return ResponseEntity.ok(CollectionModel.of(memberService.findAllInactiveMembers()));
    }
}

// redis @Cacheable을 사용하면 Custom Wrapper 객체 Result로 감싸줘야 에러가 안 터지고
// _links -> links 배열 되면서 rel : href 관계가 깨진다
// redis 쓸 것인지, hateoas 쓸 것인지 결정..?
// -> 여기다가 redis를 써야하는 건지 의문이 생겼듬 쿼리 한방 나가는 정도면 안 쓰는 게 나을까

// MemberResponseModel 반환하면 문제 없듬, 캐시도 안 쓰는데 LinkedHashMap 요놈은 뭘까?
// EntityModel -> java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.woomoolmarket.service.member.dto.response.MemberResponse