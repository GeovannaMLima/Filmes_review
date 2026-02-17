package my.Cinema.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResponseDto(@JsonProperty("Title") String title,
                               @JsonProperty("Year") String year,
                               @JsonProperty("Director") String director,
                               @JsonProperty("imdbID") String imdbId,
                               @JsonProperty("Response") String response) {
}
