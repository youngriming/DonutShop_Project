package com.shop.Service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()) //어떤 아이템을 주문했는지 엔티티 객체 뽑아옴
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); // 누가 주문했는지 회원 정보 조회

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());//주문할 상품 엔티티와 주문 수량으로 주문 상품 엔티티 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); //회원 정보와 주문 상품 리스트를 이용하여 주문 엔티티 생성
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){ // 주문 내역들을 보여주는 페이지 생성
        List<Order> orders = orderRepository.findOrders(email,pageable);//유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email); // 유저의 주문 총 개수

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders){ //주문 목록을 순회하면서 주문을 뽑아내고
            OrderHistDto orderHistDto = new OrderHistDto(order); //구매 이력 페이지에 전달할 Dto를 생성
            List<OrderItem> orderItems = order.getOrderItems(); //주문 아이템만 뽑아내서 리스트로 만듦.
            for(OrderItem orderItem : orderItems){ //주문 아이템 리스트에서 각 주문아이템을 뽑아냄.
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);//주문내역리스트와 페이징정보, 주문 총 개수를 이용하여 페이지 구현 객체를 생성
    }

    //주문 취소
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){ //현재 로그인한 회원이랑 주문한 회원이 같은지 검사
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email){ //장바구니에서 주문할 상품들 데이터를 받아 주문 생성
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList){
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

}
