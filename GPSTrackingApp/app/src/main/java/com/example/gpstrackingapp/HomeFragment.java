package com.example.gpstrackingapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {
    
    private TextView latitudeTV, longitudeTV, addressTV, distanceTV;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private ArrayList<Address> addressList;
    private double latitude, longitude, distance;
    private static final int FINE_LOCATION_PERMISSION_CODE = 100;
    private static final int COARSE_LOCATION_PERMISSION_CODE = 101;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        latitudeTV = rootView.findViewById(R.id.latitudeTV);
        longitudeTV = rootView.findViewById(R.id.longtitudeTV);
        distanceTV = rootView.findViewById(R.id.distanceTV);
        addressTV = rootView.findViewById(R.id.addressTV);

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },101);
        }

        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            addressList = new ArrayList<>();
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    latitudeTV.setText("Latitude: " + String.valueOf(latitude));
                    longitudeTV.setText("Longitude: " + String.valueOf(longitude));
                    try {
                        addressList.add(geocoder.getFromLocation(latitude, longitude, 1).get(0));
                        addressTV.setText(addressList.get(addressList.size() - 1).getAddressLine(0)+"");
                        if (addressList.size() > 1){
                            Location last = new Location("last location");
                            last.setLatitude(addressList.get(addressList.size()-2).getLatitude());
                            last.setLongitude(addressList.get(addressList.size()-2).getLongitude());
                            distance += location.distanceTo(last);
                            distanceTV.setText("Distance traveled: " + distance + " meters");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        } catch (Exception e){
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Location Permission Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}