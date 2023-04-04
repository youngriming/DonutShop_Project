package com.shop.repository;

import com.shop.dto.BoardSearchDto;
import com.shop.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> getBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable);

}
