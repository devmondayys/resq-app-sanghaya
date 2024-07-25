package com.example.javanew_resq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends BaseAdapter {

    private Context context;
    private List<Settings2.TimeEntry> timeEntries;
    private DatabaseReference mDatabase;

    public TimeAdapter(Context context, List<Settings2.TimeEntry> timeEntries) {
        this.context = context;
        this.timeEntries = timeEntries != null ? timeEntries : new ArrayList<Settings2.TimeEntry>(); // Ensure timeEntries is not null
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public TimeAdapter(Settings2 context, List timeEntries) {

    }

    @Override
    public int getCount() {
        return timeEntries != null ? timeEntries.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return timeEntries != null ? timeEntries.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_time_entry, parent, false);
        }

        TextView timeTextView = convertView.findViewById(R.id.time_text_view);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        Settings2.TimeEntry timeEntry = timeEntries.get(position);
        if (timeEntry != null) {
            String timeString = String.format("%02d:%02d", timeEntry.getHour(), timeEntry.getMinute());
            timeTextView.setText(timeString);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("alarm/times").child(timeEntry.getKey()).removeValue();
                }
            });
        }

        return convertView;
    }
}
