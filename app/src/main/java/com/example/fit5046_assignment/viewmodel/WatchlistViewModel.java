package com.example.fit5046_assignment.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fit5046_assignment.entity.Watchlist;
import com.example.fit5046_assignment.repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    private WatchlistRepository cRepository;
    private MutableLiveData<List<Watchlist>> allWatchlists;
    public WatchlistViewModel () {
        allWatchlists=new MutableLiveData<>();
    }
    public void setWatchlists(List<Watchlist> watchlists) {
        allWatchlists.setValue(watchlists);
    }
    public LiveData<List<Watchlist>> getAllWatchlists() {
        return cRepository.getAllWatchlists();
    }
    public void initalizeVars(Application application){
        cRepository = new WatchlistRepository(application);
    }
    public void insert(Watchlist watchlist) {
        cRepository.insert(watchlist);
    }
    public void insertAll(Watchlist... watchlists) {
        cRepository.insertAll(watchlists);
    }
    public void deleteAll() {
        cRepository.deleteAll();
    }
    public void update(Watchlist... watchlists) {
        cRepository.updateWatchlists(watchlists);
    }
    public LiveData<List<Watchlist>> findByPersonId(String personId) { return cRepository.findByPersonId(personId); }
    public Watchlist findById(int watchlistId){ return cRepository.findById(watchlistId); }
    public List<Watchlist> getAllByPersonId(String personId) {return cRepository.getAllByPersonId(personId);}
    public void deleteOne(int movieId) { cRepository.deleteOne(movieId); }
}