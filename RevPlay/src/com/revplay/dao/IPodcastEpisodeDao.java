package com.revplay.dao;

import com.revplay.model.PodcastEpisode;
import java.util.List;

public interface IPodcastEpisodeDao {
    boolean addEpisode(PodcastEpisode episode);

    List<PodcastEpisode> getEpisodesByPodcast(int podcastId);

    void playEpisode(int episodeId);

    List<PodcastEpisode> searchEpisodesByPodcastTitle(String podcastTitle);
}
