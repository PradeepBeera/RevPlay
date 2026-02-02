package com.revplay.service;

import com.revplay.dao.ISongDao;
import com.revplay.dao.SongDaoImpl;
import com.revplay.dao.IListeningHistoryDao;
import com.revplay.dao.ListeningHistoryDaoImpl;
import com.revplay.model.Song;
import com.revplay.model.ListeningHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SongServiceImpl implements ISongService {

    private ISongDao songDao = new SongDaoImpl();
    private IListeningHistoryDao historyDao = new ListeningHistoryDaoImpl();

    public void setSongDao(ISongDao songDao) {
        this.songDao = songDao;
    }

    public void setHistoryDao(IListeningHistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    public boolean uploadSong(Song song) {
        if (song.getReleaseDate() == null) {
            song.setReleaseDate(LocalDate.now());
        }
        // Do NOT overwrite release date if already set by Controller
        song.setPlayCount(0);
        song.setIsActive("ACTIVE");
        return songDao.addSong(song);
    }

    @Override
    public List<Song> getAllSongs() {
        return ((SongDaoImpl) songDao).getAllSongs();
    }

    @Override
    public List<Song> getArtistSongs(int artistId) {
        return songDao.getSongsByArtistId(artistId);
    }

    @Override
    public List<Song> search(String keyword) {
        return songDao.searchSongs(keyword);
    }

    @Override
    public boolean playSong(int songId) {
        return playSongLogic(songId);
    }

    @Override
    public boolean playSong(int songId, int userId) {
        boolean played = playSongLogic(songId);
        if (played) {
            ListeningHistory history = new ListeningHistory(0, userId, songId, LocalDateTime.now(), "PLAYED");
            historyDao.addHistory(history);
        }
        return played;
    }

    private boolean playSongLogic(int songId) {
        Song song = songDao.getSongById(songId);
        if (song != null) {
            System.out.println("🎶 Now Playing: " + song.getTitle());
            if (song.getFileUrl() != null && !song.getFileUrl().isEmpty()) {
                System.out.println("   Source: " + song.getFileUrl());
            } else {
                System.out.println("   (Simulation: Audio playing...)");
            }
            songDao.incrementPlayCount(songId);
            return true;
        } else {
            System.out.println("Song not found.");
            return false;
        }
    }

    @Override
    public Song getSongDetails(int songId) {
        return songDao.getSongById(songId);
    }
}
