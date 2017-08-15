package com.maya.memories.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maya.memories.R;
import com.maya.memories.activities.VideoViewActivity;
import com.maya.memories.models.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by home on 8/6/2017.
 */

public class TrendingVideosAdapter extends RecyclerView.Adapter<TrendingVideosAdapter.ViewHolder>
{
    List<VideoItem> list;
    Context context;
    public TrendingVideosAdapter(List<VideoItem> list, Context context)
    {
        this.list=list;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_person_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context)
                .load(list.get(position).photoUrl)
                .error(R.drawable.ic_white)
                .placeholder(R.drawable.ic_white)
                .into((holder.images));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, VideoViewActivity.class);
                intent.putExtra("VideoItem",list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView images;

        public ViewHolder(View itemView) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.ivItemGridImage);
        }

    }

}
