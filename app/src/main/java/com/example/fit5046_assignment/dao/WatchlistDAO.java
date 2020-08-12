package com.example.fit5046_assignment.dao;

//import android.lifecycle.LivaDate;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fit5046_assignment.entity.Watchlist;
import static  androidx.room.OnConflictStrategy.REPLACE;
import java.util.List;

@Dao
public interface WatchlistDAO {
    @Query("SELECT * FROM watchlist")
    LiveData<List<Watchlist>> getAll();
    @Query("SELECT * FROM watchlist WHERE wid = :watchlistId LIMIT 1")
    Watchlist findById(int watchlistId);
    @Insert
    void insertAll(Watchlist... watchlists);
    @Insert
    long insert(Watchlist watchlist);
    @Delete
    void delete(Watchlist watchlist);

    @Update(onConflict = REPLACE)
    void updateWatchlists(Watchlist... watchlists);
    @Query("DELETE FROM watchlist")
    void deleteAll();

    @Query("DELETE FROM watchlist WHERE movie_id = :movieId")
    void deleteOne(int movieId);

    @Query("SELECT * FROM watchlist WHERE person_id = :perId")
    LiveData<List<Watchlist>> findByPersonId(String perId);
    @Query("SELECT * FROM watchlist WHERE person_id = :perId")
    List<Watchlist> getAllByPersonId(String perId);
}