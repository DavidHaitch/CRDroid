package com.davidhaitch.crdroid;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleItemAdapter extends ArrayAdapter<ScheduleItem>
{
    Context context;
    int layoutResourceId;
    List<ScheduleItem> scheduleItems = null;

    public ScheduleItemAdapter(Context context, int layoutResourceId, List<ScheduleItem> scheduleItems)
    {
        super(context, layoutResourceId, scheduleItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.scheduleItems = scheduleItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {	
        View row = convertView;
        MessageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.Timestamp = (TextView)row.findViewById(R.id.timestamp);
            holder.ShowName = (TextView)row.findViewById(R.id.showName);
            row.setTag(holder);
        }
        else
        {
            holder = (MessageHolder)row.getTag();
        }

        ScheduleItem item = scheduleItems.get(position);
        holder.Timestamp.setText(item.Timestamp + "\t");
        holder.ShowName.setText(item.ShowName);

        return row;
    }

    static class MessageHolder
    {
        TextView Timestamp;
        TextView ShowName;
    }
}

