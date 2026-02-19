package my.Cinema.services;


import my.Cinema.dtos.UserMovieRequestDto;
import my.Cinema.dtos.UserMovieResponseDto;
import my.Cinema.exception.MovieNotFoundedException;
import my.Cinema.models.MovieModel;
import my.Cinema.models.UserModel;
import my.Cinema.models.UserMovieModel;
import my.Cinema.repositories.MovieRepository;
import my.Cinema.repositories.UserMovieRepository;
import my.Cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserMovieService {

    @Autowired
    private UserMovieRepository userMovieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;


    public UserMovieResponseDto addMovieToList(UserMovieRequestDto userMovieRequestDto) {
        String userLogado= SecurityContextHolder.getContext().getAuthentication().getName();

        //buscar entidade pelo email
        UserModel user= (UserModel)userRepository.findByLogin(userLogado).orElseThrow(()-> new RuntimeException("Usuario não encontrado"));

        //buscar o filme
        MovieModel movie= movieRepository.findByImdbIdIgnoreCase(userMovieRequestDto.imdbId())
                .orElseThrow(()-> new MovieNotFoundedException("Filme não encontrado"));

        if(userMovieRepository.existsByUserAndMovie(user,movie)){
            throw new RuntimeException("Este filme ja esta na sua Lista");
        }
        UserMovieModel userMovie= new UserMovieModel(user,movie,userMovieRequestDto.nota(),userMovieRequestDto.status(),userMovieRequestDto.review());
        userMovieRepository.save(userMovie);
        return new UserMovieResponseDto(userMovie);

    }

    public List<UserMovieResponseDto> getMyList() {
        //user logado
        UserModel user= getUserLogado();

        //buscar no repositorio e transforma em DTo->List
        return userMovieRepository.findByUserId(user.getId()).stream().
                map(userNew->new UserMovieResponseDto(userNew)).toList();

    }

    public List<UserMovieResponseDto> lookListOfOthersUsers(Long userId) {
        //user q solicita
        UserModel user= getUserLogado();

        //acha user pelo id setado
        return userMovieRepository.findByUserId(userId).stream().
                map(userNew->new UserMovieResponseDto(userNew)).toList();
    }

    private UserModel getUserLogado(){
        String userLogado= SecurityContextHolder.getContext().getAuthentication().getName();
        return (UserModel)userRepository.findByLogin(userLogado).
                orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
    }

}
