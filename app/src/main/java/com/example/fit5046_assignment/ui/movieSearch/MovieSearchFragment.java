package com.example.fit5046_assignment.ui.movieSearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fit5046_assignment.Movie_View;
import com.example.fit5046_assignment.ui.movieMemoir.MovieMemoirFragment;
import com.example.fit5046_assignment.NetworkConnection;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.SignUpActivity;
import com.example.fit5046_assignment.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieSearchFragment extends Fragment {
    NetworkConnection networkConnection=null;
    private MovieSearchViewModel movieSearchViewModel;
    private EditText et_search;
    private Button bt_search;
    private ListView search_movies;
    private List<HashMap<String,Object>> list;
    private Bitmap bitmapPoster;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieSearchViewModel =
                ViewModelProviders.of(this).get(MovieSearchViewModel.class);
        networkConnection = new NetworkConnection();
        View root = inflater.inflate(R.layout.fragment_movie_search, container, false);

        et_search = root.findViewById(R.id.et_search);
        bt_search = root.findViewById(R.id.bt_search);
        search_movies = root.findViewById(R.id.search_movies);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<HashMap<String,Object>>();
                SearchMovieTask searchMovieTask = new SearchMovieTask();
                searchMovieTask.execute(et_search.getText().toString());
            }
        });
        return root;
    }

    private class SearchMovieTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... params) {
            return networkConnection.movieSearchApi(params[0]);
        }
        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONObject jsonObject = null;

            try{
                jsonObject = new JSONObject(record);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));

                GetPosterTask getPosterTask = new GetPosterTask();
                getPosterTask.execute(jsonArray);

            } catch(JSONException e){
                e.printStackTrace();
            }

        }
    }
    private class GetPosterTask extends AsyncTask<JSONArray, Void, List<HashMap<String,Object>>> {
        @Override
        protected List<HashMap<String,Object>> doInBackground(JSONArray... params) {
            try{
                JSONArray jsonArray = params[0];
                for (int i = 0; i < jsonArray.length(); i++){

                    HashMap<String,Object> map = new HashMap<>();
                    JSONObject jo = (JSONObject) jsonArray.get(i);
                    String poster = jo.getString("poster_path");
                    String date = jo.getString("release_date");

                    if (poster.equals("null") || date.equals("")){
                        continue;
                    } else {
                        map.put("id",jo.getInt("id"));
                        map.put("name",jo.getString("title"));
                        map.put("releaseDate",jo.getString("release_date"));
                        map.put("posterUrl",poster);
                        Bitmap bitmap = networkConnection.moviePoster(poster);
                        //System.out.println("bitmap2    " + bitmap);
                        map.put("posterPath",bitmap);
                        list.add(map);
                    }

                }

            } catch(JSONException e){
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,Object>> result) {
            super.onPostExecute(result);
            SimpleAdapter adapter = new SimpleAdapter(MovieSearchFragment.this.getActivity(), result,R.layout.listview_search_movies,
                    new String[]{"id","name","releaseDate","posterPath"},
                    new int[]{R.id.search_movie_id,R.id.search_movie_name,R.id.search_release_date,R.id.search_image});
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if((view instanceof ImageView) & (data instanceof Bitmap)){
                        ImageView imageView = (ImageView) view;
                        Bitmap bitmap = (Bitmap) data;
                        imageView.setImageBitmap(bitmap);
                        return true;
                    }
                    return false;
                }
            });
            search_movies.setAdapter(adapter);
            search_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = new Movie_View();
                    Bundle bundle = new Bundle();
                    String movieId = list.get(position).get("id").toString();
                    String releaseDate = list.get(position).get("releaseDate").toString();
                    String movieName = list.get(position).get("name").toString();
                    String posterUrl = list.get(position).get("posterUrl").toString();
                    String poster = list.get(position).get("posterPath").toString();

                    bundle.putString("movieId",movieId);
                    bundle.putString("movieName",movieName);
                    bundle.putString("releaseDate",releaseDate);
                    bundle.putString("posterUrl",posterUrl);
                    fragment.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();


                }
            });

        }
    }
}
