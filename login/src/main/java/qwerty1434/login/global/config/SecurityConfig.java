package qwerty1434.login.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import qwerty1434.login.global.jwt.JwtSecurityConfig;
import qwerty1434.login.global.jwt.TokenProvider;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .exceptionHandling()

                .and()
                .authorizeRequests()
                // 토큰이 없이 요청되는 페이지
                .antMatchers("/user/login","/user/signup" ,"/user/test").permitAll()
                .anyRequest().authenticated() // 나머지는 다 인증이 필요함

                // addFilterBefore로 등록했던 JwtSecurityConfig클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}
