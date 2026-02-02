package com.revplay.dao;

import com.revplay.model.Album;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDaoImpl implements IAlbumDao {

    @Override
    public boolean createAlbum(Album album) {
        String sql = "INSERT INTO ALBUM (artist_id, title, release_date, description, cover_image_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, album.getArtistId());
            ps.setString(2, album.getTitle());
            ps.setDate(3, Date.valueOf(album.getReleaseDate()));
            ps.setString(4, album.getDescription());
            ps.setString(5, album.getCoverImageUrl());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in createAlbum: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error creating album", e);
        }
    }

    @Override
    public Album getAlbumById(int albumId) {
        String sql = "SELECT * FROM ALBUM WHERE album_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToAlbum(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAlbumById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching album by ID", e);
        }
        return null;
    }

    @Override
    public List<Album> getAlbumsByArtistId(int artistId) {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM ALBUM WHERE artist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                albums.add(mapResultSetToAlbum(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAlbumsByArtistId: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching albums by artist", e);
        }
        return albums;
    }

    @Override
    public boolean deleteAlbum(int albumId) {
        String sql = "DELETE FROM ALBUM WHERE album_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, albumId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in deleteAlbum: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error deleting album", e);
        }
    }

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM ALBUM";
        try (Connection conn = JDBCUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                albums.add(mapResultSetToAlbum(rs));
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAllAlbums: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching all albums", e);
        }
        return albums;
    }

    private Album mapResultSetToAlbum(ResultSet rs) throws SQLException {
        Album album = new Album();
        album.setAlbumId(rs.getInt("album_id"));
        album.setArtistId(rs.getInt("artist_id"));
        album.setTitle(rs.getString("title"));
        album.setReleaseDate(rs.getDate("release_date").toLocalDate());
        album.setDescription(rs.getString("description"));
        album.setCoverImageUrl(rs.getString("cover_image_url"));
        return album;
    }
}
