package com.example.fit5046_assignment.ui.watchlist;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.fit5046_assignment.Movie_View;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.entity.Watchlist;
import com.example.fit5046_assignment.ui.home.HomeFragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WatchlistFragment extends Fragment {

    private WatchlistViewModel watchlistViewModel;
    private ListView listView;
    private TextView tv_selected;
    private Button bt_delete;
    private Button bt_view;
    private String movieId;
    private String  watchlistId;
    private List<HashMap<String,String>> list = new ArrayList<>();
    private String personId;
    SimpleAdapter adapter;
    private String movieName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        watchlistViewModel =
                ViewModelProviders.of(this).get(WatchlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        personId = String.valueOf(sp.getInt("personId",0));
        bt_delete = root.findViewById(R.id.bt_delete);
        bt_view = root.findViewById(R.id.bt_view);
        listView = root.findViewById(R.id.listview_watchlist);
        tv_selected = root.findViewById(R.id.tv_selected);

        ReadWatchlist readWatchlist = new ReadWatchlist();
        readWatchlist.execute();

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Movie_View();
                Bundle bundle = new Bundle();
                bundle.putString("movieId",movieId);
                bundle.putString("movieName",movieName);
                bundle.putString("disabled","disabled");

                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();

            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertInfo = new AlertDialog.Builder(WatchlistFragment.this.getActivity());
                alertInfo.setTitle("Are you sure to delete?");
                alertInfo.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int movie_id = Integer.valueOf(movieId);
                        HomeFragment.watchlistViewModel.deleteOne(movie_id);
                        tv_selected.setText("");
                        Toast.makeText(getContext(),"Delete successfully",Toast.LENGTH_SHORT).show();
                        int size=list.size();
                        if(size>0){
                            System.out.println(size);
                            list.removeAll(list);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                        ReadWatchlist readWatchlist1 = new ReadWatchlist();
                        readWatchlist1.execute();
                    }
                });
                alertInfo.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertInfo.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                movieId = list.get(position).get("movieId");
                movieName = list.get(position).get("movieName");
                tv_selected.setText(movieName + " is chosen");
            }
        });


/*
        HomeFragment.watchlistViewModel.getAllByPersonId(personId).observe(getViewLifecycleOwner(), new Observer<List<Watchlist>>(){

        });

/*
        List<Watchlist> watchlists = HomeFragment.watchlistViewModel.getAllByPersonId(personId);
        for (int i =0; i< watchlists.size();i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("movieName",watchlists.get(i).getMovieName());
            map.put("releaseDate",watchlists.get(i).getReleaseDate());
            map.put("addDate",watchlists.get(i).getDate());
            map.put("addTime",watchlists.get(i).getTime());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(WatchlistFragment.this.getActivity(), list,R.layout.listview_watchlist,
                new String[]{"movieName","releaseDate","addDate","addTime"},
                new int[]{R.id.watchlist_movie_name,R.id.watchlist_release_date,R.id.watchlist_adddate,R.id.watchlist_addtime});
        listView.setAdapter(adapter);
*/
        return root;
    }

    private class ReadWatchlist extends AsyncTask<Void, Void, List<Watchlist>> {
        @Override
        protected List<Watchlist> doInBackground(Void... params) {
            List<Watchlist> watchlists = HomeFragment.watchlistViewModel.getAllByPersonId(personId);
            return watchlists;
        }

        @Override
        protected void onPostExecute(List<Watchlist> record) {
            super.onPostExecute(record);
            List<Watchlist> watchlists = record;
            for (int i =0; i< watchlists.size();i++){
                HashMap<String,String> map = new HashMap<>();
                map.put("movieId",watchlists.get(i).getMovieId());
                map.put("movieName",watchlists.get(i).getMovieName());
                map.put("releaseDate",watchlists.get(i).getReleaseDate());
                map.put("addDate",watchlists.get(i).getDate());
                map.put("addTime",watchlists.get(i).getTime());
                list.add(map);
            }
            adapter = new SimpleAdapter(WatchlistFragment.this.getActivity(), list,R.layout.listview_watchlist,
                    new String[]{"movieId","movieName","releaseDate","addDate","addTime"},
                    new int[]{R.id.watchlist_movie_id,R.id.watchlist_movie_name,R.id.watchlist_release_date,R.id.watchlist_adddate,R.id.watchlist_addtime});
            listView.setAdapter(adapter);
        }
    }

}
