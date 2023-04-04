package com.shop.controller;

import com.shop.Service.MailService;
import com.shop.Service.MemberService;
import com.shop.dto.MemberFormDto;
import com.shop.dto.FindIdDto;
import com.shop.dto.UpdateInfoDto;
import com.shop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    String confirm="";
    boolean confirmCheck=false;

    @GetMapping(value ="/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){
        if(confirmCheck == false){
            model.addAttribute("errorMessage", "인증 확인 부탁드립니다.");
            return "member/memberForm";
        }
        if(bindingResult.hasErrors()){
            return "member/memberForm"; //fields.hasErrors 여기서 에러메세지 뜸.
        }
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
            confirmCheck = false;
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "member/memberjoin";
    }
    @GetMapping(value ="/manager/new") //관리자 계정으로 가입할 때
    public String managerMemberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/managerForm";
    }

    @PostMapping(value = "/manager/new") //관리자 계정 가입 정보 DB에 저장
    public String managerMemberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/managerForm"; //fields.hasErrors 여기서 에러메세지 뜸.
        }
        try{
            Member member = Member.createManagerMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/managerForm";
        }
        return "member/memberJoin";
    }

    @GetMapping(value="/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/findId") //아이디 찾기 클릭했을 때
    public String findId(Model model){
        model.addAttribute("findIdFormDto", new FindIdDto());
        return "member/findIdForm";
    }

    @PostMapping(value = "/findId")
    public String getId(@ModelAttribute("findIdFormDto") @Valid FindIdDto findIdDto,
                        BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/findIdForm";
        }
        try{
            Member member = memberService.findId(findIdDto);
            model.addAttribute("MemberId",member.getEmail());
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/findIdForm";
        }
        return "member/getIdForm";
    }

    @GetMapping(value = "/myInfo") //내정보조회 클릭했을 때
    public String getMyInfo(Principal principal, Model model){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        String email = principal.getName();
        MemberFormDto memberFormDto = memberService.getMember(email);
        model.addAttribute("memberFormDto",memberFormDto);
        return "member/myInfo";
    }

    @GetMapping(value = "/updateInfo") // 정보 수정 클릭했을 때
    public String updateInfo(Principal principal,Model model){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        String email = principal.getName();
        MemberFormDto memberFormDto = memberService.getMember(email);
        UpdateInfoDto updateInfoDto = new UpdateInfoDto();
        updateInfoDto.setName(memberFormDto.getName());
        updateInfoDto.setZipcode(memberFormDto.getZipcode());
        updateInfoDto.setStreetAdr(memberFormDto.getStreetAdr());
        updateInfoDto.setDetailAdr(memberFormDto.getDetailAdr());
        updateInfoDto.setTelNumber(memberFormDto.getTelNumber());
        model.addAttribute("updateInfoForm",updateInfoDto);
        return "member/updateInfo";
    }

    @PostMapping(value = "/updateInfo")
    public String updateInfo(@ModelAttribute("updateInfoForm") @Valid UpdateInfoDto updateInfoDto,
                             BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            return "member/updateInfo";
        }
        String email= principal.getName();
        Long memberId = memberService.updateInfo(updateInfoDto,email);

        return "redirect:/";
    }

    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfirm(@PathVariable("email") String email) throws Exception{
        confirm = mailService.sendSimpleMessage(email);
        return new ResponseEntity<String> ("인증 메일을 보냈습니다.", HttpStatus.OK);
    }

    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code") String code) throws Exception{
        if(code.equals(confirm)){
            confirmCheck=true;
            return new ResponseEntity<String> ("인증 성공하였습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String> ("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
    }


}
