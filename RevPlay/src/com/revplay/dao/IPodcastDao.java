package com.revplay.dao;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import java.util.List;

public interface IPodcastDao {
    boolean createPodcast(Podcast podcast);

    List<Podcast> getAllPodcasts();

    Podcast getPodcastById(int id);

    boolean updatePodcast(Podcast podcast);

    boolean deletePodcast(int id);

    List<Podcast> searchPodcastByTitle(String title);

    boolean addEpisode(PodcastEpisode episode);

    List<PodcastEpisode> getEpisodesByPodcastId(int podcastId);

    PodcastEpisode getEpisodeById(int episodeId);

    boolean incrementEpisodePlayCount(int episodeId);
}
