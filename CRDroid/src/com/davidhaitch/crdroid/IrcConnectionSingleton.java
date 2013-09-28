package com.davidhaitch.crdroid;

import android.os.AsyncTask;

public class IrcConnectionSingleton
{
	private static IrcConnectionSingleton Instance = new IrcConnectionSingleton();
	
	public IrcWrapper Irc;
	
    public static IrcConnectionSingleton getInstance()
    {
        return Instance;
    }

    private IrcConnectionSingleton()
    {
    	new InitializeIrcWrapperTask().execute((Void[])null);
    }
}

class InitializeIrcWrapperTask extends AsyncTask<Void, Void, Void>
{
    @Override
    protected Void doInBackground(Void... nulls)
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}