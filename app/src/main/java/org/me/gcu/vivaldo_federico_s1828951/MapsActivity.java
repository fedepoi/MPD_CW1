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


public class MapsActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener
{
private DrawerLayout drawer;

//    private GoogleMap mMap;
//    private ActivityMapsBinding binding;
//    private String urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
//    private String result = "";
//
//    private DataFeed dataFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("main","main");
        super.onCreate(savedInstanceState);

      //   dataFeed= new DataFeed(urlSource, MapsActivity.this);

        setContentView(R.layout.activity_maps);



//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//--------------------------------------------------------------------//
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

//
//    private void parseData(String str)
//    {
//        try
//        {
//            RoadWorkItem roadWork = new RoadWorkItem();
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            XmlPullParser xpp = factory.newPullParser();
//            xpp.setInput( new StringReader( str ) );
//            int eventType = xpp.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT)
//            {
//                if(eventType == XmlPullParser.START_DOCUMENT)
//                {
//                    System.out.println("Start document");
//                    Log.e("start xml doc","Start document");
//                }
//                else
//                if(eventType == XmlPullParser.START_TAG)
//                {
//                    if (xpp.getName().equalsIgnoreCase("title"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setTitle(temp);
//                    }
//                    else  if (xpp.getName().equalsIgnoreCase("description"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setDesc(temp);
//                    }
//                    else  if (xpp.getName().equalsIgnoreCase("link"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setLink(temp);
//                    }
//                    else  if (xpp.getName().equalsIgnoreCase("point"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setPoint(temp);
//                    }else  if (xpp.getName().equalsIgnoreCase("author"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setAuthor(temp);
//                    }else  if (xpp.getName().equalsIgnoreCase("comments"))
//                    {
//
//                        String temp = xpp.nextText();
//                        roadWork.setComments(temp);
//                    }else  if (xpp.getName().equalsIgnoreCase("pubDate"))
//                    {
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
//                CustomInfoWindow customInfoWindow=new CustomInfoWindow(MapsActivity.this);
//                Marker tmp ;
//
//              tmp= this.mMap.addMarker(new MarkerOptions().position(ltln).title(roadWork.getTitle()));
//               tmp.setTag(roadWork);
//               this.mMap.setInfoWindowAdapter(customInfoWindow);
//               this.mMap.setOnInfoWindowClickListener(this);
//
//                eventType = xpp.next();
//
//            } // End of while
//        }
//        catch (XmlPullParserException ae1)
//        {
//            Log.e("pull parser exception","Parsing error" + ae1.toString());
//        }
//        catch (IOException ae1)
//        {
//            Log.e("io exception","IO error during parsing");
//        }
//
//        System.out.println("End document");
//
//    }
//
//    private static double parseStringToDouble(String value) {
//        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        dataFeed=new DataFeed(urlSource,MapsActivity.this);
//
//        LatLng glasgow = new LatLng(55.860916, 	-4.251433);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 12.0f));
//
//
//    }
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        Log.e("ciao",marker.getTitle());
//    }
//
//
//    @Override
//    public void receiveData(String str) {
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                String replace = str.replace("null", "");
//                Log.e("received data", replace);
//               // result=replace;
//                parseData(replace);
//            }
//        });
//    }

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