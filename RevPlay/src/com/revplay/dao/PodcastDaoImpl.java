package com.revplay.dao;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PodcastDaoImpl implements IPodcastDao {

    @Override
    public boolean createPodcast(Podcast podcast) {
        String sql = "INSERT INTO PODCAST (title, host_name, category, description, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, podcast.getTitle());
            ps.setString(2, podcast.getHostName());
            ps.setString(3, podcast.getCategory());
            ps.setString(4, podcast.getDescription());
            ps.setTimestamp(5, Timestamp.valueOf(podcast.getCreatedAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in createPodcast: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error creating podcast", e);
        }
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        List<Podcast> list = new ArrayList<>();
        String sql = "SELECT * FROM PODCAST";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapPodcast(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAllPodcasts: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching podcasts", e);
        }
        return list;
    }

    @Override
    public Podcast getPodcastById(int id) {
        String sql = "SELECT * FROM PODCAST WHERE podcast_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapPodcast(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getPodcastById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching podcast by id", e);
        }
        return null;
    }

    @Override
    public boolean updatePodcast(Podcast podcast) {
        String sql = "UPDATE PODCAST SET title=?, host_name=?, category=?, description=? WHERE podcast_id=?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, podcast.getTitle());
            ps.setString(2, podcast.getHostName());
            ps.setString(3, podcast.getCategory());
            ps.setString(4, podcast.getDescription());
            ps.setInt(5, podcast.getPodcastId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in updatePodcast: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error updating podcast", e);
        }
    }

    @Override
    public boolean deletePodcast(int id) {
        String sql = "DELETE FROM PODCAST WHERE podcast_id=?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in deletePodcast: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error deleting podcast", e);
        }
    }

    @Override
    public List<Podcast> searchPodcastByTitle(String title) {
        List<Podcast> list = new ArrayList<>();
        String sql = "SELECT * FROM PODCAST WHERE title LIKE ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapPodcast(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in searchPodcastByTitle: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error searching podcast", e);
        }
        return list;
    }

    @Override
    public boolean addEpisode(PodcastEpisode episode) {
        String sql = "INSERT INTO PODCAST_EPISODE (podcast_id, title, duration_seconds, release_date, file_url, play_count, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, episode.getPodcastId());
            ps.setString(2, episode.getTitle());
            ps.setInt(3, episode.getDurationSeconds());
            ps.setDate(4, Date.valueOf(episode.getReleaseDate()));
            ps.setString(5, episode.getFileUrl());
            ps.setInt(6, episode.getPlayCount());
            ps.setTimestamp(7, Timestamp.valueOf(episode.getCreatedAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addEpisode: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding episode", e);
        }
    }

    @Override
    public List<PodcastEpisode> getEpisodesByPodcastId(int podcastId) {
        List<PodcastEpisode> list = new ArrayList<>();
        String sql = "SELECT * FROM PODCAST_EPISODE WHERE podcast_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, podcastId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapEpisode(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getEpisodesByPodcastId: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching episodes", e);
        }
        return list;
    }

    @Override
    public PodcastEpisode getEpisodeById(int episodeId) {
        String sql = "SELECT * FROM PODCAST_EPISODE WHERE episode_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapEpisode(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getEpisodeById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching episode by id", e);
        }
        return null;
    }

    @Override
    public boolean incrementEpisodePlayCount(int episodeId) {
        String sql = "UPDATE PODCAST_EPISODE SET play_count = play_count + 1 WHERE episode_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in incrementEpisodePlayCount: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error incrementing episode play count", e);
        }
    }

    private Podcast mapPodcast(ResultSet rs) throws SQLException {
        return new Podcast(
                rs.getInt("podcast_id"),
                rs.getString("title"),
                rs.getString("host_name"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }

    private PodcastEpisode mapEpisode(ResultSet rs) throws SQLException {
        return new PodcastEpisode(
                rs.getInt("episode_id"),
                rs.getInt("podcast_id"),
                rs.getString("title"),
                rs.getInt("duration_seconds"),
                rs.getDate("release_date").toLocalDate(),
                rs.getString("file_url"),
                rs.getInt("play_count"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
