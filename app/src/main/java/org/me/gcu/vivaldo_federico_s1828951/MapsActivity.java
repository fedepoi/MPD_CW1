package org.me.gcu.vivaldo_federico_s1828951;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

// Federico Vivaldo S1828951
public class MapsActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ArrayList<RoadWorkItem> plannedRoadWorksArray = new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> roadWorksArray = new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> incidentsArray = new ArrayList<RoadWorkItem>();


    private ListController l;

    private RelativeLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("main", "main");
        super.onCreate(savedInstanceState);


        if (!isNetworkAvailable()) {
            setContentView(R.layout.general_error);
        } else {

            setContentView(R.layout.activity_maps);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);

            NavigationView navView = findViewById(R.id.nav_view);
            navView.setNavigationItemSelectedListener(MapsActivity.this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MapsActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            l = new ListController(MapsActivity.this);
            l.addObserver(this);


            this.loadingLayout = findViewById(R.id.loading_container);

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo;
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.home:
                Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment(this.l);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.date:
                Fragment fragment2 = new CheckByDateFragment(l);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment2).commit();
                break;
            case R.id.road:
                Fragment fragment3 = new CheckByRoadFragment(l);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment3).commit();
                break;
            case R.id.plan:
                Fragment fragment4 = new PlanJourneyFragment(l);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment4).commit();
                break;
        }
        return true;
    }


    @Override
    public void update(Observable o, Object arg) {
        Log.e("road list -->", String.valueOf(l.getRoadWorkItemsList().size()));
        Log.e("plan list -->", String.valueOf(l.getPlannedList().size()));
        Log.e("incidents list -->", String.valueOf(l.getIncidentsList().size()));

        roadWorksArray = l.getRoadWorkItemsList();
        plannedRoadWorksArray = l.getPlannedList();
        incidentsArray = l.getIncidentsList();

        if (l.getFinished()) {
            Handler uiThread = new Handler(Looper.getMainLooper());
            uiThread.post(new Runnable() {
                @Override
                public void run() {
                    loadingLayout.setVisibility(View.GONE);
                    Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment(l);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            });
        }


    }


}