package hacks.cupertino.cupertinohacks2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    public static MapsActivity mainActivity;

    private GoogleMap mMap;
    private LocationManager locationManager;

    private double latitude = 0;
    private double longitude = 0;

    private int frameIndex = 0;

    private ServerConnector serverConnector;
    public static String emailAddress;

    private ArrayAdapter<String> arrayAdapter;
    private int tabIndex;

    private Button traffic;
    private Button crime;
    private Button disaster;
    private Button events;
    private EditText emailAddressCom;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mainActivity = this;
        Log.d("HEY", "ONCREATE");
        jumpToFrameIndex(0);

        arrayAdapter =  new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Traffic");
        arrayAdapter.add("Crime");
        arrayAdapter.add("Natural Disaster");
        arrayAdapter.add("Other");

        serverConnector = new ServerConnector();
    }

    public void jumpToFrameIndex(int index){
        if(index == 0){
            setContentView(R.layout.email_layout);
            emailAddressCom = (EditText)(findViewById(R.id.editText));
        }
        else if(index == 1 ){
            dialog = ProgressDialog.show(this, "", "Loading map...", true);

            setContentView(R.layout.maps_layout);
            traffic = (Button) findViewById(R.id.traffic);
            crime = (Button) findViewById(R.id.crime);
            disaster = (Button) findViewById(R.id.disaster);
            events = (Button) findViewById(R.id.events);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        else if(index == 2 ){
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Select your problem : ");

            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    final String choice = arrayAdapter.getItem(which);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Provide a brief description.");
                    final EditText input = new EditText(MapsActivity.this);
                    input.setHint("You may leave this blank.");
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String des = input.getText().toString();


                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            alert.setTitle("Choose the severity:");
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

                            final SeekBar seek=new SeekBar(MapsActivity.this);
                            seek.setPadding(20, 10, 20, 0);
                            linear.addView(seek);
                            linear.addView(linear2);

                            alert.setView(linear);
                            alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    serverConnector.sendProblem(longitude, latitude, seek.getProgress(), choice, emailAddress, des);
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
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                }
            });
            builderSingle.show();
        }
    }


    public void onTraffic(View v){
        changeTabs(0);

        traffic.setBackgroundColor(Color.BLUE);
        crime.setBackgroundColor(Color.BLACK);
        disaster.setBackgroundColor(Color.BLACK);
        events.setBackgroundColor(Color.BLACK);
    }

    public void onCrime(View v){
        changeTabs(1);

        traffic.setBackgroundColor(Color.BLACK);
        crime.setBackgroundColor(Color.BLUE);
        disaster.setBackgroundColor(Color.BLACK);
        events.setBackgroundColor(Color.BLACK);
    }

    public void onDisasters(View v){
        changeTabs(2);
        traffic.setBackgroundColor(Color.BLACK);
        crime.setBackgroundColor(Color.BLACK);
        disaster.setBackgroundColor(Color.BLUE);
        events.setBackgroundColor(Color.BLACK);
    }

    public void onEvents(View v){
        changeTabs(3);
        traffic.setBackgroundColor(Color.BLACK);
        crime.setBackgroundColor(Color.BLACK);
        disaster.setBackgroundColor(Color.BLACK);
        events.setBackgroundColor(Color.BLUE);
    }

    public void onEmailSubmit(View v){
        emailAddress = emailAddressCom.getText().toString();
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
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 50, this);
        // Add a marker in Sydeney and move the camera
        changeTabs(0);
    }

    public void changeTabs(int index){
        clearMarkers();
        tabIndex = index;
        if(index == 0){

            for(int i=0;i<serverConnector.trafficEvents.size();i++){
                ServerConnector.Event e = serverConnector.trafficEvents.get(i);
                Log.d("HEY", e.latitude + " " + e.longitude);
                placeMarker(e.latitude, e.longitude, e.title);

            }
        }
        else if(index == 1){
            for(int i=0;i<serverConnector.crimeEvents.size();i++){
                ServerConnector.Event e = serverConnector.crimeEvents.get(i);
                Log.d("HEY", e.latitude + " " + e.longitude);
                placeMarker(e.latitude, e.longitude, e.title);

            }
        }
        else if(index == 2){
            for(int i=0;i<serverConnector.disasterEvents.size();i++){
                ServerConnector.Event e = serverConnector.disasterEvents.get(i);
                Log.d("HEY", e.latitude + " " + e.longitude);
                placeMarker(e.latitude, e.longitude, e.title);

            }
        }
        else if(index == 3){
            for(int i=0;i<serverConnector.eventEvents.size();i++){
                ServerConnector.Event e = serverConnector.eventEvents.get(i);
                Log.d("HEY", e.latitude + " " + e.longitude);
                placeMarker(e.latitude, e.longitude, e.title);

            }
        }
    }

    public void clearMarkers(){
        mMap.clear();
    }

    public void zoomToLocation(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void placeMarker(double latitude, double longitude, String title){
        LatLng marker = new LatLng(longitude, latitude);
        mMap.addMarker(new MarkerOptions().position(marker).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
    }



    protected void onStart() {
        super.onStart();



    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int i) {}

    public void onConnectionFailed(ConnectionResult connectionResult) {}

    boolean moveToLocation = false;

    @Override
    public void onLocationChanged(final Location location) {
        //your code here
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(!moveToLocation){
            dialog.dismiss();
            zoomToLocation();
            moveToLocation = true;
        }

    }

    public void onProviderDisabled(String provider){}
    public void onProviderEnabled(String provider){}
    public void onStatusChanged(String provider, int status, Bundle extras){}

}
