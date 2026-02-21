package my.Cinema.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(){
        super("Este email ja foi cadastrado!");
    }
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
