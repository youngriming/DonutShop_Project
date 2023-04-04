package com.shop.controller;

import com.shop.Service.CommentService;
import com.shop.Service.MemberService;
import com.shop.Service.NoticeService;
import com.shop.dto.*;
import com.shop.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService;

    @GetMapping(value = "/admin/notice/new") //공지사항 작성 클릭했을 때
    public String notice(Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        model.addAttribute("noticeFormDto",new NoticeDto());
        return "notice/noticeForm";
    }

    @PostMapping(value = "/admin/notice/new") //새로운 공지사항 등록, '저장' 버튼 눌렀을 때 공지사항 DB에 저장
    public String noticeNew(@ModelAttribute("noticeFormDto") @Valid NoticeDto noticeDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }
        try{
            noticeService.saveNotice(noticeDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","공지 등록 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }
        return "redirect:/notices";
    }

    @GetMapping(value = "/admin/notice/{noticeId}") //공지사항 관리 탭에서 수정할 때
    public String noticeUpdate(@PathVariable("noticeId") Long noticeId, Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        try{
            NoticeDto noticeDto = noticeService.getNoticeDtl(noticeId);
            model.addAttribute("noticeFormDto", noticeDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 공지입니다.");
            return "notice/noticeForm";
        }
        return "notice/noticeForm";
    }

    @PostMapping(value = "/admin/notice/{noticeId}") //공지사항 수정 완료하고 DB에 업데이트
    public String noticeUpdate(@ModelAttribute("noticeFormDto") @Valid NoticeDto noticeDto,
                               BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }
        try{
            noticeService.updateNotice(noticeDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","공지 수정 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }
        return "redirect:/notices";
    }
    @PostMapping(value = "/admin/notice/delete/{noticeId}") //공지사항 삭제 버튼 눌렀을 때
    public String noticeDelete(@ModelAttribute("noticeFormDto") NoticeDto noticeDto, Model model){
        try{
            noticeService.deleteNotice(noticeDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","공지 삭제 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }
        return "redirect:/notices";
    }

    @GetMapping(value = {"/notices", "/notices/{page}"}) //헤더에서 공지사항 클릭
    public String notice(NoticeSearchDto noticeSearchDto,
                         @PathVariable("page")Optional<Integer> page, Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,5);
        Page<Notice> notices = noticeService.getNoticeMainPage(noticeSearchDto, pageable);

        model.addAttribute("notices",notices);
        model.addAttribute("noticeSearchDto",noticeSearchDto);
        model.addAttribute("maxPage",5);
        return "notice/noticeList";
    }

    @GetMapping(value = "/notice/{noticeId}") //공지사항 상세페이지로 이동(수정 불가)
    public String noticeDtl(Model model, @PathVariable("noticeId") Long noticeId,Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        try{
            NoticeDto noticeDto = noticeService.getNoticeDtl(noticeId);
            model.addAttribute("noticeFormDto", noticeDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 공지입니다.");
            return "notice/noticeList";
        }
        return "notice/noticeDtl";
    }

    @GetMapping(value = {"/admin/notices", "/admin/notices/{page}"}) //공지사항 관리 눌렀을 때 (관리페이지 나옴)
    public String noticeManage(NoticeSearchDto noticeSearchDto,
                               @PathVariable("page")Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,5);
        Page<Notice> notices = noticeService.getNoticeMngPage(noticeSearchDto, pageable);
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        model.addAttribute("notices",notices);
        model.addAttribute("noticeSearchDto",noticeSearchDto);
        model.addAttribute("maxPage",5);
        return "notice/noticeMng";
    }


}
