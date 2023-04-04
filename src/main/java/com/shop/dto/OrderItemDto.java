package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto { //조회한 주문 데이터를 화면에 보낼 때 사용할 Dto
    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm=orderItem.getItem().getItemNm();
        this.count=orderItem.getCount();
        this.orderPrice=orderItem.getOrderPrice();
        this.imgUrl=imgUrl;

    }
}
