package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item; // 하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문상품 테이블 기준으로 다대일 관계를 맺어줌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")//OrderItem 테이블은 order_id 칼럼을 외래키로 갖는다.
    private Order order; //하나의 주문에 여러 개의 주문 상품을 주문할 수 있으므로 주문상품 테이블 기준으로 다대일 관계를 맺어줌

    private int orderPrice; //주문 가격

    private int count; //수량

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }
    public void cancel(){
        this.getItem().addStock(count); //주문 취소 시 주문 수량만큼 상품의 재고를 더해줌.
    }
}
