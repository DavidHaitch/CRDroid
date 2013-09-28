package com.davidhaitch.crdroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
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
	Button sendMessageButton;
	
	@ViewById
	EditText chatMessageBox;
	
	private int lastMessageCount = 0;
	
	private IrcMessageAdapter ircMessageAdapter;
	
	private MonitorChatTask monitorChatTask;
	
	private static IrcService ircService;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		if(((MainActivity)getActivity()).isIrcServiceBound && ircService == null)
		{
			ircService = ((MainActivity)getActivity()).ircServiceBinder.getService();
		}
		
		Activity activity = this.getActivity();
		int layoutResource = R.layout.layout_irc_message;
		ircMessageAdapter = new IrcMessageAdapter(activity, layoutResource, ircService.irc.MessageHistory);
		SetUpChatView();
		monitorChatTask = new MonitorChatTask();
		monitorChatTask.execute();
        return null;
    }
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		chatMessageBox.clearFocus();
		chatListView.requestFocus();
		monitorChatTask.cancel(true);
	}
	
	@Click(R.id.sendMessageButton)
	void onSendMessageClick()
	{
		ircService.irc.SendMessage(chatMessageBox.getText().toString());
		chatMessageBox.setText("");
	}

	
	@UiThread
	void SetUpChatView()
	{
		chatListView.setSelector(android.R.color.transparent); 
		chatListView.setAdapter(ircMessageAdapter);	
	}

	@UiThread
	void ReframeChatView()
	{
		chatListView.setSelection(ircMessageAdapter.getCount() - 1);
		lastMessageCount = ircMessageAdapter.getCount();
	}
	
	class MonitorChatTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... nulls)
        {
            try
            {
            	this.publishProgress((Void[])null);
            	while(true)
            	{
            		if(ChatFragment.ircService.irc.MessageHistory.size() > 0 && ChatFragment.ircService.irc.MessageHistory.size() > ChatFragment.this.lastMessageCount)
            		{ 	
            			ChatFragment.this.ReframeChatView();
            		}
            		
            		Thread.sleep(250);
            	}
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}
