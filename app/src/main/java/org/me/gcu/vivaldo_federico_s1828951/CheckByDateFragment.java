package org.me.gcu.vivaldo_federico_s1828951;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class CheckByDateFragment extends Fragment implements Observer {

    ListView listView;
    ArrayAdapter adapter;
    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private EditText textInput;
    private ProgressBar loading;
    final Calendar myCalendar = Calendar.getInstance();


    private ArrayList<RoadWorkItem> a;
    private ArrayList<RoadWorkItem> b;
    private ArrayList<RoadWorkItem> c;
    private ArrayList<RoadWorkItem> all;
    private DataFeed dataFeed1;
    private DataFeed dataFeed2;
    private DataFeed dataFeed3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_by_date, container, false);
        adapter = new ArrayAdapter<RoadWorkItem>(getContext(),
                R.layout.fragment_check_by_date, all);

        listView = view.findViewById(R.id.mobile_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RoadWorkItem rwi = (RoadWorkItem) parent.getItemAtPosition(position);
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.view_more_dialog);
                dialog.setTitle("Custom Dialog Example");
                TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
                TextView startDate = (TextView) dialog.findViewById(R.id.dialog_startDate);
                TextView endDate = (TextView) dialog.findViewById(R.id.dialog_endDate);
                TextView link = (TextView) dialog.findViewById(R.id.dialog_link);
                TextView pubDate = (TextView) dialog.findViewById(R.id.dialog_pubDate);
                TextView desc = (TextView) dialog.findViewById(R.id.dialog_description);


                title.setText(rwi.getTitle());


                SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyyy - HH:mm");
                if (rwi.getStartDate() != null) {
                    startDate.setText("Start date: " + formatter.format(rwi.getStartDate()));
                } else {
                    startDate.setText("Start date not provided");
                }

                if (rwi.getEndDate() != null) {
                    endDate.setText("End date: " + formatter.format(rwi.getEndDate()));
                } else {
                    endDate.setText("End date not provided");
                }


//                startDate.append(rwi.getStartDate().toString());
//                endDate.append(rwi.getEndDate().toString());
                link.append(rwi.getLink());
                pubDate.append(rwi.getPubDate());
                desc.append(rwi.getDesc());
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        all = new ArrayList<RoadWorkItem>();

        dataFeed1 = new DataFeed();
        dataFeed1.fetchData("plannedRoadWork", plannedRoadWorksURL);
        dataFeed1.addObserver(this);

        dataFeed2 = new DataFeed();
        dataFeed2.fetchData("roadWork", roadWorksURL);
        dataFeed2.addObserver(this);
        dataFeed3 = new DataFeed();
        dataFeed3.fetchData("incident", incidentsURL);
        dataFeed3.addObserver(this);


        loading = view.findViewById(R.id.check_by_date_loading);
        textInput = view.findViewById(R.id.check_by_date_text_input);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        textInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        loading.setVisibility(View.VISIBLE);
        return view;
    }


    private void updateLabel() {
        ArrayList<RoadWorkItem> filteredArray = new ArrayList<RoadWorkItem>();
        String myFormat = "MM-dd-yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        textInput.setText(dateFormat.format(myCalendar.getTime()));

        Date currentDate = myCalendar.getTime();
        for (RoadWorkItem rwi : all) {
            SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM yyyy - HH:mm");
            Date date = null;

            if (rwi.getStartDate() != null) {
                Log.e("start date debug", rwi.getStartDate().toString());
                date = rwi.getStartDate();

                if (currentDate.before(date)) {
                    filteredArray.add(rwi);
                }

            }
        }

        Log.d("old", String.valueOf(all.size()));
        Log.d("new array length", String.valueOf(filteredArray.size()));
        adapter = new RoadWorkItemAdapter(getActivity(),
                R.layout.map_fragment, filteredArray);
        listView.setAdapter(adapter);


    }


    @Override
    public void update(Observable o, Object arg) {

        Log.e("updating", "received update from df");
        Log.d("arg", o.toString());
        //   Log.d("arg1", arg.toString());
        ArrayList<RoadWorkItem> tmp;


        a = dataFeed1.getList();
        b = dataFeed2.getList();
        c = dataFeed3.getList();
        if (a != null) {
            all.addAll(a);
        }

        if (b != null) {
            all.addAll(b);
        }
        if (c != null) {
            all.addAll(c);
        }


        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);

                Log.e("UI", "updating UI");
                adapter = new RoadWorkItemAdapter(getActivity(),
                        R.layout.map_fragment, all);
                listView.setAdapter(adapter);


                if (dataFeed1.getFinished()&& dataFeed2.getFinished()&& dataFeed3.getFinished()) {
                    loading.setVisibility(View.GONE);
                }

                // loading.setVisibility(View.GONE);
            }
        });


    }


}
