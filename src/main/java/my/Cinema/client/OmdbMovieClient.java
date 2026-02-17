package my.Cinema.client;

import my.Cinema.dtos.MovieResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="omdbClient", url ="${api.omdb-url}")
public interface OmdbMovieClient {

    @GetMapping("/")
    MovieResponseDto getMovieByTitle(@RequestParam("t") String title,
                                     @RequestParam("apiKey")  String Key);

    @GetMapping("/")
    MovieResponseDto getMovieById(@RequestParam("i") String imdbId,
                                  @RequestParam("apiKey") String Key);
}
