package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id") //장바구니 아이템 테이블은 cart_id 칼럼을 외래키로 갖는다.
    private Cart cart; // 하나의 장바구니에는 여러 개의 장바구니 아이템이 들어갈 수 있으므로 다대일 관계로 맺어줌.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")//장바구니 아이템 테이블은 item_id 칼럼을 외래키로 갖는다.
    private Item item; //하나의 아이템은 여러 개의 장바구니 아이템으로 들어갈 수 있으므로 다대일 관계로 맺어줌.

    private int count; //같은 상품을 몇 개 담을지 저장

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){
        this.count+=count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
