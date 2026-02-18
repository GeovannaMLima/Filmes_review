package my.Cinema.dtos;

import my.Cinema.models.UserModel;

public record UserResponseDto(Long id,
                              String login,
                              String password) {
    public UserResponseDto(UserModel user) {
        this(user.getId(), user.getLogin(), user.getPassword());
    }
}

