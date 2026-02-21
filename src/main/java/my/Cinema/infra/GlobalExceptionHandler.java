package my.Cinema.infra;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.Cinema.exception.MovieAlreadyInListException;
import my.Cinema.exception.MovieNotFoundedException;
import my.Cinema.exception.UserAlreadyExistException;
import my.Cinema.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundedException.class)
    private ResponseEntity<RestErrorMessage> handleMovieNotFounded(MovieNotFoundedException e, HttpServletRequest request) {
        RestErrorMessage threatResponse = new RestErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        RestErrorMessage threatResponse = new RestErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    private ResponseEntity<RestErrorMessage> handleUserAlreadyExist(UserAlreadyExistException e, HttpServletRequest request) {
        RestErrorMessage threatResponse = new RestErrorMessage(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<RestErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {

        String errorMessage= e.getBindingResult().getFieldErrors().stream()
                .map(error-> error.getField() + ": "+error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        RestErrorMessage threatResponse = new RestErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

    @ExceptionHandler(MovieAlreadyInListException.class)
    private  ResponseEntity<RestErrorMessage> handleMovieAlreadyInList(MovieAlreadyInListException e, HttpServletRequest request) {
        RestErrorMessage threatResponse = new RestErrorMessage(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatResponse);
    }


}
