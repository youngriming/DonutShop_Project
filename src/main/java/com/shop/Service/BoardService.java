package com.shop.Service;

import com.shop.dto.BoardDto;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.NoticeDto;
import com.shop.entity.Board;
import com.shop.entity.Notice;
import com.shop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public Long saveBoard(BoardDto boardDto){
        Board board= Board.createBoard(boardDto);
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public Page<Board> getBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardMainPage(boardSearchDto,pageable);
    }

    @Transactional
    public BoardDto getBoardDtl(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new); // 없으면 예외 던져줌.
        BoardDto boardDto = BoardDto.of(board);
        return boardDto;
    }

    @Transactional
    public Boolean validateWriter(Long boardId, String email){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        String writerMemberEmail = board.getCreatedBy();

        if(!writerMemberEmail.equals(email)){ //같지 않으면
            return false;
        }
        return true; //같으면 true 반환
    }

    public Long updateBoard(BoardDto boardDto)throws Exception{
        Board board = boardRepository.findById(boardDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardDto);
        return board.getId();
    }

    public void deleteBoard(BoardDto boardDto)throws Exception{
        Board board = boardRepository.findById(boardDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }
}
