package org.ec;

import io.quarkus.test.junit.QuarkusTest;
import org.ec.entity.Movie;
import org.ec.repository.MovieRepository;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MovieRepositoryTest {

    @Inject
    MovieRepository movieRepository;

    @Test
    void findByCountryOK() {
        List<Movie> movies = movieRepository.findByCountry("USA");
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals(2, movies.size());
        assertEquals(2L, movies.get(0).getId());
        assertEquals("SecondMovie", movies.get(0).getTitle());
        assertEquals("MySecondMovie", movies.get(0).getDescription());
        assertEquals("USA", movies.get(0).getCountry());
        assertEquals("Me", movies.get(0).getDirector());
    }

    @Test
    void findByCountryKO() {
        List<Movie> movies = movieRepository.findByCountry("Earth");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
    }
}