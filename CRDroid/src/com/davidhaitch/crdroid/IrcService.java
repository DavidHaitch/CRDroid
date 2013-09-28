package com.davidhaitch.crdroid;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EService;

@EService
public class IrcService extends Service
{
	private final IBinder binder = new IrcBinder();
	
	public IrcWrapper irc;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		InitIrcWrapper();
		return binder;
	}
	
	@Override
	public void onRebind(Intent intent)
	{
		
	}
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		return true;
	}

	@Background
	void InitIrcWrapper()
	{
		if(irc == null)
		{
			irc = new IrcWrapper();
		}
		
		if(!irc.isConnected())
		{
			irc.Init(this.getApplicationContext());
		}
	}
	
	public class IrcBinder extends Binder
	{
		IrcService getService()
		{
			return IrcService.this;
		}
	}
}
