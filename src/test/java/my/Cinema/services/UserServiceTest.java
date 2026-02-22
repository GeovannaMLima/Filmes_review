package my.Cinema.services;

import my.Cinema.dtos.UserRegisterRequestDto;
import my.Cinema.dtos.UserResponseDto;
import my.Cinema.exception.UserAlreadyExistException;
import my.Cinema.exception.UserNotFoundException;
import my.Cinema.models.UserModel;
import my.Cinema.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Return UserAlreadyExistException se ja existir usuário com email")
    void TrySaveUserButUserAlreadyExistException() {
        UserRegisterRequestDto requestDto= new UserRegisterRequestDto("teste@gmail","abc123de");
        //userfalso pra dar conflito com request
        UserModel userFalso= new UserModel();
        userFalso.setLogin("teste@gmail");
        when(userRepository.findByLogin(requestDto.login())).thenReturn(Optional.of(userFalso));

        assertThrows(UserAlreadyExistException.class,()->
                userService.saveUser(requestDto));

        verify(userRepository, never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Deve salvar usuario quando email estiver livre ")
    void SaveUserSucess() {
        UserRegisterRequestDto requestDto= new UserRegisterRequestDto("teste@gmail","abc123de");
        when(userRepository.findByLogin(requestDto.login())).thenReturn(Optional.empty());
        //treina encriptador
        when(passwordEncoder.encode(requestDto.password())).thenReturn("hash_test");

        //act
        UserResponseDto response = userService.saveUser(requestDto);

        //assert
        assertNotNull(response);

        verify(userRepository, times(1)).save(ArgumentMatchers.any(UserModel.class));
    }

    @Test
    @DisplayName("retorna o usuario que possui o id passado")
    void getUserByIdSucess() {
        Long id = 1L;
        UserModel user =  new UserModel();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.getUserById(id);
        assertNotNull(response);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("retorna o UserNotFoundedException quando id passado não for encontrado")
    void tryGetUserByIdButUserNotFoundException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->
                userService.getUserById(id));
        //chama 1 vez o metodo para verificar se existe
        verify(userRepository, times(1)).findById(id);
    }
}