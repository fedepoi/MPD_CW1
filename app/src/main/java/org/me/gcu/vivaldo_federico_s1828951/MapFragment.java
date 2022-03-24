package org.me.gcu.vivaldo_federico_s1828951;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.io.IOException;
import java.io.StringReader;

import com.google.android.gms.maps.SupportMapFragment;
public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener,DataInterface
{


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String result = "";

    private DataFeed dataFeed;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Log.e("map fragment on create","alive");
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
//
        return view;
    }



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

                CustomInfoWindow customInfoWindow=new CustomInfoWindow(getContext());
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
        dataFeed=new DataFeed(urlSource,this);

        LatLng glasgow = new LatLng(55.860916, 	-4.251433);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 12.0f));


    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("ciao",marker.getTitle());

       marker.hideInfoWindow();
        // Custom dialog setup
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.view_more_dialog);
        dialog.setTitle("Custom Dialog Example");
// Set the custom dialog components as a TextView and Button component
        TextView text = (TextView) dialog.findViewById(R.id.infoView);
        text.setText(marker.getTitle());
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        } );
        dialog.show();



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
