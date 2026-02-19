package my.Cinema.dtos;

import jakarta.validation.constraints.*;
import my.Cinema.models.Status;

public record UserMovieRequestDto(@NotBlank String imdbId,
                                  @NotNull Status status,
                                  @Min(value =0, message= "A nota mínima é 0")
                                  @Max(value =5, message= "A nota máxima é 5")
                                  Integer nota,
                                  String review) {
}
