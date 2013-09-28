package com.davidhaitch.crdroid;

public class IrcMessage
{
	public String Channel;
	public String Sender;
	public String Login;
	public String Hostname;
	public String Message;
	
	public IrcMessage()
	{
	}
	
	public IrcMessage(String message)
	{
		Message = message;
		Sender = "";
	}
	
	public IrcMessage(String channel, String sender, String login, String hostname, String message)
	{
		Channel = channel;
		Sender = sender;
		Login = login;
		Hostname = hostname;
		Message = message;
	}
}
