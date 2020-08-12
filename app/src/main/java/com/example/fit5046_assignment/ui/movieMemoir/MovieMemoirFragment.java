package com.example.fit5046_assignment.ui.movieMemoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fit5046_assignment.NetworkConnection;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.ui.home.HomeFragment;
import com.example.fit5046_assignment.ui.movieSearch.MovieSearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MovieMemoirFragment extends Fragment {

    private MovieMemoirViewModel movieMemoirViewModel;
    NetworkConnection networkConnection=null;
    private ListView db_movie;
    private int personId;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieMemoirViewModel =
                ViewModelProviders.of(this).get(MovieMemoirViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie_memoir, container, false);

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);

        personId = sp.getInt("personId",0);
        networkConnection = new NetworkConnection();
        db_movie = root.findViewById(R.id.db_movies);

        return root;
    }



    private class GetPersonId extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.findPersonById(params[0]);
        }
        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);


        }
    }

    private class FindAllMovies extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.findPersonById(params[0]);
        }
        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);


        }
    }

}
