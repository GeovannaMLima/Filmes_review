package my.Cinema.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email, String role) {
}
