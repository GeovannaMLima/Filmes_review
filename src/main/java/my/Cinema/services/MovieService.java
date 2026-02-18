package my.Cinema.services;

import my.Cinema.client.OmdbMovieClient;
import my.Cinema.dtos.MovieResponseDto;
import my.Cinema.exception.MovieNotFoundedException;
import my.Cinema.models.MovieModel;
import my.Cinema.repositories.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    OmdbMovieClient  omdbMovieClient;

    @Value("${api.omdb.key}")
    private String key;



    public MovieResponseDto getMovieByTitle(String title) {
        Optional<MovieModel> movie = movieRepository.findByTitleIgnoreCase(title);

        if (movie.isPresent()) {
            var movieDto= new MovieResponseDto(movie.get().getTitle(),movie.get().getYear(),movie.get().getDirector(),movie.get().getImdbId(),"True");
            return movieDto;
        }
        System.out.println("Buscando na API externa (OMDB)...");
        var callApi= omdbMovieClient.getMovieByTitle(title,key);
        //verifica se respone = false e lanca execption
        if("False".equalsIgnoreCase(callApi.response())){
            throw new MovieNotFoundedException("Filme não encontrado com titulo: " + title);
        }
        var movieModel = new MovieModel(callApi.imdbId(),callApi.title(), callApi.year(), callApi.director());
        movieRepository.save(movieModel);
        return callApi;
    }

    public MovieResponseDto getMovieByImdbId(String imdbId) {
        Optional<MovieModel> movie = movieRepository.findByImdbIdIgnoreCase(imdbId);
        if (movie.isPresent()) {
            var movieDto= new MovieResponseDto(movie.get().getTitle(),movie.get().getYear(),movie.get().getDirector(),movie.get().getImdbId(),"True");
            return movieDto;
        }
        var callApi = omdbMovieClient.getMovieById(imdbId,key);
        if("False".equalsIgnoreCase(callApi.response())){
            throw new MovieNotFoundedException("Filme não encontrado com Id: " + imdbId);
        }
        var movieModel= new MovieModel(callApi.imdbId(),callApi.title(), callApi.year(), callApi.director());
        movieRepository.save(movieModel);
        return callApi;
    }
}
