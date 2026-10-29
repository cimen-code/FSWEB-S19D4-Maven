package com.workintech.s19d1.controller;

import com.workintech.s19d1.dto.MovieRequest;
import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.service.MovieService;
import com.workintech.s19d1.util.HollywoodValidation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"/movie", "/movies"})
public class MovieController {

    private final MovieService movieService;

    @GetMapping({"", "/"})
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public Movie findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @PostMapping({"", "/"})
    public Movie save(@RequestBody MovieRequest request) {

        Movie movie = request.getMovie();
        HollywoodValidation.validateMovie(movie);

        if (request.getActor() != null) {
            movie.addActor(request.getActor());
        }

        if (request.getActors() != null) {

            for (Actor actor : request.getActors()) {
                movie.addActor(actor);
            }
        }

        log.debug("Creating movie");
        return movieService.save(movie);
    }

    @PutMapping("/{id}")
    public Movie update(
            @PathVariable Long id,
            @RequestBody Movie input) {

        HollywoodValidation.validateMovie(input);

        Movie existing = movieService.findById(id);

        existing.setName(input.getName());
        existing.setDirectorName(input.getDirectorName());
        existing.setRating(input.getRating());
        existing.setReleaseDate(input.getReleaseDate());

        if (input.getActors() != null) {

            if (existing.getActors() != null) {

                for (Actor actor :
                        new ArrayList<>(existing.getActors())) {

                    existing.removeActor(actor);
                }
            }

            for (Actor actor : input.getActors()) {
                existing.addActor(actor);
            }
        }

        log.debug("Updating movie {}", id);
        return movieService.save(existing);
    }

    @DeleteMapping("/{id}")
    public Movie delete(@PathVariable Long id) {

        Movie existing = movieService.findById(id);

        movieService.delete(existing);

        log.debug("Deleted movie {}", id);
        return existing;
    }
}
