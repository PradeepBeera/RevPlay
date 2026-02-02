package com.revplay.service;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import java.util.List;

public interface IPodcastService {
    boolean createPodcast(Podcast podcast);

    List<Podcast> getAllPodcasts();

    boolean updatePodcast(Podcast podcast);

    boolean deletePodcast(int id);

    List<Podcast> searchPodcastByTitle(String title);

    boolean addEpisode(PodcastEpisode episode);

    List<PodcastEpisode> getPodcastEpisodes(int podcastId);

    boolean playEpisode(int episodeId);
}
