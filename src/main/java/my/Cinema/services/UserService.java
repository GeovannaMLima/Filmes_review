package my.Cinema.services;

import my.Cinema.dtos.UserRegisterRequestDto;
import my.Cinema.dtos.UserResponseDto;
import my.Cinema.models.UserModel;
import my.Cinema.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto saveUser(UserRegisterRequestDto requestDto) {
        var user = new UserModel();
        BeanUtils.copyProperties(requestDto, user); //copy data do user p dto

        String senhaHash = passwordEncoder.encode(requestDto.password());
        user.setPassword(senhaHash);

        userRepository.save(user);
        var userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }


}
