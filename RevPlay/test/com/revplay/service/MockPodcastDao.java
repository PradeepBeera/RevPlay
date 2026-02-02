package com.revplay.service;

import com.revplay.dao.IPodcastDao;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock implementation of IPodcastDao for unit testing.
 */
public class MockPodcastDao implements IPodcastDao {
    private Map<Integer, Podcast> podcasts = new HashMap<>();
    private Map<Integer, PodcastEpisode> episodes = new HashMap<>();
    private int nextPodcastId = 1;
    private int nextEpisodeId = 1;

    @Override
    public boolean createPodcast(Podcast podcast) {
        podcast.setPodcastId(nextPodcastId++);
        podcasts.put(podcast.getPodcastId(), podcast);
        return true;
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        return new ArrayList<>(podcasts.values());
    }

    @Override
    public Podcast getPodcastById(int id) {
        return podcasts.get(id);
    }

    @Override
    public boolean updatePodcast(Podcast podcast) {
        if (!podcasts.containsKey(podcast.getPodcastId())) {
            return false;
        }
        podcasts.put(podcast.getPodcastId(), podcast);
        return true;
    }

    @Override
    public boolean deletePodcast(int id) {
        return podcasts.remove(id) != null;
    }

    @Override
    public List<Podcast> searchPodcastByTitle(String title) {
        return podcasts.values().stream()
                .filter(p -> p.getTitle() != null && p.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addEpisode(PodcastEpisode episode) {
        if (!podcasts.containsKey(episode.getPodcastId())) {
            return false;
        }
        episode.setEpisodeId(nextEpisodeId++);
        episodes.put(episode.getEpisodeId(), episode);
        return true;
    }

    @Override
    public List<PodcastEpisode> getEpisodesByPodcastId(int podcastId) {
        return episodes.values().stream()
                .filter(e -> e.getPodcastId() == podcastId)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastEpisode getEpisodeById(int episodeId) {
        return episodes.get(episodeId);
    }

    @Override
    public boolean incrementEpisodePlayCount(int episodeId) {
        PodcastEpisode ep = episodes.get(episodeId);
        if (ep == null)
            return false;
        ep.setPlayCount(ep.getPlayCount() + 1);
        return true;
    }

    public void clear() {
        podcasts.clear();
        episodes.clear();
        nextPodcastId = 1;
        nextEpisodeId = 1;
    }
}
