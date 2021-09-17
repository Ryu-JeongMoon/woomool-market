package com.woomoolmarket.controller.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.controller.member.model.MemberResponseModel;
import com.woomoolmarket.controller.wrapper.Result;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.service.MemberService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
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
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final static WebMvcLinkBuilder DEFAULTURI = linkTo(MemberController.class);
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Result<List<MemberResponse>>> getMembers(HttpServletRequest request) {
        log.warn("http2 check ??!! -> {}", request.getProtocol());
        return ResponseEntity.ok(new Result<>(memberService.findAllActiveMembers()));
    }

    @PostMapping
    public ResponseEntity<Result<EntityModel>> joinMember(
        @Validated @RequestBody SignUpMemberRequest signUpMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Result(bindingResult.getFieldErrors()));
        }

        MemberResponse memberResponse = memberService.findMember(memberService.join(signUpMemberRequest));

        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(DEFAULTURI.withRel("modify-member"), DEFAULTURI.withRel("leave-member"));
        URI createdUri = DEFAULTURI.slash(memberResponse.getId()).withSelfRel().toUri();
        return ResponseEntity.created(createdUri).body(new Result<>(memberResponseModel));
    }

    /**
     * TODO 고민해봅시당
     * 어떤 자료를 보여줄 것인고?
     * 수정 페이지
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<EntityModel>> getMember(@PathVariable Long id) {

        MemberResponse memberResponse = memberService.findMember(id);
        EntityModel<MemberResponse> memberResponseEntityModel = new MemberResponseModel(memberResponse);
        memberResponseEntityModel.add(
            DEFAULTURI.slash(id).withRel("modify-member"),
            DEFAULTURI.slash(id).withRel("leave-member"));

        return ResponseEntity.ok(new Result<>(memberResponseEntityModel));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Result<EntityModel>> editMemberInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyMemberRequest modifyMemberRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new Result(bindingResult.getFieldErrors()));
        }

        MemberResponse memberResponse = memberService.editInfo(id, modifyMemberRequest);
        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(DEFAULTURI.withRel("delete"));
        URI createdUri = DEFAULTURI.slash(id).toUri();

        return ResponseEntity.created(createdUri).body(new Result<>(memberResponseModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity leaveMember(@PathVariable Long id) {
        memberService.leaveSoftly(id);
        return ResponseEntity.ok("ok");
    }


    /* FOR ADMIN */
    @GetMapping("/admin-only/{id}")
    public ResponseEntity<Result<EntityModel>> getMemberByAdmin(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);

        Long previousId = memberService.findPreviousId(memberResponse).getId();
        Long nextId = memberService.findNextId(memberResponse).getId();

        MemberResponseModel memberResponseModel = new MemberResponseModel(memberResponse);
        memberResponseModel.add(DEFAULTURI.slash(previousId).withRel("previous-member"));
        memberResponseModel.add(
            DEFAULTURI.slash(nextId).withRel("next-member"),
            DEFAULTURI.slash(id).withRel("modify-member"),
            DEFAULTURI.slash(id).withRel("leave-member"));

        return ResponseEntity.ok().body((new Result<>(memberResponseModel)));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/all")
    public ResponseEntity<Result<List<MemberResponse>>> getAllMembers() {
        return ResponseEntity.ok(new Result<>(memberService.findAllMembers()));
    }

    /* TODO 얘는 뭔가 중복스러운데 관리자를 위해 남겨두어야 할까?
     *  화면단에서 보여줘야 하는게 다르면 내비두장 */
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/active")
    public ResponseEntity<Result<List<MemberResponse>>> getAllActiveMembers() {
        return ResponseEntity.ok(new Result<>(memberService.findAllActiveMembers()));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin-only/inactive")
    public ResponseEntity<Result<List<MemberResponse>>> getAllInactiveMembers() {
        return ResponseEntity.ok(new Result<>(memberService.findAllInactiveMembers()));
    }
}