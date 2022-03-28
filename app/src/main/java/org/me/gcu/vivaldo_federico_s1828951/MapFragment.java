package org.me.gcu.vivaldo_federico_s1828951;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import org.me.gcu.vivaldo_federico_s1828951.databinding.ActivityMapsBinding;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        //DataInterface,
        AdapterView.OnItemSelectedListener {


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private String rdataToDisplay = "";

    private DataFeed dataFeed;

    private Spinner spinner;

    private ArrayList<RoadWorkItem> plannedRoadWorksArray= new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> roadWorksArray= new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> incidentsArray= new ArrayList<RoadWorkItem>();

    private ArrayList<RoadWorkItem> arrayList;

    private ArrayList<Marker> mMarker= new ArrayList<Marker>();

    public MapFragment(ArrayList<RoadWorkItem> array){
        this.arrayList= array;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("map fragment on create", "alive");
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner) view.findViewById(R.id.map_spinner);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(getContext(), R.array.map_spinner, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        spinner.setOnItemSelectedListener(this);
        return view;
    }


//    private void parseRoadData(String str, String rwiType) {
//        try {
//            RoadWorkItem roadWork = new RoadWorkItem(rwiType);
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser xpp = factory.newPullParser();
//            xpp.setInput(new StringReader(str));
//            int eventType = xpp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_DOCUMENT) {
//                    System.out.println("Start document");
//                    Log.e("start xml doc", "Start document");
//                } else if (eventType == XmlPullParser.START_TAG) {
//                    if (xpp.getName().equalsIgnoreCase("title")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setTitle(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("description")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setDesc(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("link")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setLink(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("point")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setPoint(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("author")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setAuthor(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("comments")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setComments(temp);
//                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
//
//                        String temp = xpp.nextText();
//                        roadWork.setPubDate(temp);
//                    }
//
//
//                }
//                double lt = parseStringToDouble(roadWork.getLat());
//                double ln = parseStringToDouble(roadWork.getLon());
//
//                LatLng ltln = new LatLng(lt, ln);
//
//                CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
//                Marker tmp;
//
//                tmp = this.mMap.addMarker(new MarkerOptions().position(ltln).title(roadWork.getTitle()));
//                tmp.setTag(roadWork);
//                this.mMap.setInfoWindowAdapter(customInfoWindow);
//                this.mMap.setOnInfoWindowClickListener(this);
//
//                eventType = xpp.next();
//
//            }
//        } catch (XmlPullParserException ae1) {
//            Log.e("pull parser exception", "Parsing error" + ae1.toString());
//        } catch (IOException ae1) {
//            Log.e("io exception", "IO error during parsing");
//        }
//
//        System.out.println("End document");
//
//    }

//    private static double parseStringToDouble(String value) {
//        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //change feed to road works
        //dataFeed = new DataFeed(plannedRoadWorksURL, this);

        // new Thread(new Task(urlSource)).start();
       // getDataFeed(roadWorksURL);
        LatLng glasgow = new LatLng(55.860916, -4.251433);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 12.0f));

        Marker tmp;
        for (RoadWorkItem rwi : this.arrayList) {

            double lt = parseStringToDouble(rwi.getLat());
            double ln = parseStringToDouble(rwi.getLon());

            LatLng ltln = new LatLng(lt, ln);

            CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
            tmp = this.mMap.addMarker(new MarkerOptions().position(ltln).title(rwi.getTitle()));
            tmp.setTag(rwi);

            mMarker.add(tmp);
            this.mMap.setInfoWindowAdapter(customInfoWindow);
            this.mMap.setOnInfoWindowClickListener(this);
        }
    }

//    private void displayMarkers(ArrayList<RoadWorkItem> a){
//        for (RoadWorkItem rwi : a) {
//            Log.e("hey",rwi.toString());
//                double lt = parseStringToDouble(rwi.getLat());
//                double ln = parseStringToDouble(rwi.getLon());
//
//                LatLng ltln = new LatLng(lt, ln);
//
//                CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
//                Marker tmp;
//
//                tmp = this.mMap.addMarker(new MarkerOptions().position(ltln).title(rwi.getTitle()));
//                tmp.setTag(rwi);
//                this.mMap.setInfoWindowAdapter(customInfoWindow);
//                this.mMap.setOnInfoWindowClickListener(this);
//        }
//    }
    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("ciao", marker.getTitle());

        marker.hideInfoWindow();
        // Custom dialog setup
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.view_more_dialog);
        dialog.setTitle("Custom Dialog Example");
