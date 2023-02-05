package qwerty1434.login.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginDto {
    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
