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

    private ArrayList<RoadWorkItem> list;
    private String type;

    private RoadWorkItem roadWork;


    public DataFeed(String type, String url) {
        list = new ArrayList<RoadWorkItem>();
        this.url = url;
        this.type = type;
        doSomeTaskAsync();
    }


    public ArrayList<RoadWorkItem> getList() {
        return this.list;
    }

    private void parseRoadData(String str, String rwiType) {

        try {
            Log.e("-------------------------------------->", str);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(str));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                roadWork = new RoadWorkItem(rwiType);

                //roadWork = new RoadWorkItem(rwiType);
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.i("start xml doc", "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {

                    //  Log.e("tag name -->",xpp.getName());

                    //  if (xpp.getName().equalsIgnoreCase("item")){

                    if (xpp.getName().equalsIgnoreCase("item")) {

                        parseItem(xpp, rwiType);
                        double lt = parseStringToDouble(roadWork.getLat());
                        double ln = parseStringToDouble(roadWork.getLon());


                        list.add(roadWork);
                    }
//                    Log.e("item",xpp.getName());
//                         if (xpp.getName().equalsIgnoreCase("title")) {
//                             Log.e("title",xpp.getName());
//                             String temp = xpp.nextText();
//                             roadWork.setTitle(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("description")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setDesc(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("link")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setLink(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("point")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setPoint(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("author")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setAuthor(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("comments")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setComments(temp);
//                         } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
//
//                             String temp = xpp.nextText();
//                             roadWork.setPubDate(temp);
//                         }


                }


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


    private void parseItem(XmlPullParser parser, String rwiType) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, XmlPullParser.NO_NAMESPACE, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equalsIgnoreCase("title")) {
                Log.e("title", parser.getName());
                String temp = parser.nextText();
                roadWork.setTitle(temp);
            } else if (parser.getName().equalsIgnoreCase("description")) {

                String temp = parser.nextText();
                roadWork.setDesc(temp);
            } else if (parser.getName().equalsIgnoreCase("link")) {

                String temp = parser.nextText();
                roadWork.setLink(temp);
            } else if (parser.getName().equalsIgnoreCase("point")) {

                String temp = parser.nextText();
                roadWork.setPoint(temp);
            } else if (parser.getName().equalsIgnoreCase("author")) {

                String temp = parser.nextText();
                roadWork.setAuthor(temp);
            } else if (parser.getName().equalsIgnoreCase("comments")) {

                String temp = parser.nextText();
                roadWork.setComments(temp);
            } else if (parser.getName().equalsIgnoreCase("pubDate")) {

                String temp = parser.nextText();
                roadWork.setPubDate(temp);
            } else {
                parser.next();
            }

        }
    }


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

                parseRoadData(replace, type);
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
                    }
                    in.close();
                } catch (IOException ae) {
                    Log.e("run error", "ioexception in run");
                }
                Message message = new Message();
                message.obj = result;

                asyncHandler.sendMessage(message);

            }
        };
        asyncHandler.post(runnable);
    }


}

