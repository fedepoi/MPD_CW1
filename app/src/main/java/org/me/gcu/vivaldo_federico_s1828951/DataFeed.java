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


public class DataFeed {
    private String url;
    private String result;
    private final DataInterface dataInt;


    DataFeed(DataInterface dataInt) {
        this.dataInt = dataInt;
    }

    public DataFeed(String aurl, DataInterface dataInt) {
        this.url = aurl;
        this.dataInt = dataInt;
        doSomeTaskAsync();
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
                //  doSomethingOnUi(response);
                dataInt.receiveData((String) response);
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

