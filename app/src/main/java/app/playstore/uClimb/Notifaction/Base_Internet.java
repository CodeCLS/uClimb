package app.playstore.uClimb.Notifaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import app.playstore.uClimb.Fragments.Home.Home_Fragment;
import app.playstore.uClimb.Fragments.no_connection;
import app.playstore.uClimb.Login.Login_uClimb;
import app.playstore.uClimb.R;

public class Base_Internet extends AppCompatActivity {
    private static final String TAG = "Base_internet";
    private boolean screen_on = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isConnected = false;
        registerReceiver(mNetworkDetectReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    private boolean isConnected;

    private static final int WIFI_ENABLE_REQUEST = 0x1006;
    private static int status_2 = 0;

    private BroadcastReceiver mNetworkDetectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkInternetConnection();
        }
    };


    @Override
    protected void onDestroy() {
        status_2 = 0;


        unregisterReceiver(mNetworkDetectReceiver);

        super.onDestroy();
    }
    private void checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();


        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            if (screen_on == true){

                screen_on = false;
                setscreenback();


            }
            else{
                isConnected =true;
                return;

            }



        } else {
            if (status_2 == 0){
                screen_on = true;
                isConnected= false;
                setDefaultFragment();
                status_2 = 1;

            }
            else{
                return;
            }



        }
    }

    private void setscreenback() {
        Intent intent = new Intent(this, Login_uClimb.class);
        startActivity(intent);
        status_2 = 0;

        ///Home_Fragment mFragment = new Home_Fragment();
        ///FragmentManager fragmentManager = getSupportFragmentManager();
        ///fragmentManager.beginTransaction()
        ///        .replace(R.id.container_fragment, mFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == WIFI_ENABLE_REQUEST) {

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

}
    private void setDefaultFragment() {
        int status = 0;
        if (status != 1){
            Log.d(TAG,"Internet435" + " " + status);
            Intent intent = new Intent(this, Login_uClimb.class);
            startActivity(intent);
            Toast.makeText(this, "Lost internet connection", Toast.LENGTH_SHORT).show();
            status =1;
            Log.d(TAG,"Internet4355" + " " + status);



        }
        else{
            return;
        }


       //no_connection mFragment = new no_connection();
       //FragmentManager fragmentManager = getSupportFragmentManager();
       //fragmentManager.beginTransaction()
       //        .replace(R.id.container_fragment, mFragment).commit();
    }


}










