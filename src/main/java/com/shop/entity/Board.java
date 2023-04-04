package com.shop.entity;

import com.shop.constant.QnaStatus;
import com.shop.dto.BoardDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity{

    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Lob
    @Column(nullable = false)
    private String boardDetail;

    @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    public static Board createBoard(BoardDto boardDto){
        Board board = new Board();
        board.setQnaStatus(boardDto.getQnaStatus());
        board.setTitle(boardDto.getTitle());
        board.setAuthor(boardDto.getAuthor());
        board.setBoardDetail(boardDto.getBoardDetail());
        return board;
    }

    public void updateBoard(BoardDto boardDto){
        this.qnaStatus = boardDto.getQnaStatus();
        this.title = boardDto.getTitle();
        this.author = boardDto.getAuthor();
        this.boardDetail = boardDto.getBoardDetail();
    }

}
