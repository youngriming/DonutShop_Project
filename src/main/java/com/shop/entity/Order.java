package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member; //한 명의 회원이 여러 번 주문을 할 수 있으므로 주문엔티티 기준에서 다대일 관계를 맺어줌.

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                orphanRemoval = true, fetch = FetchType.LAZY) //연관 관계 주인(OrderItem 테이블)의 필드인 order를 mappedBy의 값으로 세팅. 양방향 참조
    private List<OrderItem> orderItems = new ArrayList<>(); //OrderItem에서 다대일로 맺었으니 반대인 일대다로 맺어줌.
    //CascadeType.ALL는 부모 엔티티의 영속성 상태변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll 옵션으로 설정
    //고아 객체 제거를 위해 orphanRemoval = true 옵션 지정해줌. Order 엔티티(부모 엔티티)에서 OrderItem 엔티티(자식 엔티티)를 삭제했을 때 orderItem 엔티티가 삭제
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this); //Order 엔티티와 OrderItem엔티티는 양방향 참조 관계이기 떄문에 OrderItem 객체에도 Order 세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 order로 세팅
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){ //총 주문금액을 구하는 메소드
        int totalPrice =0;
        for(OrderItem orderItem : orderItems){
            totalPrice = totalPrice+ orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus=OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

}
