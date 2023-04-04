package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {
    private String searchDateType; //공지등록일
    private String searchBy; //제목 혹은 작성자
    private String searchQuery; // 조회할 검색어
}
