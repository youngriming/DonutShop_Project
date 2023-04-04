package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection //해당 어노테이션을 이용하면 item 객체로 값을 받은 후 Dto 클래스로 변환하는 과정 없이 바로 Dto 객체를 뽑아낼 수 있음.
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price){
        this.id=id;
        this.itemNm=itemNm;
        this.itemDetail=itemDetail;
        this.imgUrl=imgUrl;
        this.price=price;

    }
}
