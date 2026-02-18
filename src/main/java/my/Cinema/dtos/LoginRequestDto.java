package my.Cinema.dtos;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(@NotEmpty String login,
                              @NotEmpty String password ) {
}
