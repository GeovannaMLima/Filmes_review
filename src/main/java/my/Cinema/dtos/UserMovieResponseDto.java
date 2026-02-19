package my.Cinema.dtos;

import my.Cinema.models.Status;
import my.Cinema.models.UserMovieModel;

public record UserMovieResponseDto(Long id,
                                   String titulo,
                                   String director,
                                   Status status,
                                   Integer nota,
                                   String review){
    public UserMovieResponseDto(UserMovieModel entity){
        this(
                entity.getId(),
                entity.getMovie().getTitle(),
                entity.getMovie().getDirector(),
                entity.getStatus(),
                entity.getNota(),
                entity.getReview()
        );
    }
}
