package com.example.fit5046_assignment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddCinemaFragment extends Fragment {
    NetworkConnection networkConnection = null;
    private EditText add_cinema_name;
    private EditText add_cinema_postcode;
    private Button bt_add_cinema;
    View root;
    private String movieId;
    private String movieName;
    private String releaseDate;
    private Button bt_back;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_add_cinema, container, false);

        Bundle bundle = getArguments();
        if (bundle != null){
            movieId = bundle.getString("movieId");
            movieName = bundle.getString("movieName");
            releaseDate = bundle.getString("releaseDate");
        }

        networkConnection = new NetworkConnection();
        add_cinema_name = root.findViewById(R.id.add_cinema_name);
        add_cinema_postcode = root.findViewById(R.id.add_cinema_postcode);
        bt_add_cinema = root.findViewById(R.id.bt_add_cinema);
        bt_back = root.findViewById(R.id.bt_back);

        bt_add_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCinemaId checkCinemaId = new CheckCinemaId();
                checkCinemaId.execute();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddToMemoirFragment();
                Bundle bundle = new Bundle();
                bundle.putString("movieId",movieId);
                bundle.putString("movieName",movieName);
                bundle.putString("releaseDate",releaseDate);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });


        return root;
    }

    private class CheckCinemaId extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return networkConnection.getAllCinemas();
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONArray jsonArray = null;
            boolean flag = true;
            try{
                jsonArray = new JSONArray(record);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    if (jsonObject.get("cinemaName").equals(add_cinema_name.getText().toString())){
                        Toast.makeText(root.getContext(),"The cinema has existed",Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
            if (flag == true){
                GetNewCinemaId getNewCinemaId = new GetNewCinemaId();
                getNewCinemaId.execute();
            }


        }

    }


    private class GetNewCinemaId extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return networkConnection.getMaxCinemaId();
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONObject jsonObject = null;
            String nextId = "";
            try{
                jsonObject = new JSONObject(record);
                nextId = jsonObject.getString("cinemaId");
            } catch(JSONException e){
                e.printStackTrace();
            }
            int id = Integer.parseInt(nextId);
            Cinema cinema = new Cinema(id+1,add_cinema_name.getText().toString(),add_cinema_postcode.getText().toString());
            AddCinemaTask addCinemaTask = new AddCinemaTask();
            addCinemaTask.execute(cinema);
        }

    }

    private class AddCinemaTask extends AsyncTask<Cinema, Void, String> {
        @Override
        protected String doInBackground(Cinema... params) {

            return networkConnection.addCinema(params[0]) ;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(root.getContext(),"Add successfully",Toast.LENGTH_SHORT).show();
        }
    }


}
