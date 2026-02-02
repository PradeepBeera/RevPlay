package com.revplay.dao;

import com.revplay.model.ListeningHistory;
import com.revplay.model.Song;
import java.util.List;

public interface IListeningHistoryDao {
    boolean addHistory(ListeningHistory history);

    List<Song> getUserHistory(int userId);
}
