package com.davidhaitch.crdroid;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_radio)
public class RadioFragment extends SherlockFragment
{
	@ViewById
	Button PlayButton;

	@ViewById
	TextView ListenersTextView;
	
	private StreamStatistics stats;
	
	NotificationCompat.Builder notificationBuilder;
	NotificationManager notificationManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(notificationBuilder == null)
		{
			notificationBuilder = new NotificationCompat.Builder(getActivity());
		}
		
		if(notificationManager == null)
		{
			notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		}
		
		SetButtonState();
		LoadStatsLoop();
        return null;
    }
	
	@Click(R.id.PlayButton)
	void OnClickPlay()
	{
		if(RadioPlayerSingleton.getInstance().isLoading)
		{
			return;
		}
		
		RadioPlayerSingleton.getInstance().isLoading = true;
		SetButtonState();
		OperateRadio();
	}
	
	@Background
	void OperateRadio()
	{
		RadioPlayerSingleton.getInstance().TogglePlayStream();
		RadioPlayerSingleton.getInstance().isLoading = false;
		
		if(RadioPlayerSingleton.getInstance().IsPlaying())
		{
			notificationManager.notify(101, notificationBuilder.getNotification());
		}
		else
		{
			notificationManager.cancel(101);
		}
		
		SetButtonState();
	}

	@UiThread
	void SetButtonState()
	{	
		if(RadioPlayerSingleton.getInstance().isLoading && !RadioPlayerSingleton.getInstance().IsPlaying())
		{
			PlayButton.setText("Buffering...");
			return;
		}
		
		if(RadioPlayerSingleton.getInstance().IsPlaying())
		{
			PlayButton.setText(R.string.pause_text);
			return;
		}
		
		if(!RadioPlayerSingleton.getInstance().isLoading && !RadioPlayerSingleton.getInstance().IsPlaying())
		{
			PlayButton.setText(R.string.play_text);
			return;
		}
	}
	
	@UiThread
	void UpdateStats()
	{
		ListenersTextView.setText(stats.CurrentListeners + " Listening Now To " + stats.ServerTitle + "!");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(stats.SongHistory.get(0).SongName);
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle(stats.SongHistory.get(0).Artist);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setContentTitle(stats.SongHistory.get(0).SongName);
        notificationBuilder.setContentText(stats.SongHistory.get(0).Artist);
        PendingIntent currentIntent = PendingIntent.getActivity(getActivity(), 0, getActivity().getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(currentIntent);
        notificationBuilder.setOngoing(true);
        
        if(RadioPlayerSingleton.getInstance().IsPlaying())
        {
        	
        }
	}
	
	@Background
	void LoadStats()
	{
		InputStream rawJson;
		try
		{
	        URL url = new URL(getString(R.string.stats));
	        URLConnection urlConnection = url.openConnection();
	        urlConnection.setConnectTimeout(1000);
	        rawJson = urlConnection.getInputStream();
	        String jsonStr;
	        java.util.Scanner s = new java.util.Scanner(rawJson).useDelimiter("\\A");	        
	        if(s.hasNext())
	        {
	          jsonStr = s.next();
	        }
	        else
	        {
	        	return;
	        }
	        
	        Gson gson = new Gson();
	        stats = gson.fromJson(jsonStr, StreamStatistics.class);
	        UpdateStats();
	    }
		catch(Exception ex)
	    {
	        return;
	    }
	}
	
	@Background
	void LoadStatsLoop()
	{
        while(true)
        {
        	LoadStats();
        	try
        	{
				Thread.sleep(10000);
			}
        	catch (InterruptedException e)
			{
				e.printStackTrace();
			}
        }
	}
}
