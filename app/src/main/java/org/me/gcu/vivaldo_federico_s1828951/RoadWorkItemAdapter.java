package org.me.gcu.vivaldo_federico_s1828951;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RoadWorkItemAdapter extends ArrayAdapter<RoadWorkItem> {

    private Activity activity;
    private ArrayList<RoadWorkItem> rwi;
    private static LayoutInflater inflater = null;

    public RoadWorkItemAdapter(Activity activity, int textViewResourceId, ArrayList<RoadWorkItem> rwi) {
        super(activity, textViewResourceId, rwi);
        try {
            this.activity = activity;
            this.rwi = rwi;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return rwi.size();
    }

    public RoadWorkItem getItem(RoadWorkItem position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_title;
        public TextView display_type;
        public TextView display_time;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.custom_list_view, null);
                holder = new ViewHolder();

                holder.display_title = (TextView) vi.findViewById(R.id.listViewText);
                holder.display_type = (TextView) vi.findViewById(R.id.rwi_type);
                holder.display_time = (TextView) vi.findViewById(R.id.time);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            Log.e("hh", String.valueOf(rwi.get(position).getDifferenceHours()));


            if (rwi.get(position).getTimeDifference() >= 172800000) {  //48hh milliseconds
                holder.display_time.setTextColor(Color.RED);
                holder.display_time.setText("Be there for more than 48h");
            } else if (rwi.get(position).getTimeDifference() >= 43200000) {  //12h in milliseconds
                holder.display_time.setTextColor(Color.YELLOW);
                holder.display_time.setText("Be there for more than 12h");
            } else if (rwi.get(position).getTimeDifference() == 0) {
                holder.display_time.setTextColor(Color.GREEN);
                holder.display_time.setText("Be there for few hours");
            }


            holder.display_title.setText(rwi.get(position).getTitle());
            holder.display_type.setText(rwi.get(position).getParsedType());


        } catch (Exception e) {


        }
        return vi;
    }

}
