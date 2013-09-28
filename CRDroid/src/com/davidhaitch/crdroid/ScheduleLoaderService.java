package com.davidhaitch.crdroid;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EService;

@EService
public class ScheduleLoaderService extends Service
{
	private String scheduleJson;
	private final IBinder binder = new ScheduleLoaderBinder();

	private Map<Time, String> schedule = new HashMap<Time, String>();
	
	public ScheduleLoaderService()
	{
		super();
	}

	@Override
	public IBinder onBind(Intent arg0)
	{	
		return binder;
	}
	
	public String GetShowForHourAndDay(int day, int hour)
	{ 
		for(Time date : schedule.keySet())
		{
			int tmp_hour = date.hour;
			int tmp_day = date.weekDay;
			if(tmp_hour == hour && tmp_day == day)
			{
				return schedule.get(date);
			}
		}
		
		return "DJ Derpy Hour";
	}
	
	public void BuildSchedule()
	{
		LoadRawJson();
		int timeout = 0;
		while (scheduleJson == null && timeout < 40)
		{
			try
			{
				Thread.sleep(250);
				timeout++;
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		if(scheduleJson == null)
		{
			return;
		}

		JsonObject ScheduleObject = parseJson(scheduleJson);
		JsonObject ScheduleHours = ScheduleObject.get("schedule").getAsJsonObject();
		
		String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
		Calendar inferredDate = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		inferredDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		inferredDate.setFirstDayOfWeek(Calendar.MONDAY);
		inferredDate.set(Calendar.HOUR_OF_DAY, 0);
		int c = 0;
		for(String day : days)
		{
			JsonArray dayEntries = ScheduleHours.get(day).getAsJsonArray();
			for(int i = 0; i < dayEntries.size(); i++)
			{	
				Calendar newdate = inferredDate;
				newdate.set(Calendar.HOUR_OF_DAY, i);
				newdate.set(Calendar.MINUTE, 0);
				newdate.set(Calendar.SECOND, 0);
				newdate.set(Calendar.MILLISECOND, 0);
				Time time = new Time();
				time.hour = i;
				time.weekDay = inferredDate.get(Calendar.DAY_OF_WEEK) - 1;
				schedule.put(time, dayEntries.get(i).getAsString());
			}
			
			c++;
			inferredDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return;
	}

	private JsonObject parseJson(String json)
	{
		JsonParser parser = new JsonParser();
		return parser.parse(json).getAsJsonObject();
	}

	@Background
	void LoadRawJson()
	{
		try
		{
			URL url = new URL("http://ponify.me/schedule.json.php?v=2");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(1000);
			InputStream rawJson = urlConnection.getInputStream();
			java.util.Scanner s = new java.util.Scanner(rawJson)
					.useDelimiter("\\A");
			if (s.hasNext())
			{
				ScheduleLoaderService.this.scheduleJson = s.next();
			} else
			{
				return;
			}
		} catch (Exception e)
		{
			return;
		}
	}

	public class ScheduleLoaderBinder extends Binder
	{
		ScheduleLoaderService getService()
		{
			return ScheduleLoaderService.this;
		}
	}
}
