package com.shop.controller;

import com.shop.Service.BoardService;
import com.shop.Service.CommentService;
import com.shop.Service.MemberService;
import com.shop.dto.*;
import com.shop.entity.Board;
import com.shop.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final MemberService memberService;


    @GetMapping(value = "/board/new") //게시글 등록
    public String board(Model model, Principal principal) {
        String email = principal.getName();
        MemberFormDto memberFormDto = memberService.getMember(email);
        String userName = memberFormDto.getName();
        model.addAttribute("boardFormDto", new BoardDto());
        model.addAttribute("userName",userName);
        model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        return "board/boardForm";
    }

    @PostMapping(value = "/board/new") //게시글 저장
    public String boardNew(@ModelAttribute("boardFormDto") @Valid BoardDto boardDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "board/boardForm";
        }
        try {
            boardService.saveBoard(boardDto);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/boards", "/boards/{page}"}) //헤더에서 게시판 클릭
    public String board(BoardSearchDto boardSearchDto,
                        @PathVariable("page") Optional<Integer> page, Model model, Principal principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Board> boards = boardService.getBoardMainPage(boardSearchDto, pageable);

        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        model.addAttribute("boards", boards);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);
        return "board/boardList";
    }

    @GetMapping(value = "/board/{boardId}") //게시글 상세페이지로 이동
    public String noticeDtl(Model model, @PathVariable("boardId") Long boardId,Principal principal) {

        boolean isWriter = boardService.validateWriter(boardId, principal.getName());

        boolean isAdmin=true;
        if(!memberService.getRole(principal.getName()).equals("ADMIN")){
            isAdmin = false;
        }

        boolean roleCheck;
        if(isWriter == true || isAdmin == true){
            roleCheck= true;
        }
        else{
            roleCheck=false;
        }

        List<CommentDto> commentDtoList = commentService.findAll(boardId);
        String email = principal.getName();
        MemberFormDto memberFormDto = memberService.getMember(email);

        try {
            model.addAttribute("roleCheck", roleCheck);
            BoardDto boardDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardDto);
            model.addAttribute("commentList",commentDtoList);
            model.addAttribute("userName",memberFormDto.getName());
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 문의글입니다.");
            return "board/boardList";
        }
        return "board/boardDtl";
    }

    @GetMapping(value = "/board/update/{boardId}") // 게시글 수정할 때
    public String boardUpdate(@PathVariable("boardId") Long boardId, Model model, Principal principal) {
        if(principal != null){
            String email = principal.getName();
            MemberFormDto memberFormDto = memberService.getMember(email);
            model.addAttribute("name","["+memberFormDto.getName()+"]"+"님 환영합니다");
        }
        try {
            BoardDto boardDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글입니다.");
            return "board/boardList";
        }
        return "board/boardUpdateForm";
    }

    @PostMapping(value = "/board/update/{boardId}")
    public String boardUpdate(@ModelAttribute ("boardFormDto") @Valid BoardDto boardDto, BindingResult bindingResult,
                              Model model){
        if(bindingResult.hasErrors()){
            return "board/boardUpdateForm";
        }
        try{
            boardService.updateBoard(boardDto);
        }catch(Exception e){
            model.addAttribute("errorMessage","게시글 수정 중 에러가 발생하였습니다.");
            return "board/boardUpdateForm";
        }
        return "redirect:/boards";
    }

    @GetMapping(value = "/board/delete/{boardId}")
    public String boardDelete(@ModelAttribute ("boardFormDto") @Valid BoardDto boardDto, Model model){
        try {
            boardService.deleteBoard(boardDto);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 삭제 중 에러가 발생하였습니다.");
        }
        return "redirect:/boards";
    }


}