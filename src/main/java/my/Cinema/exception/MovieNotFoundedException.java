package my.Cinema.exception;

public class MovieNotFoundedException extends RuntimeException{

    public MovieNotFoundedException() {
        super("Filme não Encontrado");
    }
    public MovieNotFoundedException(String message) {
        super(message);
    }
}
