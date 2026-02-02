package com.revplay.service;

import com.revplay.dao.IGenreDao;
import com.revplay.model.Genre;
import java.util.*;

/**
 * Mock implementation of IGenreDao for unit testing.
 */
public class MockGenreDao implements IGenreDao {
    private Map<Integer, Genre> genres = new HashMap<>();
    private Map<String, Genre> genresByName = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<Genre> getAllGenres() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Genre getGenreById(int id) {
        return genres.get(id);
    }

    @Override
    public Genre getGenreByName(String name) {
        // Case-insensitive lookup
        for (Genre g : genres.values()) {
            if (g.getGenreName().equalsIgnoreCase(name)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public boolean addGenre(String genreName) {
        if (getGenreByName(genreName) != null) {
            return false; // Already exists
        }
        Genre genre = new Genre();
        genre.setGenreId(nextId++);
        genre.setGenreName(genreName);
        genres.put(genre.getGenreId(), genre);
        genresByName.put(genreName.toLowerCase(), genre);
        return true;
    }

    public void clear() {
        genres.clear();
        genresByName.clear();
        nextId = 1;
    }
}
