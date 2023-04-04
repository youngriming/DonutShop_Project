package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto { //ItemImg 엔티티 객체를 화면으로 전달하기 위해 DTO 생성
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();
    //모델매퍼는 서로 다른 클래스의 값을 필드의 이름과 자료형이 같으면 getter, setter를 통해 값을 복사해서 객체를 반환
    public static ItemImgDto of(ItemImg itemImg){ //엔티티 객체를 파라미터로 받아서
        return modelMapper.map(itemImg, ItemImgDto.class); //itemImg 객체의 자료형과 멤버변수 이름이 같을 때 ItemImgDto로 값을 복사해서 반환.
    }
}
