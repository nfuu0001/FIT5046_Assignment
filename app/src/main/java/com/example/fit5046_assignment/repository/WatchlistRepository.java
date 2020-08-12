package com.example.fit5046_assignment.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fit5046_assignment.dao.WatchlistDAO;
import com.example.fit5046_assignment.database.WatchlistDatabase;
import com.example.fit5046_assignment.entity.Watchlist;

import java.util.List;

public class WatchlistRepository {
    private WatchlistDAO dao;
    private LiveData<List<Watchlist>> allWatchlists;
    private Watchlist watchlist;

    public WatchlistRepository(Application application) {
        WatchlistDatabase db = WatchlistDatabase.getInstance(application);
        dao = db.watchlistDao();
    }

    public LiveData<List<Watchlist>> getAllWatchlists() {
        allWatchlists = dao.getAll();
        return allWatchlists;
    }

    public Watchlist findById(final int watchlistId) {
        watchlist = dao.findById(watchlistId);
        return watchlist;
    }

    public void insertAll(final Watchlist... watchlists) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(watchlists);
            }
        });
    }

    public void insert(final Watchlist watchlist) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(watchlist);
            }
        });
    }

    public void deleteAll() {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final Watchlist watchlist) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(watchlist);
            }
        });
    }



    public void updateWatchlists(final Watchlist... watchlists) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateWatchlists(watchlist);
            }
        });
    }

    public void deleteOne(final int movieId) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteOne(movieId);
            }
        });
    }

    public LiveData<List<Watchlist>> findByPersonId(String personId) {
        allWatchlists = dao.findByPersonId(personId);
        return allWatchlists;
    }

    public List<Watchlist> getAllByPersonId(String personId) {
        List<Watchlist> list = dao.getAllByPersonId(personId);
        return list;
    }

}