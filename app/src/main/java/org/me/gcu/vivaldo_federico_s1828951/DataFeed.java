package org.me.gcu.vivaldo_federico_s1828951;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class DataFeed extends Observable {
    private String url;
    private String result;
  //  private final DataInterface dataInt;

    private ArrayList<RoadWorkItem> list;
    private String type;


//    DataFeed(DataInterface dataInt) {
//        this.dataInt = dataInt;
//    }
//
//    public DataFeed(String aurl, DataInterface dataInt) {
//        this.url = aurl;
//        this.dataInt = dataInt;
//        doSomeTaskAsync();
//    }

    public DataFeed (String type, String url){
        list = new ArrayList<RoadWorkItem>();
        this.url=url;
        this.type=type;
        doSomeTaskAsync();
    }


public ArrayList<RoadWorkItem> getList(){
        return this.list;
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
    private void parseRoadData(String str, String rwiType) {
        try {
            RoadWorkItem roadWork = new RoadWorkItem(rwiType);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(str));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                    Log.e("start xml doc", "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("title")) {

                        String temp = xpp.nextText();
                        roadWork.setTitle(temp);
                    } else if (xpp.getName().equalsIgnoreCase("description")) {

                        String temp = xpp.nextText();
                        roadWork.setDesc(temp);
                    } else if (xpp.getName().equalsIgnoreCase("link")) {

                        String temp = xpp.nextText();
                        roadWork.setLink(temp);
                    } else if (xpp.getName().equalsIgnoreCase("point")) {

                        String temp = xpp.nextText();
                        roadWork.setPoint(temp);
                    } else if (xpp.getName().equalsIgnoreCase("author")) {

                        String temp = xpp.nextText();
                        roadWork.setAuthor(temp);
                    } else if (xpp.getName().equalsIgnoreCase("comments")) {

                        String temp = xpp.nextText();
                        roadWork.setComments(temp);
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {

                        String temp = xpp.nextText();
                        roadWork.setPubDate(temp);
                    }


                }
                double lt = parseStringToDouble(roadWork.getLat());
                double ln = parseStringToDouble(roadWork.getLon());
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
//                switch (rwiType) {
//                    case "plannedRoadWork":
//                        plannedRoadWorksArray.add(roadWork);
//                        break;
//                    case "roadWork":
//                        roadWorksArray.add(roadWork);
//                        break;
//                    case "incident":
//                        incidentsArray.add(roadWork);
//                        break;
//                }

                list.add(roadWork);

                eventType = xpp.next();

            }
        } catch (XmlPullParserException ae1) {
            Log.e("pull parser exception", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("io exception", "IO error during parsing");
        }

        setChanged();
        notifyObservers();
        System.out.println("End document");

    }
    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? Double.NaN : Double.parseDouble(value);
    }
    //---------------------------------------------------------------------------------------------------------

    public void doSomeTaskAsync() {
        //   String result;
        HandlerThread ht = new HandlerThread("MyHandlerThread");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Object response = msg.obj;
                String str = (String) response;
                String replace = str.replace("null", "");
                parseRoadData(replace,type);
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // your async code goes here.
                Log.e("DataFeed", "Starting async thread");


                URL aurl;
                URLConnection yc;
                BufferedReader in = null;
                String inputLine = "";

                try {
                    aurl = new URL(url);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        result = result + inputLine;

                        //System.out.println(inputLine);

                    }
                    in.close();
                } catch (IOException ae) {
                    Log.e("run error", "ioexception in run");
                }
                // create message and pass any object here doesn't matter
                // for a simple example I have used a simple string
                Message message = new Message();
                message.obj = result;

                asyncHandler.sendMessage(message);

            }
        };
        asyncHandler.post(runnable);
    }


//    private void doSomethingOnUi(Object response)
//    {
//        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.post(new Runnable() {
//            @Override
//            public void run() {
//                // now update your UI here
//                // cast response to whatever you specified earlier
//                String msg = (String) response;
//                String replace = msg.replace("null", "");
//                Log.e("ui thread", replace);
//
//
//                dataInt.receiveData(msg);
//
//            }
//        });
//    }


}

