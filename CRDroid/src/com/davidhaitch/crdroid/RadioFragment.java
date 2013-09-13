package com.davidhaitch.crdroid;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
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
	
	@ViewById
	TextView artist;
	
	@ViewById
	TextView title;
	
	private StreamStatistics stats;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
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
		ListenersTextView.setText(stats.CurrentListeners + " Now Listening!");
		title.setText(stats.SongHistory.get(0).SongName);
		artist.setText(stats.SongHistory.get(0).Artist);
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
