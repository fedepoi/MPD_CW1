package org.me.gcu.vivaldo_federico_s1828951;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CheckByDateFragment extends Fragment implements Observer {

    ListView listView;
    ArrayAdapter adapter;
    private ArrayList<RoadWorkItem> a;
    private DataFeed dataFeed;
    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_check_by_date, container, false);
        adapter = new ArrayAdapter<RoadWorkItem>(getContext(),
                R.layout.map_fragment, a);

         listView = view.findViewById(R.id.mobile_list);

         dataFeed= new DataFeed();
         dataFeed.fetchData("plannedRoadWork", plannedRoadWorksURL);
        dataFeed.addObserver(this);


        Log.e("date","alive");
        return view;
    }



    @Override
    public void update(Observable o, Object arg) {
        Log.e("updating", "received update from df");
        Log.d("arg", o.toString());

        a = dataFeed.getList();


        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                Log.e("UI", "updating UI");
                adapter = new RoadWorkItemAdapter(getActivity(),
                        R.layout.map_fragment, a);
                listView.setAdapter(adapter);

            }
        });


    }


}
