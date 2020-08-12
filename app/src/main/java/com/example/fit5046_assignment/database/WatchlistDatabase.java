package com.example.fit5046_assignment.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fit5046_assignment.dao.WatchlistDAO;
import com.example.fit5046_assignment.entity.Watchlist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Watchlist.class}, version = 2, exportSchema = false)
public abstract class WatchlistDatabase extends RoomDatabase {
    public abstract WatchlistDAO watchlistDao();
    private static WatchlistDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized WatchlistDatabase getInstance(final Context
                                                                    context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchlistDatabase.class, "WatchlistDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}