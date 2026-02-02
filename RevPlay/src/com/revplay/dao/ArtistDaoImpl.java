package com.revplay.dao;

import com.revplay.model.ArtistAccount;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDaoImpl implements IArtistDao {

    @Override
    public boolean registerArtist(ArtistAccount artist) {
        String sql = "INSERT INTO ARTIST_ACCOUNT (stage_name, email, password_hash, bio, genre, instagram_link, youtube_link, spotify_link, status, created_at) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, artist.getStageName());
            ps.setString(2, artist.getEmail());
            ps.setString(3, artist.getPasswordHash());
            ps.setString(4, artist.getBio());
            ps.setString(5, artist.getGenre());
            ps.setString(6, artist.getInstagramLink());
            ps.setString(7, artist.getYoutubeLink());
            ps.setString(8, artist.getSpotifyLink());
            ps.setString(9, artist.getStatus());
            ps.setTimestamp(10, Timestamp.valueOf(artist.getCreatedAt()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in registerArtist: " + e.getMessage(), e);
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate")) {
                return false;
            }
            throw new com.revplay.exception.RevPlayException("Error registering artist", e);
        }
    }

    @Override
    public ArtistAccount getArtistByEmail(String email) {
        String sql = "SELECT * FROM ARTIST_ACCOUNT WHERE email = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToArtist(rs);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getArtistByEmail: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching artist by email", e);
        }
        return null;
    }

    @Override
    public ArtistAccount getArtistById(int id) {
        String sql = "SELECT * FROM ARTIST_ACCOUNT WHERE artist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToArtist(rs);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getArtistById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching artist by ID", e);
        }
        return null;
    }

    @Override
    public boolean updateArtistProfile(ArtistAccount artist) {
        String sql = "UPDATE ARTIST_ACCOUNT SET stage_name = ?, password_hash = ?, bio = ?, genre = ?, instagram_link = ?, youtube_link = ?, spotify_link = ? WHERE artist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, artist.getStageName());
            ps.setString(2, artist.getPasswordHash());
            ps.setString(3, artist.getBio());
            ps.setString(4, artist.getGenre());
            ps.setString(5, artist.getInstagramLink());
            ps.setString(6, artist.getYoutubeLink());
            ps.setString(7, artist.getSpotifyLink());
            ps.setInt(8, artist.getArtistId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in updateArtistProfile: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error updating artist profile", e);
        }
    }

    @Override
    public List<ArtistAccount> getAllArtists() {
        List<ArtistAccount> artists = new ArrayList<>();
        String sql = "SELECT * FROM ARTIST_ACCOUNT";
        try (Connection conn = JDBCUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artists.add(mapResultSetToArtist(rs));
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAllArtists: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching all artists", e);
        }
        return artists;
    }

    @Override
    public boolean deleteArtist(int artistId) {
        String sql = "DELETE FROM ARTIST_ACCOUNT WHERE artist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, artistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in deleteArtist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error deleting artist", e);
        }
    }

    private ArtistAccount mapResultSetToArtist(ResultSet rs) throws SQLException {
        ArtistAccount account = new ArtistAccount(
                rs.getInt("artist_id"),
                rs.getString("stage_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("bio"),
                rs.getString("genre"),
                rs.getString("instagram_link"),
                rs.getString("youtube_link"),
                rs.getString("spotify_link"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime());
        return account;
    }
}
