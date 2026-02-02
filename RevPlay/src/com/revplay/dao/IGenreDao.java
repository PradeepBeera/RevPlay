package com.revplay.dao;

import com.revplay.model.Genre;
import java.util.List;

public interface IGenreDao {
    List<Genre> getAllGenres();

    Genre getGenreById(int id);

    Genre getGenreByName(String name);

    boolean addGenre(String genreName);
}
