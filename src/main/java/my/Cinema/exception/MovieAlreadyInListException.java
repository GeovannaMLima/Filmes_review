package my.Cinema.exception;

public class MovieAlreadyInListException extends RuntimeException {
    public  MovieAlreadyInListException(){super("Este filme ja esta na sua Lista");}
    public MovieAlreadyInListException(String message) {
        super(message);
    }
}
