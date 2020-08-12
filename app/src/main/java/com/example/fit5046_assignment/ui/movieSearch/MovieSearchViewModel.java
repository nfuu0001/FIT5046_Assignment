package com.example.fit5046_assignment.ui.movieSearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieSearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MovieSearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is movie search fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}