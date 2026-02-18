package my.Cinema.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterResponseDto(String login, String password) {
}
