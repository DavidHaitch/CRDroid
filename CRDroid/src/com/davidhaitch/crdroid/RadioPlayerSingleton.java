package com.davidhaitch.crdroid;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class RadioPlayerSingleton
{
	private static RadioPlayerSingleton Instance = new RadioPlayerSingleton();

	private MediaPlayer mediaPlayer;
	
	public boolean isLoading;
	
	public boolean IsPlaying()
	{
		return mediaPlayer.isPlaying();
	}
	
    public static RadioPlayerSingleton getInstance()
    {
        return Instance;
    }

    private RadioPlayerSingleton()
    {
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
    
    public void TogglePlayStream()
    {
    	if(!mediaPlayer.isPlaying())
    	{
    		PlayStream();
    	}
    	else
    	{
    		mediaPlayer.stop();
    	}
    }

    private void PlayStream()
    {
    	SetStreamSource();
    	PreparePlayer();
		mediaPlayer.start();
    }
    
    private boolean PreparePlayer()
    {
    	try 
    	{
			mediaPlayer.prepare();
			return true;
		} 
    	catch (IllegalStateException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    private boolean SetStreamSource()
    {
    	try
    	{
			mediaPlayer.setDataSource("http://molestia.ponify.me:8062/stream");
			return true;
		}
    	catch (IllegalArgumentException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (SecurityException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IllegalStateException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IOException e)
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
}
