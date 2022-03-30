package org.me.gcu.vivaldo_federico_s1828951;


import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
import java.util.Observable;
import java.util.Observer;

import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment implements Observer, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        AdapterView.OnItemSelectedListener {


    private GoogleMap mMap;
    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";


    private DataFeed dataFeed;

    private Spinner spinner;



    private ArrayList<RoadWorkItem> a;
    ListView listView;
    ArrayAdapter adapter;


    private ArrayList<Marker> mMarker = new ArrayList<Marker>();



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

//
        adapter = new ArrayAdapter<RoadWorkItem>(getContext(),
                R.layout.map_fragment, a);

        listView = view.findViewById(R.id.mobile_list);


        return view;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng glasgow = new LatLng(55.860916, -4.251433);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 12.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(glasgow).title("Marker in Sydney"));
        dataFeed = new DataFeed("roadWork", roadWorksURL);
        dataFeed.addObserver(this);

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinner)
            System.out.println("hey");
        {
            String text = (String) spinner.getSelectedItem();
            if (text.equals("Road Works")) {
                //getDataFeed(roadWorksURL);
                mMap.clear();
                dataFeed = new DataFeed("roadWork", roadWorksURL);

            } else if (text.equals("Planned Road Works")) {
                 mMap.clear();
                dataFeed = new DataFeed("plannedRoadWork", plannedRoadWorksURL);
                //  getDataFeed(plannedRoadWorksURL);
            } else if (text.equals("Incidents")) {
                 mMap.clear();
                dataFeed = new DataFeed("incident", incidentsURL);
            } else if (text.equals("Please select what to view on the map…")) {
                // mMap.clear();
                // Log.e("Spinner default", "clear map");
            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }
    @Override
    public void update(Observable o, Object arg) {


        a = dataFeed.getList();


        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {

//                adapter = new RoadWorkItemAdapter(getActivity(),
//                        R.layout.map_fragment, a);
//                listView.setAdapter(adapter);


                Log.e("list", a.toString());

                for (RoadWorkItem rwi : a) {
                    Marker tmp;
                    double lt = parseStringToDouble(rwi.getLat());
                    double ln = parseStringToDouble(rwi.getLon());

                    LatLng ltln = new LatLng(lt, ln);

                    CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
                    tmp = mMap.addMarker(new MarkerOptions().position(ltln).title(rwi.getTitle()));
                    tmp.setTag(rwi);

                    mMarker.add(tmp);
                    mMap.setInfoWindowAdapter(customInfoWindow);
                    mMap.setOnInfoWindowClickListener(MapFragment.this::onInfoWindowClick);

                }

            }
        });




    }



}
