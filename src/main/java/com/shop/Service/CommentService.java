package com.shop.Service;

import com.shop.dto.CommentDto;
import com.shop.entity.Board;
import com.shop.entity.Comment;
import com.shop.entity.Notice;
import com.shop.repository.BoardRepository;
import com.shop.repository.CommentRepository;
import com.shop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDto commentDto){
        Optional<Board> optionalBoard = boardRepository.findById(commentDto.getBoardId());
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            Comment comment = Comment.toSaveEntity(commentDto,board);
            return commentRepository.save(comment).getId();
        }else{
            return null;
        }
    }

    public List<CommentDto> findAll(Long boardId){
        Board board = boardRepository.findById(boardId).get();
        List<Comment> commentList = commentRepository.findByBoardOrderByIdDesc(board);

        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            CommentDto commentDto = CommentDto.toCommentDto(comment, boardId);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }

}
