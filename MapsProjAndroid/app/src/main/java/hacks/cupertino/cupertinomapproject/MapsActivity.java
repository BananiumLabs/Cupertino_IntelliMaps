package hacks.cupertino.cupertinomapproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;

    private int frameIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HEY", "ONCREATE");
        Toast.makeText(this, "Hi There", Toast.LENGTH_LONG).show();
        jumpToFrameIndex(0);
    }

    public void jumpToFrameIndex(int index){
        if(index == 0){
            setContentView(R.layout.email_layout);
        }
        else if(index == 1 ){
            setContentView(R.layout.activity_maps);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else if(index == 2 ){

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Select your problem : ");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Traffic");
            arrayAdapter.add("Burglary");
            arrayAdapter.add("Road Accident");
            arrayAdapter.add("Other");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);

                    alert.setTitle("Priority?");
                    LinearLayout.LayoutParams layoutForHigh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout linear=new LinearLayout(MapsActivity.this);
                    linear.setOrientation(1);

                    LinearLayout linear2=new LinearLayout(MapsActivity.this);
                    linear2.setOrientation(0);

                    TextView low=new TextView(MapsActivity.this);
                    low.setText("LOW");
                    low.setPadding(10, 10, 10, 10);
                    linear2.addView(low);

                    TextView high =new TextView(MapsActivity.this);
                    high.setText("HIGH");
                    high.setGravity(Gravity.RIGHT);
                    high.setPadding(10, 10, 10, 10);
                    linear2.addView(high, layoutForHigh);


                    SeekBar seek=new SeekBar(MapsActivity.this);
                    seek.setPadding(20, 10, 20, 0);
                    linear.addView(seek);
                    linear.addView(linear2);

                    alert.setView(linear);
                    alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Toast.makeText(getApplicationContext(), "Your report has been sent. The city of Cupertino thanks you.",Toast.LENGTH_LONG).show();
                        }
                    });

                    alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                        }
                    });

                    alert.show();
                }
            });
            builderSingle.show();
        }
    }


    public void onEmailSubmit(View v){

        jumpToFrameIndex(1);

    }

    public void onReport(View v){
        jumpToFrameIndex(2);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

    }

    public void placeMarker(){
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected void onStart() {
        super.onStart();
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
            googleApiClient.connect();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 50, this);
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int i) {}

    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.d("HEY", latitude + " " + longitude);
    }

    public void onProviderDisabled(String provider){}
    public void onProviderEnabled(String provider){}
    public void onStatusChanged(String provider, int status, Bundle extras){}

}
