package app.playstore.uClimb.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import app.playstore.uClimb.R;
import app.playstore.uClimb.MVP.MVP_Friends.Presenter_Friends;
import app.playstore.uClimb.MVP.MVP_Location.Presenter_Location;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Location_fragment extends Fragment implements OnMapReadyCallback  {
    private static final String TAG = "Location";
    private JSONObject callbackjson;
    private PlacesClient placesClient;
    private SupportMapFragment mapFragment = null;

    public static GoogleMap mMap;

    public static void doTask(JSONObject jsonObject) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null){
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.main_location_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button publish_position = view.findViewById(R.id.btn_publish_position);
        Drawable drawable = publish_position.getBackground();
        drawable.setTint(getResources().getColor(R.color.blue_pressed_btn));

        ActivityCompat.requestPermissions((Activity) Objects.requireNonNull(this.getContext()),new String[]{ACCESS_FINE_LOCATION}, 1);
        Places.initialize(getContext(), "AIzaSyCBG4HfWkuFX4MYM-7589gNw6hFbQiS8VM");
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // Create a new Places client instance
        this.placesClient = Places.createClient(getContext());


Presenter_Location location_presenter = new Presenter_Location(getContext(),view,mapFragment);
location_presenter.getData();
Presenter_Friends friends_presenter = new Presenter_Friends(view,getContext());
friends_presenter.initViews();
location_presenter.getLocation();
BetaMessage();


                        //ok - proceed



           //     }




      //  });




    }
    public void BetaMessage(){
        SharedPreferences sharedPreferences = ((AppCompatActivity)getContext()).getPreferences(Context.MODE_PRIVATE);
        Boolean first_time = sharedPreferences.getBoolean("First_Location",true);
        Log.d(TAG,"First_Location" + first_time);

        if (first_time){
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.Location)
                    .setMessage(getResources().getString(R.string.standorte_message) )

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            dialog.dismiss();

                        }
                    })

                    .show();

            SharedPreferences.Editor shared_ed = sharedPreferences.edit();
            shared_ed.putBoolean("First_Location",false);
            shared_ed.apply();

        }
        else{
            return;
        }



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION}, 1);
        Places.initialize(getContext(), "AIzaSyCBG4HfWkuFX4MYM-7589gNw6hFbQiS8VM");

// Create a new Places client instance
        placesClient = Places.createClient(getContext());

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getLocation(){
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

// Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i("Location", String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Location", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }
        }
    }



    public StringBuilder sbMethod() {

        //use your current location here
        double mLatitude = 37.77657;
        double mLongitude = -122.417506;

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + "restaurant");
        sb.append("&sensor=true");
        sb.append("&key=******* YOUR API KEY****************");

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }







}
