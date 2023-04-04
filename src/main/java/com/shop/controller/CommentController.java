package com.shop.controller;

import com.shop.Service.CommentService;
import com.shop.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping ("/save")
    public @ResponseBody ResponseEntity save(@ModelAttribute CommentDto commentDto){
        Long saveResult = commentService.save(commentDto);
        if(saveResult != null){
            List<CommentDto> commentDtoList = commentService.findAll(commentDto.getBoardId());
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>("해당 게시글이 존재하지 않습니다.",HttpStatus.NOT_FOUND);
        }
    }
}
