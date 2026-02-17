package my.Cinema.repositories;

import my.Cinema.models.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieModel,Long> {
    Optional<MovieModel> findByTitleIgnoreCase(String title);

    Optional<MovieModel> findByImdbIdIgnoreCase(String imdbId);
}
