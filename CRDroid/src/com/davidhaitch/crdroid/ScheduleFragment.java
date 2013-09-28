package com.davidhaitch.crdroid;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

public class ScheduleFragment extends SherlockFragment
{
	private ScheduleItemAdapter scheduleItemAdapter;

	private boolean scheduleBuilt = false;
	ListView scheduleListView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleListView = (ListView) layout.findViewById(R.id.scheduleListView);
        scheduleListView.setSelector(android.R.color.transparent); 
        if(!scheduleBuilt)
        {
        	BuildScheduleTable();
        }
        
        return layout;
    }
	
	void BuildScheduleTable()
	{
		if(((MainActivity)getActivity()).isScheduleServiceBound)
		{
			ScheduleLoaderService scheduleService = ((MainActivity)getActivity()).scheduleService;
			
			Time today = new Time();
			today.setToNow();
			ArrayList<ScheduleItem> scheduleItems = new ArrayList<ScheduleItem>();
			for (int i = 0; i < 24; i++)
			{
				String TimeStamp = i + ":00";
				String ShowName = scheduleService.GetShowForHourAndDay(today.weekDay, i);
				scheduleItems.add(new ScheduleItem(TimeStamp, ShowName));
			}
			
			scheduleItemAdapter = new ScheduleItemAdapter(getActivity(), R.layout.layout_schedule_item, scheduleItems);
			scheduleListView.setAdapter(scheduleItemAdapter);
		}
	}
}
