package com.workintech.s19d1.service;

import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.exceptions.ApiException;
import com.workintech.s19d1.repository.ActorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Actor findById(Long id) {

        return actorRepository.findById(id)
            .orElseThrow(() -> new ApiException(
                "actor is not found with id: " + id,
                HttpStatus.NOT_FOUND
            ));
    }

    @Override
    public Actor save(Actor actor) {

        if (actor.getMovies() != null) {

            for (Movie movie :
                    new ArrayList<>(actor.getMovies())) {

                actor.addMovie(movie);
            }
        }

        return actorRepository.save(actor);
    }

    @Override
    public void delete(Actor actor) {

        if (actor.getMovies() != null) {

            for (Movie movie :
                    new ArrayList<>(actor.getMovies())) {

                actor.removeMovie(movie);
            }
        }

        actorRepository.delete(actor);
    }
}
