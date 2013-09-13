
package com.davidhaitch.crdroid;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity
    extends SherlockFragmentActivity
    implements TabListener
{
    private String[] locations;
    
    
    @AfterViews
    void afterViews()
    {
        locations = getResources().getStringArray(R.array.locations);
        configureActionBar();
    }

    private void configureActionBar()
    {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        for (String location: locations) 
        {
            Tab tab = getSupportActionBar().newTab();
            tab.setText(location);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
    	if(tab.getPosition() == 0)
    		ft.replace(R.id.fragment_container, new RadioFragment_());
    	else if(tab.getPosition() == 1)
    		ft.replace(R.id.fragment_container, new ChatFragment_());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater();
        return true;
    }
    
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
    
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {}
}
