package app.playstore.uClimb.MVP.MVP_Location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import app.playstore.uClimb.Adapters.Friends.Adapter_Location_Friends_List;
import app.playstore.uClimb.Obsolete.LocationService;
import app.playstore.uClimb.R;
import app.playstore.uClimb.MVP.MVP_Login.Presenter_Login;

public class Presenter_Location extends Fragment implements OnMapReadyCallback , LocationListener {
    private static final String TAG = "location_presenter";
    private ArrayList<String> friend_id = new ArrayList<>();
    private ArrayList<String> friend_location_status = new ArrayList<>();
    private ArrayList<String> friend_location_latitude = new ArrayList<>();
    private ArrayList<String> friend_location_longitude = new ArrayList<>();
    private ArrayList<String> friend_location_time = new ArrayList<>();
    private ArrayList<String> friend_name = new ArrayList<>();
    private ArrayList<String> friend_url_img = new ArrayList<>();
    private Context mContext;
    private View view;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    long hours_five = 18000000;
    private long date2_2;
    private long date1_1;
    private Date date1;
    private Date date2;



    public static GoogleMap mMap;
    private int hours;
    private Adapter_Location_Friends_List friend_location_list = new Adapter_Location_Friends_List(friend_location_status, friend_name, friend_url_img,friend_location_time);

    public Presenter_Location(Context mContext, View view, SupportMapFragment mapFragment) {
        this.mContext = mContext;
        this.view = view;
        this.mapFragment = mapFragment;

    }

    public void setRec() {

        RecyclerView recyclerView = view.findViewById(R.id.rec_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(friend_location_list);

    }

    //public void setMarker(int position) {
    //    String id = friend_id.get(position);
    //    String lat = friend_location_latitude.get(position);
    //    String longitude = friend_location_longitude.get(position);
    //    final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
    //            .findFragmentById(R.id.map);
    //    Objects.requireNonNull(mapFragment).getMapAsync(new OnMapReadyCallback() {
    //        @Override
    //        public void onMapReady(GoogleMap googleMap) {
    //            mMap = googleMap;
    //            LocationService.performOnBackgroundThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                }
    //            });
    //        }
    //    });
//
//
    //}

    public void getData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("");
        String uid;
        Presenter_Login login_presenter = new Presenter_Login();
        uid = login_presenter.getUID(mContext);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.child("User").child(uid).child("Friends").getChildren()) {
                    if (dataSnapshot.child("User").child(postSnapshot.getKey()).child("Name").exists() &&dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_last_Time_updated").exists()
                    && dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_lat").exists() && dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_lon").exists()
                    && dataSnapshot.child("User").child(postSnapshot.getKey()).child("IMG").exists()) {
                        friend_id.add(postSnapshot.getKey());
                        friend_name.add(Objects.requireNonNull(dataSnapshot.child("User").child(postSnapshot.getKey()).child("Name").getValue()).toString());
                        friend_location_time.add(Objects.requireNonNull(dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_last_Time_updated").getValue()).toString());
                        friend_location_latitude.add(Objects.requireNonNull(dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_lat").getValue()).toString());
                        friend_location_longitude.add(Objects.requireNonNull(dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("Position_lon").getValue()).toString());
                        friend_url_img.add(dataSnapshot.child("User").child(postSnapshot.getKey()).child("IMG").getValue().toString());
                        friend_location_status.add(Objects.requireNonNull(dataSnapshot.child("User").child(postSnapshot.getKey()).child("Position").child("position_status").getValue()).toString());
                    }

                }
                setRec();
                initMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG,"location_manager"+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d(TAG,"Location32"+location);
        if (location == null){
            return;
        }
        Log.d(TAG,"Locationgooglepls: " + location.getLatitude());
        Log.d(TAG,"Locationgooglepls: " + location.getLongitude());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        Button button = view.findViewById(R.id.btn_publish_position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"time: " + getTime());
                Presenter_Login login_presenter = new Presenter_Login();
                String uid = login_presenter.getUID(mContext);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("");
                databaseReference.child("User").child(uid).child("Position").child("Position_lat").setValue(location.getLatitude());
                databaseReference.child("User").child(uid).child("Position").child("Position_lon").setValue(location.getLongitude());
                databaseReference.child("User").child(uid).child("Position").child("Position_last_Time_updated").setValue(getTime());






            }
        });
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());


        return currentDateandTime;
    }

    private void initMarkers() {


            Objects.requireNonNull(mapFragment).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    Log.d(TAG,"FriendSIze: "+friend_id);
                    for (int i = 0;i <friend_id.size();i++) {
                        String id = friend_id.get(i);
                        String lat = friend_location_latitude.get(i);
                        String longitude = friend_location_longitude.get(i);
                        String name = friend_name.get(i);
                        String time = friend_location_time.get(i);
                        String status = friend_location_status.get(i);


                        // Add a marker in Sydney, Australia,
                        // and move the map's camera to the same location.
                        if (lat.isEmpty()){
                            return;
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

                        try {
                            date1 = sdf.parse(getTime());
                            date1_1 = date1.getTime() ;

                            date2 = sdf.parse(friend_location_time.get(i));
                            date2_2 = date2.getTime()+ hours_five;
                            Boolean date = date1.after(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        double latitude = Double.parseDouble(lat);
                        double longitude_final = Double.parseDouble(longitude);
                        Log.d(TAG,"latitudelongitude" + latitude + " " + longitude_final);
                        LatLng location = new LatLng(latitude, longitude_final);
                        LatLng europe = new LatLng(53.0000, 9.0000);




                        if (date2_2> date1_1){
                            googleMap.addMarker(new MarkerOptions().position(location)
                                    .title(name + " is here.")).setIcon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));


                        }
                        else {
                            googleMap.addMarker(new MarkerOptions().position(location)
                                    .title(name + " was here.")).setIcon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(europe));
                        }

                    }


                }
            });






    }




        @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"onmapready");


    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"locationchanged" + location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
