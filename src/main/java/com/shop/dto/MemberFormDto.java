package com.shop.dto;


import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class MemberFormDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; //이름
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email; //이메일
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password; //비밀번호
    /*@NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address; // 주소*/
    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;
    @NotEmpty(message = "도로명 주소는 필수 입력 값입니다.")
    private String streetAdr;
    @NotEmpty(message = "상세 주소는 필수 입력 값입니다.")
    private String detailAdr;
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "전화번호의 양식과 맞지 않습니다.")
    private String telNumber; //전화번호

    private static ModelMapper modelMapper = new ModelMapper();
    public static MemberFormDto of(Member member){
        return modelMapper.map(member, MemberFormDto.class);
    }
}
