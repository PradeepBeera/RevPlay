package com.revplay.service;

import com.revplay.dao.GenreDaoImpl;
import com.revplay.dao.IGenreDao;
import com.revplay.model.Genre;

import java.util.List;

public class GenreServiceImpl implements IGenreService {

    private IGenreDao genreDao = new GenreDaoImpl();

    public void setGenreDao(IGenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Override
    public Genre getOrCreateGenre(String genreName) {
        // Check if exists
        Genre genre = genreDao.getGenreByName(genreName);
        if (genre != null) {
            return genre;
        }

        // If not, create
        boolean created = genreDao.addGenre(genreName);
        if (created) {
            return genreDao.getGenreByName(genreName);
        }

        return null;
    }
}
