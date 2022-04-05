package org.me.gcu.vivaldo_federico_s1828951;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

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


public class MapsActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private ArrayList<RoadWorkItem> plannedRoadWorksArray = new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> roadWorksArray = new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> incidentsArray = new ArrayList<RoadWorkItem>();

    private DataFeed df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("main", "main");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(MapsActivity.this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MapsActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.home:
                Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.date:

                Fragment fragment2 = new CheckByDateFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).commit();
                break;
            case R.id.road:
                Fragment fragment3 = new CheckByRoadFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment3).commit();
                break;
            case R.id.plan:
                Fragment fragment4 = new PlanJourneyFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment4).commit();
                break;
        }
        return true;
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println(df.getList().toString());
    }


}