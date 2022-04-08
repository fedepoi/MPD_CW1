package org.me.gcu.vivaldo_federico_s1828951;


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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class CheckByRoadFragment extends Fragment {

    ListView listView;
    ArrayAdapter adapter;
    private EditText textInput;


    private ArrayList<RoadWorkItem> a;
    private ArrayList<RoadWorkItem> b;
    private ArrayList<RoadWorkItem> all;


    private LinearLayout editTextLayout;
    private Button addBtn;
    private Button removeBtn;
    private Button filterBtn;
    private ListController l;

    public CheckByRoadFragment(ListController l) {
        this.l = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_by_road, container, false);


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
        a = l.getIncidentsList();
        b = l.getRoadWorkItemsList();

        if (a != null) {
            all.addAll(a);
        }

        if (b != null) {
            all.addAll(b);
        }


        adapter = new RoadWorkItemAdapter(getActivity(),
                R.layout.map_fragment, all);
        listView.setAdapter(adapter);

        textInput = view.findViewById(R.id.check_by_road_text_input);
        editTextLayout = view.findViewById(R.id.edit_text_linear_layout);
        addBtn = view.findViewById(R.id.add_button);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edt = new EditText(getContext());
                edt.setHint("Enter another road");
                editTextLayout.addView(edt);
            }
        });

        removeBtn = view.findViewById(R.id.remove_button);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLayout.getChildCount() > 1) {
                    editTextLayout.removeViewAt(editTextLayout.getChildCount() - 1);
                }
            }
        });

        filterBtn = view.findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<RoadWorkItem> filteredArray = new ArrayList<RoadWorkItem>();


                for (int i = 0; i < editTextLayout.getChildCount(); i++) {
                    if (editTextLayout.getChildAt(i) instanceof EditText) {
                        EditText e = (EditText) editTextLayout.getChildAt(i);

                        for (RoadWorkItem rwi : all) {
                            if (!e.getText().toString().equals("")) {
                                if (rwi.getTitle().toLowerCase().contains(e.getText().toString().toLowerCase())) {
                                    filteredArray.add(rwi);
                                }
                            }
                        }


                    }
                }


                Log.d("no filter", String.valueOf(all.size()));
                Log.d("filter", String.valueOf(filteredArray.size()));

                adapter = new RoadWorkItemAdapter(getActivity(),
                        R.layout.map_fragment, filteredArray);
                listView.setAdapter(adapter);

            }
        });


        return view;
    }

}
