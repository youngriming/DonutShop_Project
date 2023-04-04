package com.shop.Service;

import com.shop.dto.FindIdDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.UpdateInfoDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional //로직을 처리하다가 에러가 발생 시 이전 상태로 롤백하는 어노테이션
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){ //이미 가입된 회원의 경우 예외처리
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    public Member findId(FindIdDto findIdDto){
        Member member = memberRepository.findByNameAndTelNumber(findIdDto.getName(),findIdDto.getTelNumber());

        if(member != null){
            return member;
        }
        else{
            throw new EntityNotFoundException("일치하는 정보가 없습니다.");
        }
    }
    public MemberFormDto getMember(String email) {
        Member member = memberRepository.findByEmail(email);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }

    public String getRole(String email){
        Member member = memberRepository.findByEmail(email);
        String roleCheck = String.valueOf(member.getRole());
        return roleCheck;
    }

    public Long updateInfo(UpdateInfoDto updateInfoDto, String email){
        Member member = memberRepository.findByEmail(email);
        if(member != null){
            Long memberId = member.updateMember(updateInfoDto);
            return memberId;
        }
        else{
            throw new EntityNotFoundException("일치하는 정보가 없습니다.");
        }
    }


}
