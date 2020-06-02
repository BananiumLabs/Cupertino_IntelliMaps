package hacks.cupertino.cupertinohacks2;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by math on 4/9/2017.
 */
public class ServerConnector {

    public ArrayList<Event> trafficEvents = new ArrayList<Event>();
    public ArrayList<Event> crimeEvents = new ArrayList<Event>();
    public ArrayList<Event> eventEvents = new ArrayList<Event>();
    public ArrayList<Event> disasterEvents = new ArrayList<Event>();

    public ServerConnector(){
        retreiveLocations();
    }

    public void sendProblem(final double longitude, final double latitude, final int priority, final String type, final String email, final String des){
        new Thread(new Runnable(){
            public void run(){
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(MapsActivity.mainActivity, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    //String state = addresses.get(0).getAdminArea();
                    //String country = addresses.get(0).getCountryName();
                    //String postalCode = addresses.get(0).getPostalCode();
                    //String knownName = addresses.get(0).getFeatureName();

                    address = address.replaceAll(" ", "_");
                    URLConnection urlConnection = new URL("https://2tbs.club/customapi.php?request=0&long=" + longitude + "&lat=" + latitude +
                            "&severity=" + (int)(priority/20) + "&type=\"" + type + "\"&email=\"" + email + "\"&street=\"" + address + ",\"&des=\"" + des.replaceAll(" ", "_") + "\"").openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while(true){
                        String line = br.readLine();
                        if(line == null) break;
                        Log.d("HEY", line);
                    }

                    br.close();

                }
                catch(Exception e){}
            }

        }).start();
    }

    public Location[] retreiveLocations(){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URLConnection urlConnection = new URL("https://2tbs.club/customapi.php?request=1").openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = br.readLine().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.d("HEY", line);
                    if(line.equals("")) return;
                    String[] rawEntries = line.split("\\},");
                    for(int i=0;i<rawEntries.length;i++){
                        String rawEntry = rawEntries[i].replaceAll("\\{", "").replaceAll("\"", "");
                        if(rawEntry.equals("")) continue;
                        String[] rawDataEntries = rawEntry.split(",");

                        Log.d("HEY", rawEntry);
                        Event e = new Event();
                        for(int j=0;j<rawDataEntries.length;j++){
                            String data = rawDataEntries[j];
                            if(data.equals("")) continue;
                            String value = data.substring(data.indexOf(":") + 1);
                            if(data.contains("latitude")){
                                e.latitude = Double.parseDouble(value);
                            }
                            else if(data.contains("longitude")){
                                e.longitude = Double.parseDouble(value);
                            }
                            else if(data.contains("severity")){
                                e.severity = Integer.parseInt(value);
                            }
                            else if(data.contains("type")){
                                e.title = value;
                            }
                            else if(data.contains("type")){
                                e.title = value;
                            }
                        }
                        if(e.title.equals("Traffic")){
                            trafficEvents.add(e);
                        }
                        else if(e.title.equals("Crime")){
                            crimeEvents.add(e);
                        }
                        else if(e.title.equals("Event")){
                            eventEvents.add(e);
                        }
                        else if(e.title.equals("Natural Disaster")){
                            disasterEvents.add(e);
                        }
                        Log.d("HEY", " - - " + e.title);
                    }

                    br.close();
                }
                catch(Exception e){
                }
            }
        }).start();
        return null;
    }

    public class Event{
        String title;
        String address;
        int severity;
        double latitude;
        double longitude;
    }

}
