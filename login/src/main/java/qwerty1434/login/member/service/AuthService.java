package qwerty1434.login.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qwerty1434.login.global.jwt.JwtFilter;
import qwerty1434.login.global.jwt.TokenProvider;
import qwerty1434.login.member.dto.LoginDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public HttpHeaders authorize(LoginDto loginDto) {
        log.info("authorize 시작");

        // username, password를 통해 AuthenticationToken객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getNickname(), loginDto.getPassword());
        log.info("authenticationToken: {}",authenticationToken);

        // authentication 토큰을 이용해 authenticate 메서드가 실행될 때 CustomUserDetailsService의 loadUserByUsername이 실행됨.
        // 실행된 결과값을 가지고 authentication 결과값을 생성하게 됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication: {}",authentication);

        // 생성된 authentication 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // tokenProvider를 통해 토큰 생성
        String jwt = tokenProvider.createToken(authentication);
        log.info("jwt: {}",jwt);

        // jwt token을 response header에 넣어서 보내줌
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return httpHeaders;
    }

}
