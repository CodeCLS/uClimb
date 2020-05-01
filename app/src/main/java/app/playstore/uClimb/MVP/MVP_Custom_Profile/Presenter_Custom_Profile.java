package app.playstore.uClimb.MVP.MVP_Custom_Profile;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import app.playstore.uClimb.Adapters.Profile.Adapter_Profile_Custom_User_Page;
import app.playstore.uClimb.Adapters.Profile.Adapter_Profile_User_Uploads;
import app.playstore.uClimb.R;
import app.playstore.uClimb.MVP.MVP_Login.Presenter_Login;

public class Presenter_Custom_Profile {
    private static final String TAG = "custom_profile_prese";

    private String uid;
    private String stat_uid;
    private String Age;
    private String Name;
    private String profile_img;
    private String Info;
    private String Subscription;
    private String grade;
    private String country;
    private ArrayList<String> follower = new ArrayList<String>();
    private ArrayList<String> following = new ArrayList<String>();
    private String Height;
    private String account_type;
    private String time_created;
    private ArrayList<String> posts = new ArrayList<>();


    private int columns = 2;
    Model_Custom_Profile custom_profile_model;
    private ArrayList<String> Source = new ArrayList<String>();
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<String> info = new ArrayList<String>();
    private ArrayList<String> posts_inner = new ArrayList<String>();
    private ArrayList<String> uid_inner = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> place = new ArrayList<String>();





    public Presenter_Custom_Profile(String uid) {
        this.custom_profile_model = new Model_Custom_Profile();
        this.uid = uid;
        Log.d(TAG,"username" + uid);


    }

    public Presenter_Custom_Profile() {
    }



    public void init(Context mContext, View view){
        fetchData(mContext,view);




    }
    public void setRec_inner(RecyclerView recyclerView,Context mContext,String uid){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> post_id = new ArrayList<String>();
                for (DataSnapshot postSnapshot: dataSnapshot.child("User").child(uid).child("Posts").getChildren()){
                    post_id.add(postSnapshot.getKey());


                }
                for (int i = 0; i < post_id.size();i++){
                    String id = post_id.get(i).toString();
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("Posts").child(id);

                    Log.d(TAG,"datasnapshot"+dataSnapshot1);
                    Source.add(dataSnapshot1.child("Source").getValue().toString());
                    time.add(dataSnapshot1.child("Time").getValue().toString());
                    type.add(dataSnapshot1.child("type").getValue().toString());
                    posts_inner.add(id);
                    info.add(dataSnapshot1.child("Info").getValue().toString());
                    uid_inner.add(dataSnapshot1.child("User_ID").getValue().toString());
                    place.add(dataSnapshot1.child("Place_name").getValue().toString());
                    Log.d(TAG,"Ource" + Source);

                }
                int numb_columns = 2;
                Log.d(TAG,"Ource" + post_id);
                Adapter_Profile_User_Uploads inner_profile_adapter = new Adapter_Profile_User_Uploads(Source,type,posts_inner,info,uid_inner,time,place);

                recyclerView.setLayoutManager(new GridLayoutManager(mContext,numb_columns));
                recyclerView.setAdapter(inner_profile_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchData(Context mContext,View view) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"uid:" +uid);
                stat_uid = Objects.requireNonNull(dataSnapshot.child(uid).child("StatisticsID").getValue()).toString();
                Age = Objects.requireNonNull(dataSnapshot.child(uid).child("Age").getValue()).toString();
                Name = Objects.requireNonNull(dataSnapshot.child(uid).child("Name").getValue()).toString();
                profile_img = Objects.requireNonNull(dataSnapshot.child(uid).child("IMG").getValue()).toString();
                Info = Objects.requireNonNull(dataSnapshot.child(uid).child("Info").getValue()).toString();
                Subscription = Objects.requireNonNull(dataSnapshot.child(uid).child("Subscription").getValue()).toString();
                grade = Objects.requireNonNull(dataSnapshot.child(uid).child("Grade").getValue()).toString();
                country = Objects.requireNonNull(dataSnapshot.child(uid).child("Country").getValue()).toString();
                for (DataSnapshot postSnapshot: dataSnapshot.child("Follower").getChildren()){
                    follower.add(Objects.requireNonNull(postSnapshot.getValue()).toString());

                }
                for (DataSnapshot postSnapshot: dataSnapshot.child(uid).child("Following").getChildren()){
                    following.add(Objects.requireNonNull(postSnapshot.getValue()).toString());

                }
                for (DataSnapshot postSnapshot: dataSnapshot.child(uid).child("Posts").getChildren()){
                    posts.add(postSnapshot.getValue().toString());
                    Log.d(TAG,"post3" + posts);

                }
                Height = Objects.requireNonNull(dataSnapshot.child(uid).child("Height").getValue()).toString();
                time_created = Objects.requireNonNull(dataSnapshot.child(uid).child("Time_created").getValue()).toString();
                account_type = Objects.requireNonNull(dataSnapshot.child(uid).child("account_type").getValue()).toString();
                Log.d(TAG,"post2" + posts);
                Adapter_Profile_Custom_User_Page profile_adapter = new Adapter_Profile_Custom_User_Page(uid,stat_uid,Age,Name,profile_img,Info,Subscription,grade,country,follower,following,Height,account_type,time_created,posts);
                RecyclerView rec = view.findViewById(R.id.custom_profile_rec);
                rec.setLayoutManager(new LinearLayoutManager(mContext));
                rec.setAdapter(profile_adapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void follow_new_user(Context mContext,String id_user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("");
        Presenter_Login login_presenter = new Presenter_Login();
        String id = login_presenter.getUID(mContext);
        Log.d(TAG,"uid"+id);
        Log.d(TAG,"id_user"+id_user);


        databaseReference.child("User").child(id).child("Following").child(id_user).setValue(id_user);
    }
}