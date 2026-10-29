package com.workintech.s19d1.controller;

import com.workintech.s19d1.dto.ActorRequest;
import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.service.ActorService;
import com.workintech.s19d1.util.HollywoodValidation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"/actor", "/actors"})
public class ActorController {

    private final ActorService actorService;

    @GetMapping({"", "/"})
    public List<Actor> findAll() {
        return actorService.findAll();
    }

    @GetMapping("/{id}")
    public Actor findById(@PathVariable Long id) {
        return actorService.findById(id);
    }

    @PostMapping({"", "/"})
    public Actor save(@RequestBody ActorRequest request) {

        Actor actor = request.getActor();
        HollywoodValidation.validateActor(actor);

        if (request.getMovie() != null) {
            actor.addMovie(request.getMovie());
        }

        if (request.getMovies() != null) {

            for (Movie movie : request.getMovies()) {
                actor.addMovie(movie);
            }
        }

        log.debug("Creating actor");
        return actorService.save(actor);
    }

    @PutMapping("/{id}")
    public Actor update(
            @PathVariable Long id,
            @RequestBody Actor input) {

        HollywoodValidation.validateActor(input);

        Actor existing = actorService.findById(id);

        existing.setFirstName(input.getFirstName());
        existing.setLastName(input.getLastName());
        existing.setGender(input.getGender());
        existing.setBirthDate(input.getBirthDate());

        if (input.getMovies() != null) {

            if (existing.getMovies() != null) {

                // Allow removal even if the input list has fixed size.
                existing.setMovies(
                    new ArrayList<>(existing.getMovies())
                );

                for (Movie movie :
                        new ArrayList<>(existing.getMovies())) {

                    existing.removeMovie(movie);
                }
            }

            for (Movie movie : input.getMovies()) {
                existing.addMovie(movie);
            }
        }

        log.debug("Updating actor {}", id);
        return actorService.save(existing);
    }

    @DeleteMapping("/{id}")
    public Actor delete(@PathVariable Long id) {

        Actor existing = actorService.findById(id);

        actorService.delete(existing);

        log.debug("Deleted actor {}", id);
        return existing;
    }
}
