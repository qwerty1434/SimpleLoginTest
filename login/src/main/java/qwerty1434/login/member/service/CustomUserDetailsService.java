package qwerty1434.login.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import qwerty1434.login.member.domain.Member;
import qwerty1434.login.member.repository.MemberRepository;

import java.util.Collections;


@Component("userDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.info("loadUserByUsername 실행");
        // DB에서 권한정보와 유저정보를 가져옴
        log.info("nickname: {}",username);
        Member member = memberRepository.findByNickname(username);
        log.info("member: {}",member);
        return createUser(member);
    }

    private UserDetails createUser(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        // 유저의 권한정보, username, password를 가지고 Userdetails의 User객체를 리턴함
        return new User(member.getNickname(),
                member.getPassword(),
                Collections.singleton(grantedAuthority));
    }
}
