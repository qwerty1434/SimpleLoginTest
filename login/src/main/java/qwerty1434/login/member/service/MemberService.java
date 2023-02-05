package qwerty1434.login.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qwerty1434.login.member.domain.Member;
import qwerty1434.login.member.dto.LoginDto;
import qwerty1434.login.member.dto.SignupDto;
import qwerty1434.login.member.repository.MemberRepository;
import qwerty1434.login.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void signup(SignupDto signupDto){
        // 아이디 등록
        Member user = Member.builder()
                .nickname(signupDto.getNickname())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .build();

        memberRepository.save(user);
    }

    @Transactional
    public Member login(LoginDto loginDto){
        Member member = memberRepository.findByNickname(loginDto.getNickname());
        return member;
    }

    @Transactional
    public Member info(){
        String nickname = SecurityUtil.getCurrentUsername().get();
        Member member = memberRepository.findByNickname(nickname);
        return member;
    }
    
}
