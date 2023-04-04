package com.shop.repository;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email); //회원가입시 중복된 회원이 있는 지 검사하여 쿼리문 날린다.
    Member findByNameAndTelNumber(String name,String telNumber); // '아이디 찾기' 하기 위해 입력받은 이름,전화번호 정보로 db에서 멤버 찾아옴.
}
