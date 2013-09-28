package com.davidhaitch.crdroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.widget.TableLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.davidhaitch.crdroid.IrcService.IrcBinder;
import com.davidhaitch.crdroid.ScheduleLoaderService.ScheduleLoaderBinder;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends SherlockFragmentActivity implements
		TabListener
{
	private String[] locations;

	public ScheduleLoaderService_ scheduleService;
	public static IrcBinder ircServiceBinder;
	public boolean isScheduleServiceBound;
	public static boolean isIrcServiceBound;
	private static int selectedTab = 0;

	private boolean isDoneCreatingTabs = false;
	
	@AfterViews
	void afterViews()
	{
		locations = getResources().getStringArray(R.array.locations);
		if (!isScheduleServiceBound)
		{
			Intent scheduleServiceIntent = new Intent(this.getApplicationContext(),
					ScheduleLoaderService_.class);
			bindService(scheduleServiceIntent, scheduleServiceConnection,
					Context.BIND_AUTO_CREATE);
		}
		
		if (!isIrcServiceBound)
		{
			Intent ircServiceIntent = new Intent(this.getApplicationContext(),
					IrcService_.class);
			bindService(ircServiceIntent, ircServiceConnection,
					Context.BIND_AUTO_CREATE);
		}
		
		configureActionBar();
	}
	
	@Override
    protected void onStop()
	{
        super.onStop();
        if (isScheduleServiceBound)
        {
            unbindService(scheduleServiceConnection);
            isScheduleServiceBound = false;
        }
    }
	private void configureActionBar()
	{
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int i = 0; i < locations.length; i++)
		{
			Tab tab = getSupportActionBar().newTab();
			tab.setText(locations[i]);
			tab.setTabListener(this);
			getSupportActionBar().addTab(tab);
			if(i == selectedTab)
			{
				getSupportActionBar().selectTab(tab);
			}
		}
		
		isDoneCreatingTabs = true;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		if(isDoneCreatingTabs)
		{
			selectedTab = tab.getPosition();
		}
		
		if (tab.getPosition() == 0)
			ft.replace(R.id.fragment_container, new RadioFragment_());
		else if (tab.getPosition() == 1)
			ft.replace(R.id.fragment_container, new ChatFragment_());
		else if (tab.getPosition() == 2)
			ft.replace(R.id.fragment_container, new ScheduleFragment());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.menu_settings:
			startActivity(new Intent(this, CRDroidPreferencesActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
	}

	private ServiceConnection scheduleServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			ScheduleLoaderBinder binder = (ScheduleLoaderBinder) service;
			scheduleService = (ScheduleLoaderService_) binder.getService();
			scheduleService.BuildSchedule();
			isScheduleServiceBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			isScheduleServiceBound = false;
		}
	};
	
	private ServiceConnection ircServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			IrcBinder binder = (IrcBinder) service;
			ircServiceBinder = binder;
			isIrcServiceBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			//isIrcServiceBound = false;
		}
	};
}
