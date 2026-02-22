package my.Cinema.services;

import my.Cinema.client.OmdbMovieClient;
import my.Cinema.dtos.MovieResponseDto;
import my.Cinema.exception.MovieNotFoundedException;
import my.Cinema.models.MovieModel;
import my.Cinema.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private OmdbMovieClient omdbMovieClient;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void  setUp()
    {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(movieService,"key","my_secret_key");;
    }


    @Test
    @DisplayName("Deve entregar filme pelo titulo encontrado no Banco")
    void getMovieByTitleByRepository() {
        String title = "test";
        MovieModel movie = new MovieModel();
        movie.setTitle(title);

        when(movieRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(movie));

        MovieResponseDto response = movieService.getMovieByTitle(title);

        assertNotNull(response);
        verify(movieRepository,times(1)).findByTitleIgnoreCase(title);
        verify(movieRepository,never()).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve entregar filme pelo titulo encontrado na API")
    void getMovieByTitleByAPI() {
        String title = "movieTest";

        when(movieRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.empty());
        MovieResponseDto responseapi= new MovieResponseDto("movieTest","1995","directorTest","9867ib","True");

        when(omdbMovieClient.getMovieByTitle(title,"my_secret_key")).thenReturn(responseapi);

        MovieResponseDto response = movieService.getMovieByTitle(title);
        assertNotNull(response);
        verify(movieRepository,times(1)).findByTitleIgnoreCase(title);
        verify(movieRepository,times(1)).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve lançar MovieNotFoundedException se filme nao for encontrado na api")
    void TryGetMovieByTitleButMovieNotFoundException() {
        String title = "Test";

        when(movieRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.empty());
        MovieResponseDto responseapi= new MovieResponseDto("movieTest","1995","directorTest","9867ib","False");

        when(omdbMovieClient.getMovieByTitle(title,"my_secret_key")).thenReturn(responseapi);
       //java ja verifica que o parametro veio false

        assertThrows(MovieNotFoundedException.class,()->
                movieService.getMovieByTitle(title));

        verify(movieRepository,times(1)).findByTitleIgnoreCase(title);
        verify(omdbMovieClient,times(1)).getMovieByTitle(title,"my_secret_key");
        verify(movieRepository,never()).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve entregar filme pelo Imdb encontrado no Banco")
    void getMovieByImdbIdByRepository() {
        String imdbId = "imdbId";
        MovieModel movie = new MovieModel();
        movie.setImdbId(imdbId);

        when(movieRepository.findByImdbIdIgnoreCase(imdbId)).thenReturn(Optional.of(movie));

        MovieResponseDto response = movieService.getMovieByImdbId(imdbId);

        assertNotNull(response);
        verify(movieRepository,times(1)).findByImdbIdIgnoreCase(imdbId);
        verify(movieRepository,never()).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve entregar filme pelo Imdb encontrado na API")
    void getMovieByImdbIdByAPI() {
        String imdbId = "imdbId";

        when(movieRepository.findByImdbIdIgnoreCase(imdbId)).thenReturn(Optional.empty());
        MovieResponseDto responseapi= new MovieResponseDto("movieTest","1995","directorTest","imdbId","True");

        when(omdbMovieClient.getMovieById(imdbId,"my_secret_key")).thenReturn(responseapi);

        MovieResponseDto response = movieService.getMovieByImdbId(imdbId);
        assertNotNull(response);
        verify(movieRepository,times(1)).findByImdbIdIgnoreCase(imdbId);
        verify(movieRepository,times(1)).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve lançar MovieNotFoundedException se filme nao for encontrado na api")
    void TryGetMovieByImdbIdButMovieNotFoundException() {

        String imdbId = "imdbId";

        when(movieRepository.findByImdbIdIgnoreCase(imdbId)).thenReturn(Optional.empty());
        MovieResponseDto responseapi= new MovieResponseDto("movieTest","1995","directorTest","9867ib","False");

        when(omdbMovieClient.getMovieById(imdbId,"my_secret_key")).thenReturn(responseapi);
        //java ja verifica que o parametro veio false

        assertThrows(MovieNotFoundedException.class,()->
                movieService.getMovieByImdbId(imdbId));

        verify(movieRepository,times(1)).findByImdbIdIgnoreCase(imdbId);
        verify(omdbMovieClient,times(1)).getMovieById(imdbId,"my_secret_key");
        verify(movieRepository,never()).save(any(MovieModel.class));
    }

    @Test
    @DisplayName("Deve retornar pagina com lista de filmes")
    void getAllMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        MovieModel movieTest = new MovieModel("123ed","titleTest","2025","directorTest");

        Page<MovieModel> pageTest = new PageImpl<MovieModel>(List.of(movieTest));

        when(movieRepository.findAll(pageable)).thenReturn(pageTest);

        //act
        Page<MovieResponseDto> response = movieService.getAllMovies(pageable);

        assertNotNull(response);
        verify(movieRepository,times(1)).findAll(pageable);
    }
}