package qwerty1434.login.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {
    // 시큐리티 컨텍스트에서 authentication 객체를 꺼내온 뒤 여기에 있는 username을 파싱해 리턴해주는 메서드
    public static Optional<String> getCurrentUsername() {
        // SecurityContextHolder에 값이 저장되는 건 JwtFilter의 doFilter메서드를 실행하는 시점
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}
