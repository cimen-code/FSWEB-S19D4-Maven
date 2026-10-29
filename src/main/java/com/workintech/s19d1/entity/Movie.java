package com.workintech.s19d1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movies", schema = "fsweb")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "director_name", nullable = false)
    private String directorName;

    private Integer rating;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToMany(
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JoinTable(
        name = "movie_actors",
        schema = "fsweb",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @JsonIgnoreProperties("movies")
    private List<Actor> actors = new ArrayList<>();

    public void addActor(Actor actor) {

        if (actor == null) {
            return;
        }

        if (actors == null) {
            actors = new ArrayList<>();
        }

        boolean exists = actors.stream().anyMatch(a ->
            a == actor ||
            (
                a.getId() != null &&
                a.getId().equals(actor.getId())
            )
        );

        if (!exists) {
            actors.add(actor);
        }

        if (actor.getMovies() == null) {
            actor.setMovies(new ArrayList<>());
        }

        boolean reverseExists =
            actor.getMovies().stream().anyMatch(m ->
                m == this ||
                (
                    m.getId() != null &&
                    m.getId().equals(id)
                )
            );

        if (!reverseExists) {
            actor.getMovies().add(this);
        }
    }

    public void removeActor(Actor actor) {

        if (actor == null) {
            return;
        }

        if (actors != null) {
            actors.removeIf(a ->
                a == actor ||
                (
                    a.getId() != null &&
                    a.getId().equals(actor.getId())
                )
            );
        }

        if (actor.getMovies() != null) {
            actor.getMovies().removeIf(m ->
                m == this ||
                (
                    m.getId() != null &&
                    m.getId().equals(id)
                )
            );
        }
    }
}
