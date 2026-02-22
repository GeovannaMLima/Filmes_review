package my.Cinema.services;

import my.Cinema.dtos.UserMovieRequestDto;
import my.Cinema.dtos.UserMovieResponseDto;
import my.Cinema.exception.MovieAlreadyInListException;
import my.Cinema.models.MovieModel;
import my.Cinema.models.Status;
import my.Cinema.models.UserModel;
import my.Cinema.models.UserMovieModel;
import my.Cinema.repositories.MovieRepository;
import my.Cinema.repositories.UserMovieRepository;
import my.Cinema.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMovieServiceTest {

    @Mock
    private UserMovieRepository userMovieRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private UserMovieService userMovieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("Deve adicionar filme na lista")
    void addMovieToListSucess() {
        //preparar cria/criar objts
        UserModel testUser= new UserModel();
        testUser.setLogin("test@gamil.com");

        MovieModel testMovie= new MovieModel();
        testMovie.setImdbId("tt1234");

        UserMovieRequestDto requestDto = new UserMovieRequestDto("tt1234", Status.WATCHED,5,"Review teste");

        // logar com security p test
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gamil.com");
        SecurityContextHolder.setContext(securityContext);

        //treina mock
        when(userRepository.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        when(movieRepository.findByImdbIdIgnoreCase(testMovie.getImdbId())).thenReturn(Optional.of(testMovie));
        //se repository ask filme na lista, retorna true
        when(userMovieRepository.existsByUserAndMovie(testUser,testMovie)).thenReturn(false);

        UserMovieResponseDto response = userMovieService.addMovieToList(requestDto);
        assertNotNull(response);//se metodo n entrega null
        assertEquals(5,requestDto.nota());

        verify(userMovieRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exception MovieAlreadyInList se filme ja estiver na lista")
    void addMovieToListButFilmeALreadyInList() {
        //preparar cria/criar objts
        UserModel testUser= new UserModel();
        testUser.setLogin("test@gamil.com");

        MovieModel testMovie= new MovieModel();
        testMovie.setImdbId("tt1234");

        UserMovieRequestDto requestDto = new UserMovieRequestDto("tt1234", Status.WATCHED,5,"Review teste");

        // logar com security p test
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gamil.com");
        SecurityContextHolder.setContext(securityContext);

        //treina mock
        when(userRepository.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        when(movieRepository.findByImdbIdIgnoreCase(testMovie.getImdbId())).thenReturn(Optional.of(testMovie));
            //se repository ask filme na lista, retorna true
        when(userMovieRepository.existsByUserAndMovie(testUser,testMovie)).thenReturn(true);

        assertThrows(MovieAlreadyInListException.class,()->
                userMovieService.addMovieToList(requestDto));

        verify(userMovieRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar pagina com lista de filmes de outro usuário")
    void lookListOfOthersUsersSucess() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        //movie e user obj falsos
        MovieModel testMovie = new MovieModel("teste1234","testTitle","testYear","testDirector");
        UserModel testUser = new UserModel();
        testUser.setLogin("test@email");
        UserMovieModel userMovieModel = new UserMovieModel(testUser,testMovie,5,Status.WATCHED,"test");



        Page<UserMovieModel>pageFake= new PageImpl<>(List.of(userMovieModel));
        when(userMovieRepository.findByUserId(userId,pageable)).thenReturn(pageFake);

        Page<UserMovieResponseDto> response = userMovieService.lookListOfOthersUsers(userId,pageable);

        //assert
        assertNotNull(response);
        assertEquals(1,response.getTotalElements());

        verify(userMovieRepository,times(1)).findByUserId(userId,pageable);
    }
}