package com.maya.memories.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.memories.R;
import com.maya.memories.interfaces.InterfaceToChatBoxFragment;
import com.maya.memories.models.ChatBoxItem;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by home on 7/30/2017.
 */

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ViewHolder>
{
    List<ChatBoxItem> list;
    Context context;
    InterfaceToChatBoxFragment interfaceToChatBoxFragment;
    public ChatBoxAdapter(List<ChatBoxItem> list, Context context, InterfaceToChatBoxFragment interfaceToChatBoxFragment)
    {
        this.list=list;
        this.context=context;
        this.interfaceToChatBoxFragment=interfaceToChatBoxFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {

        try {

            Picasso
                    .with(context)
                    .load(list.get(position).profilePic)
                    .fit()
                    .into((ImageView) holder.imgPerson);

            holder.tvPersonName.setText(Utility.getCamelCase(list.get(position).userName));
            holder.tvMessageLast.setText(list.get(position).message);
            if (list.get(position).readType == 0) {
                holder.tvMessageLast.setTextColor(Color.parseColor("#2FD5A1"));
            } else {
                holder.tvMessageLast.setTextColor(Color.parseColor("#9B9B9B"));
            }

            holder.tvTimeAgo.setText(Utility.getTimeAgo(list.get(position).timestamp.getTime()));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interfaceToChatBoxFragment.onItemClick(list.get(position));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {   public TextView tvPersonName,tvMessageLast,tvTimeAgo;
        public ImageView imgPerson;
        public RelativeLayout view;
        public ViewHolder(View itemView)
        {
            super(itemView);
            view=(RelativeLayout)itemView.findViewById(R.id.view);
            tvPersonName=(TextView)itemView.findViewById(R.id.tvPersonName);
            tvMessageLast=(TextView)itemView.findViewById(R.id.tvMessageLast);
            tvTimeAgo=(TextView)itemView.findViewById(R.id.tvTimeAgo);
            imgPerson=(ImageView) itemView.findViewById(R.id.imgPerson);
        }
    }
}
