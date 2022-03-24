package org.me.gcu.vivaldo_federico_s1828951;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Person;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

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
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback  ,GoogleMap.OnInfoWindowClickListener, DataInterface
{


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String result = "";

    private DataFeed dataFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

      //   dataFeed= new DataFeed(urlSource, MapsActivity.this);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Log.e("home","home");
                return true;
            case R.id.date:
                // do your code
                return true;
            case R.id.road:
                // do your code
                return true;
            case R.id.plan:
                // do your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    public void startProgress()
//    {
//        // Run network access on a separate thread;
//        //new Thread(new Task(urlSource)).start();
//
//    }






    //    open connection and read data feed wip needs to be changed
//    private class Task implements Runnable
//    {
//        private String url;
//
//        public Task(String aurl)
//        {
//            url = aurl;
//        }
//        @Override
//        public void run()
//        {
//
//            URL aurl;
//            URLConnection yc;
//            BufferedReader in = null;
//            String inputLine = "";
//
//            try
//            {
//                aurl = new URL(url);
//                yc = aurl.openConnection();
//                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                while ((inputLine = in.readLine()) != null)
//                {
//                    result = result + inputLine;
//
//                    //System.out.println(inputLine);
//
//                }
//                in.close();
//            }
//            catch (IOException ae)
//            {
//                Log.e("run error", "ioexception in run");
//            }
//
//            MapsActivity.this.runOnUiThread(new Runnable()
//            {
//                public void run() {
//                    Log.d("UI thread", "I am the UI thread");
////                    Log.e("result",result);
//                    parseData();
//
//                }
//            });
//        }
//
//    }


    private void parseData(String str)
    {
        try
        {
            RoadWorkItem roadWork = new RoadWorkItem();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( str ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                {
                    System.out.println("Start document");
                    Log.e("start xml doc","Start document");
                }
                else
                if(eventType == XmlPullParser.START_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setTitle(temp);
                    }
                    else  if (xpp.getName().equalsIgnoreCase("description"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setDesc(temp);
                    }
                    else  if (xpp.getName().equalsIgnoreCase("link"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setLink(temp);
                    }
                    else  if (xpp.getName().equalsIgnoreCase("point"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setPoint(temp);
                    }else  if (xpp.getName().equalsIgnoreCase("author"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setAuthor(temp);
                    }else  if (xpp.getName().equalsIgnoreCase("comments"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setComments(temp);
                    }else  if (xpp.getName().equalsIgnoreCase("pubDate"))
                    {

                        String temp = xpp.nextText();
                        roadWork.setPubDate(temp);
                    }


                }
                double lt = parseStringToDouble(roadWork.getLat());
                double ln = parseStringToDouble(roadWork.getLon());

                LatLng ltln = new LatLng(lt, ln);

                CustomInfoWindow customInfoWindow=new CustomInfoWindow(MapsActivity.this);
                Marker tmp ;

              tmp= this.mMap.addMarker(new MarkerOptions().position(ltln).title(roadWork.getTitle()));
               tmp.setTag(roadWork);
               this.mMap.setInfoWindowAdapter(customInfoWindow);
               this.mMap.setOnInfoWindowClickListener(this);

                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("pull parser exception","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("io exception","IO error during parsing");
        }

        System.out.println("End document");

    }

    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dataFeed=new DataFeed(urlSource,MapsActivity.this);

        LatLng glasgow = new LatLng(55.860916, 	-4.251433);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 12.0f));


    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("ciao",marker.getTitle());
    }


    @Override
    public void receiveData(String str) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String replace = str.replace("null", "");
                Log.e("received data", replace);
               // result=replace;
                parseData(replace);
            }
        });
    }

}