package com.example.fit5046_assignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fit5046_assignment.entity.Watchlist;
import com.example.fit5046_assignment.ui.home.HomeFragment;
import com.example.fit5046_assignment.ui.movieMemoir.MovieMemoirFragment;
import com.example.fit5046_assignment.ui.movieMemoir.MovieMemoirViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Movie_View extends Fragment {
    private MovieMemoirViewModel movieMemoirViewModel;
    NetworkConnection networkConnection = null;
    private String movieId;
    private String name;
    private TextView movieName;
    private TextView genre;
    private TextView releaseDate;
    private TextView country;
    private TextView directors;
    private TextView plot;
    private TextView cast;
    private RatingBar rating;
    private Button bt_watchlist;
    private Button bt_memoir;
    View root;
    private int personId;
    private String pictureUrl;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_movie_view, container, false);
        networkConnection = new NetworkConnection();
        movieName = root.findViewById(R.id.movie_name);
        genre = root.findViewById(R.id.movie_genre);
        releaseDate = root.findViewById(R.id.movie_release_date);
        country = root.findViewById(R.id.movie_country);
        directors = root.findViewById(R.id.movie_director);
        plot = root.findViewById(R.id.movie_plot);
        cast = root.findViewById(R.id.movie_cast);
        rating = root.findViewById(R.id.rating);
        bt_watchlist = root.findViewById(R.id.bt_watchlist);
        bt_memoir = root.findViewById(R.id.bt_memoir);

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        personId = sp.getInt("personId",0);
        String disabled = "";
        Bundle bundle = getArguments();
        pictureUrl = bundle.getString("posterUrl");
        movieId = bundle.getString("movieId");
        name = bundle.getString("movieName");
        disabled = bundle.getString("disabled");

        if ("disabled".equals(disabled)){
            bt_watchlist.setVisibility(View.INVISIBLE);

        }

        movieName.setText(name);
        GetMovieInfoTask getMovieInfoTask = new GetMovieInfoTask();
        getMovieInfoTask.execute(movieId);

        bt_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertTask insertTask = new InsertTask();
                insertTask.execute(String.valueOf(personId));
            }
        });

        bt_memoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddToMemoirFragment();
                Bundle bundle = new Bundle();
                bundle.putString("movieId",movieId);
                bundle.putString("movieName",movieName.getText().toString());
                bundle.putString("releaseDate",releaseDate.getText().toString());
                bundle.putString("posterUrl",pictureUrl);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });

        return root;
    }

    private class GetMovieInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMovieInfo(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(record);

                JSONArray jsonArray = new JSONArray(jsonObject.getString("genres"));
                String genreList = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = (JSONObject) jsonArray.get(i);
                    genreList = genreList + jo.getString("name") + " ";
                }
                genre.setText(genreList);

                //release_date
                releaseDate.setText(jsonObject.getString("release_date"));

                //production_countries
                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("production_countries"));
                String countryList = "";
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jo = (JSONObject) jsonArray.get(i);
                    countryList = countryList + jo.getString("name");
                }
                country.setText(countryList);

                //overview
                plot.setText(jsonObject.getString("overview"));

                ////vote_average
                double d = jsonObject.getDouble("vote_average");
                float f = (float) d;
                rating.setRating(f);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            GetMovieMoreInfoTask getMovieMoreInfoTask = new GetMovieMoreInfoTask();
            getMovieMoreInfoTask.execute(movieId);
        }
    }

    private class GetMovieMoreInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMovieMoreInfo(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(record);

                // cast is too much, choose first 10
                JSONArray jsonArray = new JSONArray(jsonObject.getString("cast"));
                String castList = "";
                if (jsonArray.length() > 10) {
                    for (int i = 0; i < 10; i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        castList = castList + jo.getString("name") + " ";
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        castList = castList + jo.getString("name") + " ";
                    }
                }
                cast.setText(castList);

                //crew  job
                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("crew"));
                String directorList = "";
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jo = (JSONObject) jsonArray1.get(i);
                    if (jo.getString("job").equals("Director")) {
                        directorList = directorList + jo.getString("name") + " ";
                    }
                }
                if (directorList.equals("")) {
                    directors.setText("Not provided");
                } else {
                    directors.setText(directorList);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class InsertTask extends AsyncTask<String, Void, List<Watchlist>> {
        @Override
        protected List<Watchlist> doInBackground(String... params) {
            List<Watchlist> watchlists = HomeFragment.watchlistViewModel.getAllByPersonId(params[0]);
            return watchlists;
        }

        @Override
        protected void onPostExecute(List<Watchlist> record) {
            super.onPostExecute(record);
            boolean exist = false;

            if (record.size() == 0){
                Date date = new Date();
                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
                String date2 = date1.format(date.getTime());
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                String time2 = time.format(date.getTime());
                Watchlist watchlist = new Watchlist(String.valueOf(personId),movieName.getText().toString(), releaseDate.getText().toString(),date2,time2,movieId);
                HomeFragment.watchlistViewModel.insert(watchlist);
                Toast.makeText(root.getContext(),"Add successfully",Toast.LENGTH_SHORT).show();
                exist = true;
            } else {
                for (int i = 0; i < record.size(); i++){
                    if (record.get(i).getMovieId().equals(movieId)){
                        exist = true;
                        Toast.makeText(root.getContext(),"The movie has already existed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (exist == false){
                Date date = new Date();
                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
                String date2 = date1.format(date.getTime());
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                String time2 = time.format(date.getTime());
                Watchlist watchlist = new Watchlist(String.valueOf(personId),movieName.getText().toString(), releaseDate.getText().toString(),date2,time2,movieId);
                HomeFragment.watchlistViewModel.insert(watchlist);
                Toast.makeText(root.getContext(),"Add successfully",Toast.LENGTH_SHORT).show();
            }

        }
    }
}