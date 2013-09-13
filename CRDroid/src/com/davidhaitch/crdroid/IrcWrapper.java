package com.davidhaitch.crdroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class IrcWrapper extends PircBot
{
	public List<IrcMessage> MessageHistory;
	private String Username;
	private String Channel;

	public IrcWrapper()
	{
	}

	public void Init(String username)
	{
		Username = username;
		Channel = "#CelestiaRadio";
		if(MessageHistory == null)
		{
			MessageHistory = new ArrayList<IrcMessage>();
		}
		
		this.setName(Username);
		try
		{
			this.connect("irc.canternet.org");
		}
		catch (NickAlreadyInUseException e)
		{		
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (IrcException e)
		{
			e.printStackTrace();
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
}
