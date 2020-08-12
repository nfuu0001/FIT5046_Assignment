package com.example.fit5046_assignment.ui.movieMemoir;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieMemoirViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MovieMemoirViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is movie memoir fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}