package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity{ //상품 이미지 저장하는 엔티티
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName;//원본 이미지 파일명

    private String imgUrl;//이미지 조회 경로

    private String repImgYn;//대표이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)//상품 엔티티 정보가 필요한 경우 데이터를 조회
    @JoinColumn(name="item_id")
    private Item item; //상품 1개에 이미지는 5개까지 등록이 가능하므로 상품이미지테이블 기준으로 다대일 관계로 맺어줌.

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.imgName=imgName;
        this.oriImgName=oriImgName;
        this.imgUrl=imgUrl;
    }



}
