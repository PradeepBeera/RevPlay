package com.revplay.service;

import com.revplay.dao.IPodcastDao;
import com.revplay.dao.PodcastDaoImpl;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;

import java.util.List;

public class PodcastServiceImpl implements IPodcastService {

    private IPodcastDao podcastDao = new PodcastDaoImpl();

    public void setPodcastDao(IPodcastDao podcastDao) {
        this.podcastDao = podcastDao;
    }

    @Override
    public boolean createPodcast(Podcast podcast) {
        return podcastDao.createPodcast(podcast);
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        return podcastDao.getAllPodcasts();
    }

    @Override
    public boolean updatePodcast(Podcast podcast) {
        return podcastDao.updatePodcast(podcast);
    }

    @Override
    public boolean deletePodcast(int id) {
        return podcastDao.deletePodcast(id);
    }

    @Override
    public List<Podcast> searchPodcastByTitle(String title) {
        return podcastDao.searchPodcastByTitle(title);
    }

    @Override
    public boolean addEpisode(PodcastEpisode episode) {
        return podcastDao.addEpisode(episode);
    }

    @Override
    public List<PodcastEpisode> getPodcastEpisodes(int podcastId) {
        return podcastDao.getEpisodesByPodcastId(podcastId);
    }

    @Override
    public boolean playEpisode(int episodeId) {
        PodcastEpisode ep = podcastDao.getEpisodeById(episodeId);
        if (ep != null) {
            System.out.println("▶️ Listening to Podcast Episode: " + ep.getTitle());
            System.out.println("   File: " + ep.getFileUrl());
            return podcastDao.incrementEpisodePlayCount(episodeId);
        }
        return false;
    }
}
