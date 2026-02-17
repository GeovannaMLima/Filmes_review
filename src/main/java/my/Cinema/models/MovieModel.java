package my.Cinema.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="movie")
public class MovieModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public MovieModel(String imdbId, String title, String year, String director) {
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.director = director;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "imdb_id", unique = true, nullable = false)
    private String imdbId;

    private String title;
    private String year;
    private String director;


}
