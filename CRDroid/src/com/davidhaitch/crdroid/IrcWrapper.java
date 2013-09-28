package com.davidhaitch.crdroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class IrcWrapper extends PircBot
{
	public List<IrcMessage> MessageHistory;
	private String Username;
	private String Password;
	private String Server;
	private String Channel;

	
	public IrcWrapper()
	{
		MessageHistory = new ArrayList<IrcMessage>();
	}

	public void Init(Context context)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Date now = new Date();
		Random rand = new Random(now.getTime());
		Username = sharedPref.getString("username", "CRDroid_pony_" + rand.nextInt(10000));
		Password = sharedPref.getString("nickservpassword", "");
		Channel = "#CelestiaRadio";
		Server = "irc.canternet.org";
		if(MessageHistory == null)
		{
			MessageHistory = new ArrayList<IrcMessage>();
		}
		
		this.setName(Username);
		try
		{
			this.connect(Server);
		}
		catch (NickAlreadyInUseException e)
		{		
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		} 
		catch (IrcException e)
		{
			e.printStackTrace();
			return;
		}
		
		this.joinChannel(Channel);
	}
	
	public void SendMessage(String message)
	{
		MessageHistory.add(new IrcMessage(Channel, Username, "NULL", "NULL", message));
		sendMessage(Channel, message);
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		IrcMessage tmpMessage = new IrcMessage(channel, sender, login, hostname, message);
		MessageHistory.add(tmpMessage);
	}
	
	
	protected void onAction(String sender, String login,  String hostname, String target, String action)
	{
		IrcMessage tmpMessage = new IrcMessage("Action", sender, login, hostname, sender + " " + action);
		MessageHistory.add(tmpMessage);
	}
	
	@Override
	protected void onConnect()
	{
		MessageHistory.add(new IrcMessage("Connected to " + Server));
		if(Password != "")
		{
			sendMessage("Nickserv", "identify " + Password);
		}
	}
	
	protected void onJoin(String channel, String sender, String login, String hostname)
	{
		MessageHistory.add(new IrcMessage("Now talking in " + Channel ));
	}
	
	protected void onDisconnect()
	{
		MessageHistory.add(new IrcMessage("Disconnected"));
	}
}
