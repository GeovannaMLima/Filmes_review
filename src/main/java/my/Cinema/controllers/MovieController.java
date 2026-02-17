package my.Cinema.controllers;

import jakarta.persistence.Column;
import lombok.Getter;
import my.Cinema.dtos.MovieResponseDto;
import my.Cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search")
    public ResponseEntity<MovieResponseDto> findMoviesTitle(@RequestParam String title) {
        MovieResponseDto movie= movieService.getMovieByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> findMoviesId(@PathVariable (value="id") String imdbId) {
        MovieResponseDto movie = movieService.getMovieByImdbId(imdbId);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }
}
