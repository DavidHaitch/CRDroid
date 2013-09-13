package com.davidhaitch.crdroid;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by David Haddad on 8/20/13.
 */
public class IrcMessageAdapter extends ArrayAdapter<IrcMessage>
{
    Context context;
    int layoutResourceId;
    List<IrcMessage> messages = null;

    public IrcMessageAdapter(Context context, int layoutResourceId, List<IrcMessage> messages)
    {
        super(context, layoutResourceId, messages);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {	
        View row = convertView;
        MessageHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.messageText = (TextView)row.findViewById(R.id.messageText);
            holder.userName = (TextView)row.findViewById(R.id.authorName);
            row.setTag(holder);
        }
        else
        {
            holder = (MessageHolder)row.getTag();
        }

        IrcMessage message = messages.get(position);
        holder.messageText.setText(message.Message);
        holder.userName.setText(message.Sender);

        return row;
    }

    static class MessageHolder
    {
        TextView messageText;
        TextView userName;
    }
}
