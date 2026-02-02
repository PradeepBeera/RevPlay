package com.revplay.service;

import com.revplay.model.Genre;
import java.util.List;

public interface IGenreService {
    List<Genre> getAllGenres();

    Genre getOrCreateGenre(String genreName);
}
