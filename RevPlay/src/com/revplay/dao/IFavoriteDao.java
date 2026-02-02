package com.revplay.dao;

import com.revplay.model.Song;
import java.util.List;

public interface IFavoriteDao {
    boolean addFavorite(int userId, int songId);

    boolean removeFavorite(int userId, int songId);

    List<Song> getFavoriteSongs(int userId);

    boolean isFavorite(int userId, int songId);
}
