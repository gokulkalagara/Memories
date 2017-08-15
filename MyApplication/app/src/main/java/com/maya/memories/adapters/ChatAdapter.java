package com.maya.memories.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.memories.R;
import com.maya.memories.interfaces.InterfaceToChatAdapter;
import com.maya.memories.models.ChatItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by home on 8/1/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements InterfaceToChatAdapter
{
    public int SEND = 1;
    public int RECEIVE = 0;
    List<ChatItem> list;
    public ChatAdapter(List<ChatItem> list)
    {
        this.list=list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if(viewType == SEND){
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send,parent,false);
            ViewHolder viewHolder = new ViewHolder(rootView);
            return viewHolder;
        }
        else
        {//if(viewType == RECEIVE){
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receive,parent,false);
            NormalHolder normalHolder = new NormalHolder(rootView);
            return normalHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        try {
            if (list.get(position).messageType == 1) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.tvMessage.setText(list.get(position).message);
                viewHolder.tvTime.setText(timeFormat.format(list.get(position).timestamp).toUpperCase());

            }
            else
            {
                NormalHolder normalHolder = (NormalHolder) holder;
                normalHolder.tvMessage.setText(list.get(position).message);
                normalHolder.tvTime.setText(timeFormat.format(list.get(position).timestamp).toUpperCase());

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void addItem(ChatItem chatItem)
    {
        list.add(chatItem);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {   public TextView tvMessage,tvTime;
        public ViewHolder(View itemView)
        {
            super(itemView);

            tvMessage=(TextView)itemView.findViewById(R.id.tvMessage);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
        }
    }

    public class NormalHolder extends RecyclerView.ViewHolder
    {
    public TextView tvMessage,tvTime;
    public NormalHolder(View itemView)
    {
        super(itemView);

        tvMessage=(TextView)itemView.findViewById(R.id.tvMessage);
        tvTime=(TextView)itemView.findViewById(R.id.tvTime);
    }
    }


    @Override
    public int getItemViewType(int position) {
        if(list.get(position).messageType == 1){
            return SEND;
        }
        else{
            return RECEIVE;
        }

    }

}
