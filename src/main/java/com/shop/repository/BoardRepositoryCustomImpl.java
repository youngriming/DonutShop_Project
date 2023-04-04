package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.constant.QnaStatus;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private JPAQueryFactory queryFactory;
    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    private BooleanExpression searchQnaStatusEq(QnaStatus searchQnaStatus){
        return searchQnaStatus == null ? null : QBoard.board.qnaStatus.eq(searchQnaStatus);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("title", searchBy)){
            return QBoard.board.title.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("author",searchBy)){
            return QBoard.board.author.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Board> getBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable){
        QueryResults<Board> results = queryFactory.selectFrom(QBoard.board).
                where(searchQnaStatusEq(boardSearchDto.getQnaStatus()),
                        searchByLike(boardSearchDto.getSearchBy(),boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Board> content = results.getResults(); //쿼리문 실행해서 얻은 결과물인 아이템 객체들을 리스트로 묶음.
        long total = results.getTotal(); //총 넘어온 개수
        return new PageImpl<>(content,pageable,total); //조회한 데이터를 page 클래스의 구현체인 PageImpl 객체롤 반환합니다. page 단위로 보낸다.
    }
}
