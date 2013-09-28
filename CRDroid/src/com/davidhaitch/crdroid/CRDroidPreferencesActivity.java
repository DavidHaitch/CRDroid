package com.davidhaitch.crdroid;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CRDroidPreferencesActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