// Set the custom dialog components as a TextView and Button component
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView startDate = (TextView) dialog.findViewById(R.id.dialog_startDate);
        TextView endDate = (TextView) dialog.findViewById(R.id.dialog_endDate);
        TextView link = (TextView) dialog.findViewById(R.id.dialog_link);
        TextView pubDate = (TextView) dialog.findViewById(R.id.dialog_pubDate);
        TextView desc = (TextView) dialog.findViewById(R.id.dialog_description);
        RoadWorkItem rwi = (RoadWorkItem) marker.getTag();
        title.setText(rwi.getTitle());
        startDate.setText(rwi.getStartDate());
        endDate.setText(rwi.getEndDate());
        link.append(rwi.getLink());
        pubDate.append(rwi.getPubDate());
        desc.append(rwi.getDesc());
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


//    @Override
//    public void receiveData(String str) {
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                String replace = str.replace("null", "");
//                Log.e("received data", replace);
//                rdataToDisplay = replace;
//                parsePlannedRoadWork(replace);
//            }
//        });
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinner)
            System.out.println("hey");
        {
            String text = (String) spinner.getSelectedItem();
            if (text.equals("Road Works")) {
                //getDataFeed(roadWorksURL);
                //mMap.clear();

            } else if (text.equals("Planned Road Works")) {
               // mMap.clear();
              //  getDataFeed(plannedRoadWorksURL);
            } else if (text.equals("Incidents")) {
               // mMap.clear();
               // getDataFeed(incidentsURL);
            } else if (text.equals("Please select what to view on the map…")) {
               // mMap.clear();
               // Log.e("Spinner default", "clear map");
            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------
//    public void getDataFeed(String url) {
//
//        HandlerThread ht = new HandlerThread("MyHandlerThread");
//        ht.start();
//        Handler asyncHandler = new Handler(ht.getLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                Object response = msg.obj;
//                //  doSomethingOnUi(response);
//                //  dataInt.receiveData((String)response);
//                if (url.equals(plannedRoadWorksURL)) {
//                  //  displayPlannedRoadWorkMarkers((String)response);
//                } else if (url.equals(roadWorksURL)) {
//                  //  displayRoadWorksMarkers((String)response);
//                } else if (url.equals(incidentsURL)) {
//                   // displayIncidentsMarkers((String)response);
//                }
//            }
//        };
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Log.e("DataFeed", "Starting async thread");
//                URL aurl;
//                URLConnection yc;
//                BufferedReader in = null;
//                String inputLine = "";
//                String result = "";
//                try {
//                    aurl = new URL(url);
//                    yc = aurl.openConnection();
//                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                    while ((inputLine = in.readLine()) != null) {
//                        result = result + inputLine;
//                    }
//                    in.close();
//                } catch (IOException ae) {
//                    Log.e("run error", "ioexception in run");
//                }
//                Message message = new Message();
//                message.obj = result;
//
//                asyncHandler.sendMessage(message);
//
//            }
//        };
//        asyncHandler.post(runnable);
//    }

//    private void displayPlannedRoadWorkMarkers(Object response) {
//        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.post(new Runnable() {
//            @Override
//            public void run() {
//                String str = (String)response;
//                String replace = str.replace("null", "");
//                Log.e("received data", replace);
//                rdataToDisplay = replace;
//                parseRoadData(replace,"plannedRoadWork");
//            }
//        });
//    }
//    private void displayRoadWorksMarkers(Object response) {
//        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.post(new Runnable() {
//            @Override
//            public void run() {
//                String str = (String)response;
//                String replace = str.replace("null", "");
//                Log.e("received data", replace);
//                rdataToDisplay = replace;
//                parseRoadData(replace,"roadWork");
//            }
//        });
//    }
//    private void displayIncidentsMarkers(Object response) {
//        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.post(new Runnable() {
//            @Override
//            public void run() {
//                String str = (String)response;
//                String replace = str.replace("null", "");
//                Log.e("received data", replace);
//                rdataToDisplay = replace;
//                parseRoadData(replace,"incidents");
//            }
//        });
//    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------

}
