package com.revplay.service;

import com.revplay.dao.IPodcastEpisodeDao;
import com.revplay.dao.PodcastEpisodeDaoImpl;
import com.revplay.model.PodcastEpisode;
import java.util.List;

public class PodcastEpisodeServiceImpl {

    private IPodcastEpisodeDao dao = new PodcastEpisodeDaoImpl();

    public void addEpisode(PodcastEpisode e) { dao.addEpisode(e); }
    public List<PodcastEpisode> getEpisodesByPodcast(int id) { return dao.getEpisodesByPodcast(id); }
    public void playEpisode(int id) { dao.playEpisode(id); }
    public List<PodcastEpisode> searchEpisodesByPodcastTitle(String title) {
        return dao.searchEpisodesByPodcastTitle(title);
    }
}
