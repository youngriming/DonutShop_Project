package com.shop.dto;

import com.shop.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDto {
    private Long id;
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotBlank(message = "작성자는 필수 입력 값입니다.")
    private String author;
    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String noticeDetail;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeDto of(Notice notice){
        return modelMapper.map(notice, NoticeDto.class);
    }

}
