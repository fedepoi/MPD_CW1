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


public class MapsActivity extends AppCompatActivity implements Observer, NavigationView.OnNavigationItemSelectedListener
{
private DrawerLayout drawer;

    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private ArrayList<RoadWorkItem> plannedRoadWorksArray= new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> roadWorksArray= new ArrayList<RoadWorkItem>();
    private ArrayList<RoadWorkItem> incidentsArray= new ArrayList<RoadWorkItem>();

    private DataFeed df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("main","main");
        super.onCreate(savedInstanceState);

//        df= new DataFeed("plannedRoadWork",plannedRoadWorksURL);
//        df.addObserver(this);
//        getDataFeed(plannedRoadWorksURL);
//        getDataFeed(roadWorksURL);
//        getDataFeed(incidentsURL);


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



//    private void doSomethingOnUi()
//    {
//        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.post(new Runnable() {
//            @Override
//            public void run() {
//                        setContentView(R.layout.activity_maps);
//        Toolbar toolbar  = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer= findViewById(R.id.drawer_layout);
//
//        NavigationView navView = findViewById(R.id.nav_view);
//        navView.setNavigationItemSelectedListener(MapsActivity.this);
//
//        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(MapsActivity.this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//       Fragment fragment = new org.me.gcu.vivaldo_federico_s1828951.MapFragment(incidentsArray);
//       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
//            }
//        });
//    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(df.getList().toString());
    }


    //---------------------------------------------------------------------------------------------------------
//    public void getDataFeed(String url) {
//
//        HandlerThread ht = new HandlerThread("MyHandlerThread");
//        ht.start();
//        Handler asyncHandler = new Handler(ht.getLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                Object response = msg.obj;
//                String str = (String) response;
//                String replace = str.replace("null", "");
//
//                if (url.equals(plannedRoadWorksURL)) {
//                    parseRoadData(replace,"plannedRoadWork");
//                } else if (url.equals(roadWorksURL)) {
//                    parseRoadData(replace,"roadWork");
//                } else if (url.equals(incidentsURL)) {
//                    parseRoadData(replace,"incident");
//                }
//
//                doSomethingOnUi();
////                Log.e("received data", replace);
////                rdataToDisplay = replace;
////                parseRoadData(replace, "plannedRoadWork");
//
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
////
////                LatLng ltln = new LatLng(lt, ln);
////
////                CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
////                Marker tmp;
////
////                tmp = this.mMap.addMarker(new MarkerOptions().position(ltln).title(roadWork.getTitle()));
////                tmp.setTag(roadWork);
////                this.mMap.setInfoWindowAdapter(customInfoWindow);
////                this.mMap.setOnInfoWindowClickListener(this);
//                switch (rwiType) {
//                    case "plannedRoadWork":
//                        plannedRoadWorksArray.add(roadWork);
//                        break;
//                    case "roadWork":
//                      roadWorksArray.add(roadWork);
//                        break;
//                    case "incident":
//                        incidentsArray.add(roadWork);
//                        break;
//                }
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
   // }
    //---------------------------------------------------------------------------------------------------------

}