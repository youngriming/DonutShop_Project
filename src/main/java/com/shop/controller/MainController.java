package com.shop.controller;

import com.shop.Service.ItemService;
import com.shop.Service.MemberService;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.MemberFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "/") // 메인 페이지에 접속했을 때 신제품
    public String newItem(Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,3);
        Page<MainItemDto> items = itemService.getNewItemPage(pageable);
        model.addAttribute("items",items);
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        return "main";
    }

}
