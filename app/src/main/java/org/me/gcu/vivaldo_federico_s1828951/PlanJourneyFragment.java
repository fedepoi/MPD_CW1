package org.me.gcu.vivaldo_federico_s1828951;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class PlanJourneyFragment extends Fragment implements Observer {

    private EditText datePicker;
    final Calendar myCalendar = Calendar.getInstance();


    ListView listView;
    ArrayAdapter adapter;
    // private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private ArrayList<RoadWorkItem> a;
    private ArrayList<RoadWorkItem> all;
    private DataFeed dataFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_journey, container, false);

        datePicker = view.findViewById(R.id.plan_journey_date_picker);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        all = new ArrayList<RoadWorkItem>();
        dataFeed = new DataFeed();
        dataFeed.fetchData("plannedRoadWork", plannedRoadWorksURL);
        dataFeed.addObserver(this);


        listView = view.findViewById(R.id.journey_list);
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


        return view;
    }


    private void updateLabel() {
        ArrayList<RoadWorkItem> filteredArray = new ArrayList<RoadWorkItem>();
        String myFormat = "MM-dd-yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        datePicker.setText(dateFormat.format(myCalendar.getTime()));

        Date currentDate = myCalendar.getTime();
        for (RoadWorkItem rwi : all) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date;

            if (rwi.getStartDate() != null) {
                date = rwi.getStartDate();
                if (formatter.format(currentDate).equals(formatter.format(date))) {
                    filteredArray.add(rwi);
                }

            }
        }

        adapter = new RoadWorkItemAdapter(getActivity(),
                R.layout.map_fragment, filteredArray);
        listView.setAdapter(adapter);


    }

    @Override
    public void update(Observable o, Object arg) {
        a = dataFeed.getList();
        all.addAll(a);
    }
}
