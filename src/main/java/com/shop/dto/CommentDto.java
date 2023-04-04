package com.shop.dto;

import com.shop.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private String commentRegTime;

    public static CommentDto toCommentDto(Comment comment,Long boardId){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCommentWriter(comment.getCommentWriter());
        commentDto.setCommentContents(comment.getCommentContents());
        commentDto.setBoardId(boardId);
        commentDto.setCommentRegTime(comment.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return commentDto;
    }
}
