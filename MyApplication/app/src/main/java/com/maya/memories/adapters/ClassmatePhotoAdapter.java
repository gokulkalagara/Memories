package com.maya.memories.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maya.memories.R;
import com.maya.memories.constants.Constants;
import com.maya.memories.interfaces.InterfaceToClassmatesPhotoActivity;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by home on 8/5/2017.
 */

public class ClassmatePhotoAdapter extends RecyclerView.Adapter<ClassmatePhotoAdapter.ViewHolder>
{
    List<String> list;
    Context context;
    InterfaceToClassmatesPhotoActivity interfaceToClassmatesPhotoActivity;
    SharedPreferences PREFS;
    public ClassmatePhotoAdapter(List<String> list, Context context, InterfaceToClassmatesPhotoActivity interfaceToClassmatesPhotoActivity)
    {
        this.context=context;
        this.list=list;
        this.interfaceToClassmatesPhotoActivity=interfaceToClassmatesPhotoActivity;
        PREFS=context.getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.class_person_item,parent,false);
      ViewHolder viewHolder=new ViewHolder(view);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        Picasso.with(context)
                .load(list.get(position))
                .error(R.drawable.ic_white)
                .placeholder(R.drawable.ic_white)
                .into((holder.images));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                interfaceToClassmatesPhotoActivity.addImageFrame(((BitmapDrawable)holder.images.getDrawable()).getBitmap());
                Utility.addlogs(list.get(position),"Image",PREFS.getString(Constants.USER_UUID,""));
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
