package qwerty1434.login.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    private final String authorityKey;
    private final long tokenValidityInMilliseconds;
    private Key key;

    // 생성자 주입, application.properties의 값을 주입
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.auth}") String auth){
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.authorityKey = auth;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication객체의 권한정보를 이용해 토큰 생성
    public String createToken(Authentication authentication){
        // authentication에 있는 권한들
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 만료기간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // jwt 토큰 생성 후 리턴
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authorityKey, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // Token을 파라미터로 받은 뒤 토큰에 담긴 정보를 이용해 Authentication 객체를 리턴하는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰으로 클래임을 만듬
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클래임에서 권한정보를 빼냄
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authorityKey).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한정보를 가지고 userdetails의 유저 객체를 만듬
        User principal = new User(claims.getSubject(), "", authorities);

        // 유저객체, 토큰, 권한정보를 가지고 Authentication객체를 만들고 이를 리턴
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰을 파라미터로 받은 뒤 토큰의 유효성 검증 수행
    public boolean validateToken(String token) {
        // 토큰을 파싱해 문제가 있으면 return false, 문제가 없으면 return true
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


}
