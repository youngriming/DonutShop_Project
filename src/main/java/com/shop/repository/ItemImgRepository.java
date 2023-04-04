package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {//상품의 이미지 정보를 저장하기 위해서 repository 생성
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
    //상품 대표 이미지를 찾는 쿼리 메소드(구매이력 페이지에서 주문 상품의 대표 이미지를 보여주기 위해)
}
