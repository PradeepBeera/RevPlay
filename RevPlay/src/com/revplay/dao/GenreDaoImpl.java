package com.revplay.dao;

import com.revplay.model.Genre;
import com.revplay.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDaoImpl implements IGenreDao {

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM GENRE";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAllGenres: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching all genres", e);
        }
        return genres;
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM GENRE WHERE genre_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getGenreById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching genre by id", e);
        }
        return null;
    }

    @Override
    public Genre getGenreByName(String name) {
        String sql = "SELECT * FROM GENRE WHERE genre_name = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getGenreByName: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching genre by name", e);
        }
        return null;
    }

    @Override
    public boolean addGenre(String genreName) {
        String sql = "INSERT INTO GENRE (genre_name) VALUES (?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genreName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addGenre: " + e.getMessage(), e);
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate")) {
                return false;
            }
            throw new com.revplay.exception.RevPlayException("Error adding genre", e);
        }
    }
}
