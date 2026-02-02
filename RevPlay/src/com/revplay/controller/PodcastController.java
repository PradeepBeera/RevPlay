package com.revplay.controller;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import com.revplay.service.PodcastEpisodeServiceImpl;
import com.revplay.service.PodcastServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class PodcastController {

    private PodcastServiceImpl podcastService = new PodcastServiceImpl();
    private PodcastEpisodeServiceImpl episodeService = new PodcastEpisodeServiceImpl();
    private Scanner sc = new Scanner(System.in);

    public void start() {
        int choice;
        do {
            System.out.println("\n===== PODCAST MODULE =====");
            System.out.println("1. Create Podcast");
            System.out.println("2. View Podcasts");
            System.out.println("3. Update Podcast");
            System.out.println("4. Delete Podcast");
            System.out.println("5. Add Episode");
            System.out.println("6. View Episodes");
            System.out.println("7. Play Episode");
            System.out.println("8. Search Podcast");
            System.out.println("9. Search Episodes by Podcast Name");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> createPodcast();
                case 2 -> viewPodcasts();
                case 3 -> updatePodcast();
                case 4 -> deletePodcast();
                case 5 -> addEpisode();
                case 6 -> viewEpisodes();
                case 7 -> playEpisode();
                case 8 -> searchPodcast();
                case 9 -> searchEpisodes();
            }
        } while (choice != 0);
    }

    private void createPodcast() {
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Host: ");
        String host = sc.nextLine();
        System.out.print("Category: ");
        String cat = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();

        podcastService.createPodcast(
                new Podcast(0, title, host, cat, desc, LocalDateTime.now()));
    }

    private void viewPodcasts() {
        podcastService.getAllPodcasts()
                .forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));
    }

    private void updatePodcast() {
        System.out.print("Podcast ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("New Title: ");
        String title = sc.nextLine();
        System.out.print("New Host: ");
        String host = sc.nextLine();
        System.out.print("New Category: ");
        String cat = sc.nextLine();
        System.out.print("New Description: ");
        String desc = sc.nextLine();

        podcastService.updatePodcast(new Podcast(id, title, host, cat, desc, null));
    }

    private void deletePodcast() {
        System.out.print("Podcast ID: ");
        podcastService.deletePodcast(sc.nextInt());
    }

    private void addEpisode() {
        System.out.print("Podcast ID: ");
        int pid = sc.nextInt(); sc.nextLine();
        System.out.print("Episode Title: ");
        String title = sc.nextLine();
        System.out.print("Duration: ");
        int dur = sc.nextInt(); sc.nextLine();
        System.out.print("URL: ");
        String url = sc.nextLine();

        episodeService.addEpisode(
                new PodcastEpisode(0, pid, title, dur, LocalDate.now(), url, 0, LocalDateTime.now()));
    }

    private void viewEpisodes() {
        System.out.print("Podcast ID: ");
        int pid = sc.nextInt();
        episodeService.getEpisodesByPodcast(pid)
                .forEach(e -> System.out.println(e.getEpisodeId() + " | " + e.getTitle()));
    }

    private void playEpisode() {
        System.out.print("Episode ID: ");
        episodeService.playEpisode(sc.nextInt());
    }

    private void searchPodcast() {
        System.out.print("Enter podcast name: ");
        podcastService.searchPodcastByTitle(sc.nextLine())
                .forEach(p -> System.out.println(p.getPodcastId() + " | " + p.getTitle()));
    }

    private void searchEpisodes() {
        System.out.print("Enter podcast name: ");
        episodeService.searchEpisodesByPodcastTitle(sc.nextLine())
                .forEach(e -> System.out.println(e.getEpisodeId() + " | " + e.getTitle()));
    }
}
