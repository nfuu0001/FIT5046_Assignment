package com.example.fit5046_assignment.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.example.fit5046_assignment.MainActivity;
import com.example.fit5046_assignment.NetworkConnection;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.SignInActivity;
import com.example.fit5046_assignment.entity.Watchlist;
import com.example.fit5046_assignment.viewmodel.WatchlistViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    NetworkConnection networkConnection=null;
    private HomeViewModel homeViewModel;
    private List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    ListView movieList;
    int personId = 0;
    public static WatchlistViewModel watchlistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        networkConnection = new NetworkConnection();
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getActivity().getApplication());
        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String name= sp.getString("first name","Tom");
        //System.out.println("person name------------------------" + name);
        personId = sp.getInt("personId",0);
        //System.out.println("personId------------------------" + personId);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView currentDate = root.findViewById(R.id.current_date);
        TextView firstName = root.findViewById(R.id.firstName);

        movieList = root.findViewById(R.id.top5);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String curDate = "Current date: " + simpleDateFormat.format(date);
        currentDate.setText(curDate);
        String welcome = "Hi " + name + ", welcome !";
        firstName.setText(welcome);
        TopFiveTask topFiveTask = new TopFiveTask();
        topFiveTask.execute(personId);
        return root;
    }

    private class TopFiveTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return networkConnection.getTopFive(params[0]);
        }
        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONArray jsonArray = null;
            try{
                jsonArray = new JSONArray(record);
                for (int i=0; i<jsonArray.length();i++){
                    HashMap<String, String> maps = new HashMap<>();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String movieName = jsonObject.getString("movieName");
                    String releaseDate = jsonObject.getString("releaseDate");
                    String ratingScore = jsonObject.getString("ratingScore");
                    maps.put("movieName",movieName);
                    maps.put("releaseDate",releaseDate);
                    maps.put("ratingScore",ratingScore);
                    list.add(maps);
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
            SimpleAdapter adapter = new SimpleAdapter(HomeFragment.this.getActivity(), list,R.layout.listview_top5,
                    new String[]{"movieName","releaseDate","ratingScore"},
                    new int[]{R.id.movie_name,R.id.release_date,R.id.rating_score});
            movieList.setAdapter(adapter);
        }
    }


}
