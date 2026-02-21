package my.Cinema.controllers;

import my.Cinema.dtos.MovieResponseDto;
import my.Cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
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

    @GetMapping()
    public ResponseEntity<Page<MovieResponseDto>> findAllMovies(@PageableDefault(page=0, size=10,sort="id", direction= Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getAllMovies(pageable));
    }
}
