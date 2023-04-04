package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType; //상품 등록일
    private ItemSellStatus searchSellStatus; //상품 판매 상태
    private String searchBy;//어떤 기준으로 검색할 것인지
    private String searchQuery;//검색어
}
