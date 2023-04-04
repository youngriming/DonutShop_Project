package com.shop.dto;

import com.shop.constant.QnaStatus;
import com.shop.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDto {
    private Long id;
    private QnaStatus qnaStatus;
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotBlank(message = "작성자는 필수 입력 값입니다.")
    private String author;
    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String boardDetail;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardDto of(Board board){return modelMapper.map(board, BoardDto.class);}
}
