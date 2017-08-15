package com.maya.memories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maya.memories.R;
import com.maya.memories.activities.ClassmatesPhotosActivity;
import com.maya.memories.constants.Constants;
import com.maya.memories.models.ClassItem;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by home on 8/3/2017.
 */

public class MemoriesItemsAdapter extends RecyclerView.Adapter<MemoriesItemsAdapter.ViewHolder>
{
    List<ClassItem> list;
    Context context;
    SharedPreferences PREFS;
    public MemoriesItemsAdapter(List<ClassItem> list, Activity context)
    {
        this.context=context;
        this.list=list;
        PREFS=context.getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_person_item,parent,false);
        MemoriesItemsAdapter.ViewHolder viewHolder=new MemoriesItemsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Picasso.with(context)
                .load(list.get(position).profilePic)
                .error(R.drawable.ic_white)
                .placeholder(R.drawable.ic_white)
                .into((holder.images));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(context, ClassmatesPhotosActivity.class);
                intent.putExtra("ClassItem",list.get(position));
                context.startActivity(intent);
                Utility.addlogs(list.get(position).rollNo,"Image",PREFS.getString(Constants.USER_UUID,""));
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
