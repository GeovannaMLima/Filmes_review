package my.Cinema.repositories;

import my.Cinema.models.MovieModel;
import my.Cinema.models.UserModel;
import my.Cinema.models.UserMovieModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovieModel,Long> {
    Page<UserMovieModel> findByUserId(Long id, Pageable pageable);

    Boolean existsByUserAndMovie(UserModel user, MovieModel movie);
}
