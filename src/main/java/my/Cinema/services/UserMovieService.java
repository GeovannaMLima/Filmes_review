package my.Cinema.services;


import my.Cinema.dtos.UserMovieRequestDto;
import my.Cinema.dtos.UserMovieResponseDto;
import my.Cinema.exception.MovieAlreadyInListException;
import my.Cinema.exception.MovieNotFoundedException;
import my.Cinema.exception.UserNotFoundException;
import my.Cinema.models.MovieModel;
import my.Cinema.models.UserModel;
import my.Cinema.models.UserMovieModel;
import my.Cinema.repositories.MovieRepository;
import my.Cinema.repositories.UserMovieRepository;
import my.Cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        UserModel userLogado= getUserLogado();

        //buscar entidade pelo email
        UserModel user= (UserModel)userRepository.findByLogin(userLogado.getLogin()).orElseThrow(()-> new UserNotFoundException("Usuario não encontrado"));

        //buscar o filme
        MovieModel movie= movieRepository.findByImdbIdIgnoreCase(userMovieRequestDto.imdbId())
                .orElseThrow(()-> new MovieNotFoundedException("Filme não encontrado"));

        if(userMovieRepository.existsByUserAndMovie(user,movie)){
            throw new MovieAlreadyInListException("Este filme ja esta na sua Lista");
        }
        UserMovieModel userMovie= new UserMovieModel(user,movie,userMovieRequestDto.nota(),userMovieRequestDto.status(),userMovieRequestDto.review());
        userMovieRepository.save(userMovie);
        return new UserMovieResponseDto(userMovie);

    }

    public Page<UserMovieResponseDto> getMyList(Pageable pageable) {
        //user logado
        UserModel user= getUserLogado();
        //busca paginado no repositorio
        Page<UserMovieModel> pageMyList= userMovieRepository.findByUserId(user.getId(), pageable);
        //metodo map de Page tranf em dto
        return pageMyList.
                map(userNew->new UserMovieResponseDto(userNew));

    }

    public Page<UserMovieResponseDto> lookListOfOthersUsers(Long userId, Pageable pageable) {
        //acha user pelo id setado n precisa do contextHolder
        return userMovieRepository.findByUserId(userId,pageable).
                map(userNew->new UserMovieResponseDto(userNew));
    }

    private UserModel getUserLogado(){
        String userLogado= SecurityContextHolder.getContext().getAuthentication().getName();
        return (UserModel)userRepository.findByLogin(userLogado).
                orElseThrow(()-> new UserNotFoundException("Usuario não encontrado"));
    }

}
