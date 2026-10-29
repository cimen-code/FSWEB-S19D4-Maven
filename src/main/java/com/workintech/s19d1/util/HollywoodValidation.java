package com.workintech.s19d1.util;

import com.workintech.s19d1.entity.Actor;
import com.workintech.s19d1.entity.Movie;
import com.workintech.s19d1.exceptions.ApiException;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public final class HollywoodValidation {

    private HollywoodValidation() {
    }

    public static void validateActor(Actor actor) {

        if (actor == null) {
            throw new ApiException(
                "Actor is required",
                HttpStatus.BAD_REQUEST
            );
        }

        if (actor.getFirstName() == null ||
            actor.getFirstName().isBlank() ||
            actor.getLastName() == null ||
            actor.getLastName().isBlank()) {

            throw new ApiException(
                "Actor firstName and lastName are required",
                HttpStatus.BAD_REQUEST
            );
        }

        if (actor.getGender() == null ||
            actor.getBirthDate() == null ||
            actor.getBirthDate().isAfter(LocalDate.now())) {

            throw new ApiException(
                "Valid gender and birthDate are required",
                HttpStatus.BAD_REQUEST
            );
        }
    }

    public static void validateMovie(Movie movie) {

        if (movie == null) {
            throw new ApiException(
                "Movie is required",
                HttpStatus.BAD_REQUEST
            );
        }

        if (movie.getName() == null ||
            movie.getName().isBlank() ||
            movie.getDirectorName() == null ||
            movie.getDirectorName().isBlank()) {

            throw new ApiException(
                "Movie name and directorName are required",
                HttpStatus.BAD_REQUEST
            );
        }

        if (movie.getRating() == null ||
            movie.getRating() < 0 ||
            movie.getRating() > 10 ||
            movie.getReleaseDate() == null) {

            throw new ApiException(
                "Valid rating and releaseDate are required",
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
