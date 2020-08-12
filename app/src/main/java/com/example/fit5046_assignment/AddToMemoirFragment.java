package com.example.fit5046_assignment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fit5046_assignment.entity.Watchlist;
import com.example.fit5046_assignment.ui.home.HomeFragment;
import com.example.fit5046_assignment.ui.movieSearch.MovieSearchFragment;
import com.example.fit5046_assignment.ui.watchlist.WatchlistFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class AddToMemoirFragment extends Fragment {
    NetworkConnection networkConnection = null;
    private TextView tx_memoir_moviename;
    private TextView tx_memoir_releasedate;
    private ImageView memoir_image;
    private TextView tv_memoir_date;
    private TextView tv_memoir_time;
    private TextView tx_cinema;
    private TextView tv_memoir_score;
    private Spinner spinner_cinema;
    private EditText et_memoir_comment;
    private RatingBar memoir_ratingbar;
    private Button bt_memoir_date;
    private Button bt_add_cinema;
    private Button bt_memoir_submit;
    private String movieName;
    private String releaseDate;
    private String movieId;
    private String posterUrl;
    View root;
    private int personId;
    private String dateWatched;
    private String timeWatched;
    private int memoirId;
    private String cinemaName;
    private Cinema cinema;
    private String comment;
    private Memoir memoir;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_add_memoir, container, false);
        networkConnection = new NetworkConnection();

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        personId = sp.getInt("personId",0);


        tx_memoir_moviename = root.findViewById(R.id.tx_memoir_moviename);
        tx_memoir_releasedate = root.findViewById(R.id.tx_memoir_releasedate);
        memoir_image = root.findViewById(R.id.memoir_image);
        tv_memoir_date = root.findViewById(R.id.tv_memoir_date);
        tv_memoir_time = root.findViewById(R.id.tv_memoir_time);
        tx_cinema = root.findViewById(R.id.tx_cinema);
        spinner_cinema = root.findViewById(R.id.spinner_cinema);
        et_memoir_comment = root.findViewById(R.id.et_memoir_comment);
        memoir_ratingbar = root.findViewById(R.id.memoir_ratingbar);
        bt_memoir_date = root.findViewById(R.id.bt_memoir_date);
        bt_add_cinema = root.findViewById(R.id.bt_add_cinema);
        bt_memoir_submit = root.findViewById(R.id.bt_memoir_submit);
        tv_memoir_score = root.findViewById(R.id.tv_memoir_score);

        Bundle bundle = getArguments();
        if (bundle != null){
            movieId = bundle.getString("movieId");
            System.out.println("记录 " + movieId);
            movieName = bundle.getString("movieName");
            releaseDate = bundle.getString("releaseDate");
        }



        tx_memoir_moviename.setText(movieName);
        tx_memoir_releasedate.setText(releaseDate);
        GetPosterUrl getPosterUrl = new GetPosterUrl();
        getPosterUrl.execute(movieId);

        bt_add_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddCinemaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("movieId",movieId);
                bundle.putString("movieName",movieName);
                bundle.putString("releaseDate",releaseDate);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
            }
        });

        memoir_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv_memoir_score.setText(String.valueOf(rating));
            }
        });

        bt_memoir_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoir = new Memoir();
                comment = et_memoir_comment.getText().toString();
                memoir.setComment(comment);
                memoir.setMovieName(movieName);
                memoir.setReleaseDate(releaseDate);
                memoir.setDateWatched(dateWatched);
                memoir.setTimeWatched(timeWatched);
                String score = tv_memoir_score.getText().toString();
                float rating = Float.parseFloat(score);
                memoir.setRatingScore(rating);
                GetNewMemoirId getNewMemoirId = new GetNewMemoirId();
                getNewMemoirId.execute();

            }
        });

        cinemaName = (String) spinner_cinema.getSelectedItem();
        spinner_cinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cinemaName = spinner_cinema.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cinemaName = "Waverley Cinema";
            }
        });

        bt_memoir_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer date = new StringBuffer();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                String time = df.format(new Date());
                tv_memoir_time.setText(time);
                timeWatched = "1970-01-01T" + time + "+08:00";
                date.append(year);
                date.append("-");

                if (month < 10) {
                    date.append("0");
                    date.append(month);
                } else {
                    date.append(month);
                }

                date.append("-");

                if (day < 10) {
                    date.append(0);
                    date.append(day);
                } else {
                    date.append(day);
                }
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1,
                                          int day1) {
                        StringBuffer date = new StringBuffer();
                        month1 = month1 + 1;
                        date.append(year1);
                        date.append("-");
                        if (month1 < 10) {
                            date.append("0");
                            date.append(month1);
                        } else {
                            date.append(month1);
                        }
                        date.append("-");
                        if (day1 < 10) {
                            date.append(0);
                            date.append(day1);
                        } else {
                            date.append(day1);
                        }

                        tv_memoir_date.setText(date.toString());
                        dateWatched = date.toString() + "T00:00:00+08:00";
                    }
                }, year, month, day);
                DatePicker dp = datePicker.getDatePicker();
                dp.setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        return root;
    }



    private class GetPosterUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getMovieInfo(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(record);
                posterUrl = jsonObject.getString("poster_path");
            } catch(JSONException e){
                e.printStackTrace();
            }

            GetPosterImage getPosterImage = new GetPosterImage();
            getPosterImage.execute(posterUrl);

        }

    }

    private class GetPosterImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return networkConnection.moviePoster(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap record) {
            super.onPostExecute(record);
            Bitmap bitmap = record;
            memoir_image.setImageBitmap(bitmap);
            ShowSpinner showSpinner = new ShowSpinner();
            showSpinner.execute();
        }

    }

    private class ShowSpinner extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return networkConnection.getAllCinemas();
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            List<String> cinemas = new ArrayList<>();
            try{
                JSONArray jsonArray = new JSONArray(record);
                int size = jsonArray.length();

                for(int i = 0; i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String info = "";
                    info = info + jsonObject.getString("cinemaName") + " " + jsonObject.getString("postcode");
                    cinemas.add(info);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            final ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(root.getContext(),android.R.layout.simple_spinner_item,cinemas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_cinema.setAdapter(adapter);

        }

    }

    private class GetNewMemoirId extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return networkConnection.getMaxMemoirId();
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            JSONObject jsonObject = null;
            String nextId = "";
            try {
                jsonObject = new JSONObject(record);
                nextId = jsonObject.getString("memoirId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int id = Integer.parseInt(nextId);
            memoirId = id + 1;
            memoir.setMemoirId(memoirId);
            GetCinema getCinema = new GetCinema();
            //System.out.println("cinema name----------------" + cinemaName);
            String cinemaName1 = cinemaName.substring(0,cinemaName.length()-5);
            getCinema.execute(cinemaName1);

        }
    }

    private class GetCinema extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.findCinemaByName(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            Cinema cinema = new Cinema();
            record = record.trim().replace("[","");
            record = record.trim().replace("]","");
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(record);
                int cinemaId = jsonObject.getInt("cinemaId");
                String cinemaName = jsonObject.getString("cinemaName");
                String postcode = jsonObject.getString("postcode");
                cinema.setCinemaId(cinemaId);
                cinema.setCinemaName(cinemaName);
                cinema.setPostcode(postcode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            memoir.setCinemaId(cinema);
            GetPerson getPerson = new GetPerson();
            getPerson.execute(Integer.toString(personId));
        }

    }

    private class GetPerson extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.findPersonById(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            Person person = new Person();
            Credential credential = new Credential();
            record = record.trim().replace("[","");
            record = record.trim().replace("]","");
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(record);
                String address = jsonObject.getString("address");
                JSONObject jsonObject1 = null;
                jsonObject1 = new JSONObject(jsonObject.getString("credentialId"));
                int credentialId = jsonObject1.getInt("credentialId");
                String passwordHash = jsonObject1.getString("passwordHash");
                String signUpDate = jsonObject1.getString("signUpDate");
                String username = jsonObject1.getString("username");
                credential.setCredentialId(credentialId);
                credential.setPasswordHash(passwordHash);
                credential.setSignUpDate(signUpDate);
                credential.setUsername(username);
                String dob = jsonObject.getString("dob");
                String fname = jsonObject.getString("fname");
                String gender = jsonObject.getString("gender");
                int personId = jsonObject.getInt("personId");
                String postcode = jsonObject.getString("postcode");
                String state = jsonObject.getString("state");
                String surname = jsonObject.getString("surname");
                person.setAddress(address);
                person.setCredentialId(credential);
                person.setDob(dob);
                person.setFname(fname);
                person.setGender(gender);
                person.setPersonId(personId);
                person.setPostcode(postcode);
                person.setState(state);
                person.setSurname(surname);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            memoir.setPersonId(person);
            AddMemoirTask addMemoirTask = new AddMemoirTask();
            addMemoirTask.execute(memoir);
        }

    }

    private class AddMemoirTask extends AsyncTask<Memoir, Void, String> {
        @Override
        protected String doInBackground(Memoir... params) {

            return networkConnection.addMemoir(params[0]) ;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(root.getContext(),"Submit successfully",Toast.LENGTH_SHORT).show();
        }
    }


}
