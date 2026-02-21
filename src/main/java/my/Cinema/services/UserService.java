package my.Cinema.services;

import jakarta.transaction.Transactional;
import my.Cinema.dtos.UserRegisterRequestDto;
import my.Cinema.dtos.UserResponseDto;
import my.Cinema.exception.UserAlreadyExistException;
import my.Cinema.exception.UserNotFoundException;
import my.Cinema.models.UserModel;
import my.Cinema.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto saveUser(UserRegisterRequestDto requestDto) {
        if(userRepository.findByLogin(requestDto.login()).isPresent()) {
            throw new UserAlreadyExistException("Este email ja foi cadastrado!");
        }
        var user = new UserModel();
        BeanUtils.copyProperties(requestDto, user); //copy data do user p dto

        String senhaHash = passwordEncoder.encode(requestDto.password());
        user.setPassword(senhaHash);

        userRepository.save(user);
        var userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }
    @Transactional
    public List<UserResponseDto> getAllUsers() {
         List<UserModel> userModel= userRepository.findAll();
         List<UserResponseDto> userResponseDtos = new ArrayList<>();
         for(UserModel user: userModel) {
             userResponseDtos.add(new UserResponseDto(user));
         }
         return userResponseDtos;
    }

    @Transactional
    public UserResponseDto getUserById(Long id) {
        UserModel userModel = userRepository.findById(id).orElseThrow (()->new UserNotFoundException("Úsuário não encontrado"));
        return new UserResponseDto(userModel);
    }


}
