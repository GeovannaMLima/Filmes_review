package my.Cinema.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterRequestDto(@NotEmpty String login,
                                     @NotEmpty String password) {
}
