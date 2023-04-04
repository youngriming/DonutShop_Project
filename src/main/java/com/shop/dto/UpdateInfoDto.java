package com.shop.dto;

import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UpdateInfoDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; //이름
    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;
    @NotEmpty(message = "도로명 주소는 필수 입력 값입니다.")
    private String streetAdr;
    @NotEmpty(message = "상세 주소는 필수 입력 값입니다.")
    private String detailAdr;
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String telNumber; //전화번호

}
