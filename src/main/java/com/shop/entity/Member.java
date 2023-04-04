package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.dto.UpdateInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Table(name = "member")
@ToString
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) //회원은 이메일 통해 유일하게 구분하기 때문에 동일한 값을 못 들어오게 막기
    private String email;

    private String password;

    private String zipcode;

    private String streetAdr;

    private String detailAdr;

    private String telNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setZipcode(memberFormDto.getZipcode());
        member.setStreetAdr(memberFormDto.getStreetAdr());
        member.setDetailAdr(memberFormDto.getDetailAdr());
        member.setEmail(memberFormDto.getEmail());
        member.setTelNumber(memberFormDto.getTelNumber());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); // Bean을 파라미터로 넘겨서 비밀번호를 암호화한다.
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }

    public static Member createManagerMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setZipcode(memberFormDto.getZipcode());
        member.setStreetAdr(memberFormDto.getStreetAdr());
        member.setDetailAdr(memberFormDto.getDetailAdr());
        member.setEmail(memberFormDto.getEmail());
        member.setTelNumber(memberFormDto.getTelNumber());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); // Bean을 파라미터로 넘겨서 비밀번호를 암호화한다.
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

    public Long updateMember(UpdateInfoDto updateInfoDto){
        this.name= updateInfoDto.getName();
        this.zipcode= updateInfoDto.getZipcode();
        this.streetAdr= updateInfoDto.getStreetAdr();
        this.detailAdr= updateInfoDto.getDetailAdr();
        this.telNumber=updateInfoDto.getTelNumber();
        return this.id;
    }
}
