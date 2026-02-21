package my.Cinema.controllers;

import jakarta.validation.Valid;
import my.Cinema.dtos.UserMovieRequestDto;
import my.Cinema.dtos.UserMovieResponseDto;
import my.Cinema.services.UserMovieService;
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
@RequestMapping("lists")
public class UserMovieController {

    @Autowired
    private UserMovieService userMovieService;

    @PostMapping("/add")
    public ResponseEntity<UserMovieResponseDto> addToList(@Valid @RequestBody UserMovieRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userMovieService.addMovieToList(request));
    }

    @GetMapping("/my-list")
    public ResponseEntity<Page<UserMovieResponseDto>> getMyList(@PageableDefault(page=0, size=10,sort="id", direction= Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userMovieService.getMyList(pageable));
    }

    @GetMapping("/{id}/watchList")
    public ResponseEntity<Page<UserMovieResponseDto>> getWatchList(@PathVariable(value="id")Long id, @PageableDefault(page=0, size=10,sort="id", direction= Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userMovieService.lookListOfOthersUsers(id,pageable));
    }

}
