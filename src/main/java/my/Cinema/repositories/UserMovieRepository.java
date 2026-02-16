package my.Cinema.repositories;

import my.Cinema.models.UserModel;
import my.Cinema.models.UserMovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovieModel,Long> {
}
