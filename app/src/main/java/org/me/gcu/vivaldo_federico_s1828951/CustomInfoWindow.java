package org.me.gcu.vivaldo_federico_s1828951;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;


public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindow(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);

    }


    private void renderWindowContent(Marker marker, View view) {
        String title = marker.getTitle();

        RoadWorkItem rwi = (RoadWorkItem) marker.getTag();

        TextView titleText = (TextView) view.findViewById(R.id.infoWindowTitleText);
        TextView startText = (TextView) view.findViewById(R.id.infoWindowStartDate);
        TextView endText = (TextView) view.findViewById(R.id.infoWindowEndDate);
//        Button btn = (Button) view.findViewById(R.id.viewMoreBtn);


        if (!title.equals("")) {
            titleText.setText(title);

            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyyy - HH:mm");

            if(rwi.getStartDate() !=null){
                startText.setText("Start date: "+formatter.format(rwi.getStartDate()));
            } else { startText.setText("Start date not provided");}

            if(rwi.getEndDate() !=null){
                endText.setText("End date: "+formatter.format(rwi.getEndDate()));
            } else { endText.setText("End date not provided");}




//            startText.append(rwi.getStartDate().toString());
//            endText.append(rwi.getEndDate().toString());
        }
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowContent(marker, mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowContent(marker, mWindow);
        return mWindow;
    }


}
