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

import android.util.Log;

import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class MapsActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener
{
private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("main","main");
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_maps);


        Toolbar toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(MapsActivity.this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(MapsActivity.this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


       Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment();
       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
                switch (item.getItemId()) {
            case R.id.home:
                Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                break;
            case R.id.date:

                Fragment fragment2 = new CheckByDateFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2).commit();
                break;
            case R.id.road:
                // do your code
                break;
            case R.id.plan:
                // do your code
                break;
//            default:
//                return super.onOptionsItemSelected(item);
        }
       return true;
    }
}