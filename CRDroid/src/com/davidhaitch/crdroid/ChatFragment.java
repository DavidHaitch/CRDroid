package com.davidhaitch.crdroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.TextChange;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_chat)
public class ChatFragment extends SherlockFragment
{
	@ViewById
	ListView chatListView;
	
	@ViewById
	EditText chatMessageBox;
	
	private int lastMessageCount = 0;
	
	private IrcMessageAdapter ircMessageAdapter;
	
	private static IrcWrapper ircWrapper = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(ircWrapper == null)
		{
			ircWrapper = new IrcWrapper();
		}
		
		if(!ircWrapper.isConnected())
		{
			InitIrcWrapper();
		}
		
		Activity activity = this.getActivity();
		int layoutResource = R.layout.layout_irc_message;
		ircMessageAdapter = new IrcMessageAdapter(activity, layoutResource, ircWrapper.MessageHistory);
		SetUpChatView();
		new MonitorChatTask().execute(ircWrapper);
        return null;
    }
	
	@TextChange(R.id.chatMessageBox)
	void OnMessageType(CharSequence text)
	{
		String textStr = text.toString();
		if(textStr.contains("\n"))
		{
			textStr.replace('\n', ' ');
			ircWrapper.SendMessage(textStr);
			chatMessageBox.setText("");
			chatMessageBox.clearFocus();
			chatListView.requestFocus();
		}
	}
	
	@Background
	void InitIrcWrapper()
	{
		ircWrapper.Init("David_Haitch_CRDroid");
	}
	
	@UiThread
	void SetUpChatView()
	{
		chatListView.setAdapter(ircMessageAdapter);	
	}

	@UiThread
	void RestockChatView()
	{
		chatListView.setSelection(ircMessageAdapter.getCount() - 1);
		lastMessageCount = ircMessageAdapter.getCount();
	}
	
	class MonitorChatTask extends AsyncTask<IrcWrapper, Void, Void>
    {
        private Boolean running = true;
        @Override
        protected Void doInBackground(IrcWrapper... ghost)
        {
            try
            {
            	this.publishProgress((Void[])null);
            	while(running)
            	{
            		if(ChatFragment.ircWrapper.MessageHistory.size() > 0 && ChatFragment.ircWrapper.MessageHistory.size() > ChatFragment.this.lastMessageCount)
            		{ 	
            			ChatFragment.this.RestockChatView();
            		}
            		
            		Thread.sleep(250);
            	}
            	
                return null;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}
