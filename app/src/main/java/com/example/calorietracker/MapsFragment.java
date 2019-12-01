package com.example.calorietracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.List;

public class MapsFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    MapView vMaps;
    GoogleApiClient client;
    LocationRequest locationRequest;
    String username = "";
    String address = "";
    private GoogleMap googleMap;
    private static final String google_place_key = "AIzaSyDdIC2V-gln9f5dr3V791hJZuxz1SX5kb0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        username = bundle.getString("username");
        CredentialAsyncTask credentialAsyncTask = new CredentialAsyncTask();
        credentialAsyncTask.execute(username);

        vMaps = (MapView) rootView.findViewById(R.id.mapView);
        vMaps.onCreate(savedInstanceState);

        vMaps.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        vMaps.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                client = new GoogleApiClient.Builder(getActivity().getApplicationContext()).addApi(LocationServices.API)
                        .addConnectionCallbacks(MapsFragment.this)
                        .addOnConnectionFailedListener(MapsFragment.this)
                        .build();

                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
//                else {
//                    ActivityCompat.requestPermissions(this, new String[] {
//                                    Manifest.permission.ACCESS_FINE_LOCATION,
//                                    Manifest.permission.ACCESS_COARSE_LOCATION },
//                            TAG_CODE_PERMISSION_LOCATION);
//                }

                LatLng myLocation = getLocationFromAddress(getActivity().getApplicationContext(), address);
                if (myLocation != null) {
                    googleMap.addMarker(new MarkerOptions().position(myLocation).title("Home").snippet("My Home"));
                }

                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + myLocation.latitude + "," + myLocation.longitude
                        + "&radius=" + 5000 + "&keyword=park" + "&key=" + google_place_key;

                GetNearByParks getNearByParks = new GetNearByParks();
                getNearByParks.execute(url);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        vMaps.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        vMaps.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vMaps.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        vMaps.onLowMemory();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng p1 = null;

        try {
            addressList = geocoder.getFromLocationName(strAddress, 5);
            if (addressList == null) {
                return null;
            }

            Address location = addressList.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Location not found", Toast.LENGTH_SHORT);
        } else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(update);

            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("park");
            googleMap.addMarker(options);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest().create();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, MapsFragment.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class CredentialAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return RestClient.findWithStringParams("findByUsername", "credential", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                address = jsonArray.getJSONObject(0).getJSONObject("userid").getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GetNearByParks extends AsyncTask<String, Void, String>{

        String url;
        InputStream inputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String data = "";

        @Override
        protected String doInBackground(String... strings) {
            url = (String) strings[0];
            try {
                URL myurl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)myurl.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i< jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject locationobj = object.getJSONObject("geometry").getJSONObject("location");
                    String latitude = locationobj.getString("lat");
                    String longtitude = locationobj.getString("lng");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    String name = jsonObject1.getString("name");
                    String snippet = jsonObject1.getString("vicinity");

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(name);
                    markerOptions.snippet(snippet);
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                    googleMap.addMarker(markerOptions);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
