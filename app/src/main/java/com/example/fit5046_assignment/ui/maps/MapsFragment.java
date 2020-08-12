package com.example.fit5046_assignment.ui.maps;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fit5046_assignment.NetworkConnection;
import com.example.fit5046_assignment.R;
import com.example.fit5046_assignment.SignUpActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    NetworkConnection networkConnection=null;
    private MapsViewModel mapsViewModel;
    private MapView mView;
    private GoogleMap gMap;
    View root;
    private List<HashMap<Double,Double>> LatLng;
    private Double homeLat;
    private Double homeLng;
    LatLng p1;
    private List<Address> addressCinemas;
    Geocoder coder;
    private String home;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapsViewModel =
                ViewModelProviders.of(this).get(MapsViewModel.class);
        root = inflater.inflate(R.layout.fragment_maps, container, false);
        networkConnection = new NetworkConnection();


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        Context context = getActivity().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        home = sp.getString("address","default");
        mView = (MapView) root.findViewById(R.id.mapview);

        coder = new Geocoder(context);

        List<Address> address;
        p1 = null;

        try {
            address = coder.getFromLocationName(home, 1);
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }


        if (mView != null) {
            mView.onCreate(null);
            mView.onResume();
            mView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (p1 != null){
            gMap.addMarker(new MarkerOptions().position(p1).title("Home"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p1, 12.0f));
        }

        GetCinemaTask getCinemaTask = new GetCinemaTask();
        getCinemaTask.execute();
    }



    private class GetCinemaTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            return networkConnection.getAllCinemas() ;
        }
        @Override
        protected void onPostExecute(String result) {


            JSONArray jsonArray = null;
            try {

                jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++ ){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String address = jsonObject.getString("cinemaName");
                    LatLng p = null;
                    System.out.println("address ---------" + address);
                    try {
                        addressCinemas = coder.getFromLocationName(address, 1);
                        Address location = addressCinemas.get(0);
                        location.getLatitude();
                        location.getLongitude();
                        p = new LatLng(location.getLatitude(), location.getLongitude());
                        System.out.println("--------"+p);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    gMap.addMarker(new MarkerOptions().position(p).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).alpha(0.8f));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}