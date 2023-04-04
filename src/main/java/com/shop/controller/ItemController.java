package com.shop.controller;

import com.shop.Service.ItemService;
import com.shop.Service.MemberService;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "admin/item/new") //상품 등록 처음 클릭했을 때 상품 등록 양식 전달
    public String itemForm(Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new") //상품 등록 양식 전달 받아서 DB에 저장
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}") // 상품 수정하려고 상품명 클릭했을 때
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}") // 수정하려는 상품 정보를 DB에 저장
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty()&& itemFormDto.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value ={"/admin/items", "/admin/items/{page}"}) //상품 관리 클릭했을 때
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        Pageable pageable = PageRequest.of(page.isPresent()? page.get():0, 5);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);
        return "item/itemMng";
    }

    @GetMapping(value = "/product") //헤더에서 상품 클릭했을 때
    public String itemMain(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model,Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,8);

        if(itemSearchDto.getSearchQuery() == null){
            itemSearchDto.setSearchQuery("");
        }
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto,pageable);
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "item/product";
    }

    @GetMapping(value = "/item/{itemId}") //상품 클릭했을 때 해당 제품의 상세페이지 띄워줌
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId, Principal principal){
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }
//    @PostMapping(value = "/admin/item/delete/{itemId}") // 상품 삭제
//    public String itemDelete(ItemFormDto itemFormDto, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
//        try{
//            itemService.deleteItem(itemFormDto, itemImgFileList);
//        }catch (Exception e){
//            model.addAttribute("errorMessage","상품 삭제 중 에러가 발생하였습니다.");
//            return "item/itemForm";
//        }
//        return "redirect:/";
//    }

}
