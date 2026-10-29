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
@Table(name = "actors", schema = "fsweb")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToMany(
        mappedBy = "actors",
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JsonIgnoreProperties("actors")
    private List<Movie> movies = new ArrayList<>();

    public void addMovie(Movie movie) {

        if (movie == null) {
            return;
        }

        if (movies == null) {
            movies = new ArrayList<>();
        }

        boolean exists = movies.stream().anyMatch(m ->
            m == movie ||
            (
                m.getId() != null &&
                m.getId().equals(movie.getId())
            )
        );

        if (!exists) {
            movies.add(movie);
        }

        if (movie.getActors() == null) {
            movie.setActors(new ArrayList<>());
        }

        boolean reverseExists =
            movie.getActors().stream().anyMatch(a ->
                a == this ||
                (
                    a.getId() != null &&
                    a.getId().equals(id)
                )
            );

        if (!reverseExists) {
            movie.getActors().add(this);
        }
    }

    public void removeMovie(Movie movie) {

        if (movie == null) {
            return;
        }

        if (movies != null) {
            movies.removeIf(m ->
                m == movie ||
                (
                    m.getId() != null &&
                    m.getId().equals(movie.getId())
                )
            );
        }

        if (movie.getActors() != null) {
            movie.getActors().removeIf(a ->
                a == this ||
                (
                    a.getId() != null &&
                    a.getId().equals(id)
                )
            );
        }
    }
}
