package com.example.a1_project.Events;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a1_project.MainActivity;
import com.example.a1_project.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Event_ListAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView event_id;
        TextView event_name;
        TextView event_date;
        TextView event_location;
        Button event_view_delete;
        CheckBox checkBox;
    }

    public Event_ListAdapter(Context context, ArrayList<Event> data) {
        super(context, R.layout.event_listview_row, data);
        this.dataSet = data;
        this.mContext = context;

        //SharedPreferences prefs = context.getSharedPreferences("com.example.event", Context.MODE_PRIVATE);
        //notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        Event_ListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_listview_row, null);
            viewHolder = new Event_ListAdapter.ViewHolder();
            viewHolder.event_id = (TextView) convertView.findViewById(R.id.event_id);
            viewHolder.event_name = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.event_date = (TextView) convertView.findViewById(R.id.event_date);
            viewHolder.event_location = (TextView) convertView.findViewById(R.id.event_location);
            //viewHolder.event_view_delete = (Button) convertView.findViewById(R.id.event_view_delete);

            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Event_ListAdapter.ViewHolder) convertView.getTag();
        }

        //lastPosition = position;


       // viewHolder.event_view_delete.setTag(dataModel.getId());
        viewHolder.checkBox.setTag(dataModel.getId());

        viewHolder.event_name.setText(dataModel.getEvent());
        viewHolder.event_date.setText(dataModel.getDateTime());
        viewHolder.event_location.setText(dataModel.getLocation());

        viewHolder.event_date.setBackground(viewHolder.event_name.getBackground()); // default color for deadline



        return convertView;
    }



    // get the current datetime (dayFromNow=0) or the datetime in 24 hours (dayFromNow=1)
    public String currentTime(int dayFromNow) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, dayFromNow);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String monthText = String.valueOf(month);
        String dayText=String.valueOf(day);
        if (month<9){
            monthText="0"+ (month + 1);
        }
        if (day<10) {
            dayText ="0"+day;
        }
        String hourText = String.valueOf(hour);
        String minuteText = String.valueOf(minute);
        if (hour<10){
            hourText="0"+ hour;
        }
        if (minute<10) {
            minuteText ="0"+minute;
        }
        return year + "-" + monthText + "-" + dayText + " " + hourText + ":" + minuteText;
    }



}


