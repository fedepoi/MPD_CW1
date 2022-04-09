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
// Federico Vivaldo S1828951
public class CheckByDateFragment extends Fragment
        //implements Observer
        {

    ListView listView;
    ArrayAdapter adapter;
    private EditText textInput;
    final Calendar myCalendar = Calendar.getInstance();


    private ArrayList<RoadWorkItem> a;
    private ArrayList<RoadWorkItem> b;
    private ArrayList<RoadWorkItem> c;
    private ArrayList<RoadWorkItem> all;
    private ListController l ;

public CheckByDateFragment(ListController l){
    this.l=l;
}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_by_date, container, false);


        a = l.getPlannedList();
        b = l.getRoadWorkItemsList();
        c = l.getIncidentsList();
        all= l.getAllList();

        listView = view.findViewById(R.id.mobile_list);

        adapter = new RoadWorkItemAdapter(getActivity(),
                R.layout.fragment_check_by_date, all);
        listView.setAdapter(adapter);


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
        return view;
    }


    private void updateLabel() {
        ArrayList<RoadWorkItem> filteredArray = new ArrayList<RoadWorkItem>();
        String myFormat = "MM-dd-yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        textInput.setText(dateFormat.format(myCalendar.getTime()));

        Date currentDate = myCalendar.getTime();
        for (RoadWorkItem rwi : all) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date date = null;

            if (rwi.getStartDate() != null) {
                Log.e("start date debug", rwi.getStartDate().toString());
                date = rwi.getStartDate();

                if (formatter.format(currentDate).equals(formatter.format(date))) {
                    filteredArray.add(rwi);
                }

            }
        }

        Log.d("old", String.valueOf(all.size()));
        Log.d("new array length", String.valueOf(filteredArray.size()));
        adapter = new RoadWorkItemAdapter(getActivity(),
                R.layout.fragment_check_by_date, filteredArray);
        listView.setAdapter(adapter);


    }

    public void updateView(ListController l) {
        a = l.getPlannedList();
        b = l.getRoadWorkItemsList();
        c = l.getIncidentsList();
        all= l.getAllList();
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
              //  loading.setVisibility(View.VISIBLE);

                Log.e("UI", "updating UI");
                adapter = new RoadWorkItemAdapter(getActivity(),
                        R.layout.fragment_check_by_date, all);
                listView.setAdapter(adapter);
            }
        });


    }


}
