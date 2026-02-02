package com.revplay.service;

import com.revplay.dao.AlbumDaoImpl;
import com.revplay.dao.IAlbumDao;
import com.revplay.model.Album;
import com.revplay.util.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlbumServiceImpl implements IAlbumService {

    private IAlbumDao albumDao = new AlbumDaoImpl();

    public void setAlbumDao(IAlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    @Override
    public boolean createAlbum(Album album) {
        return albumDao.createAlbum(album);
    }

    @Override
    public Album getAlbumById(int albumId) {
        return albumDao.getAlbumById(albumId);
    }

    @Override
    public List<Album> getArtistAlbums(int artistId) {
        return albumDao.getAlbumsByArtistId(artistId);
    }

    @Override
    public List<Album> getAllAlbums() {
        return albumDao.getAllAlbums();
    }
}
