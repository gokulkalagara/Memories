package com.maya.memories.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.maya.memories.R;
import com.maya.memories.interfaces.PinActivityInterface;

import java.util.List;

/**
 * Created by home on 7/27/2017.
 */

public class LockNumberAdapter extends RecyclerView.Adapter<LockNumberAdapter.ViewHolder>
{

    List<String> mList;
    private int parent_height;
    private int main_height;
    public PinActivityInterface pinActivityInterface;
    public LockNumberAdapter(List<String> mList,PinActivityInterface pinActivityInterface)
    {
        this.mList=mList;
        this.pinActivityInterface=pinActivityInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {

        if(mList.get(position).equals(" "))
        {
            holder.tvNumber.setBackgroundColor(Color.parseColor("#00000000"));
        }
        else
        {
            holder.tvNumber.setBackgroundResource(R.drawable.green_circle);
        }
        holder.tvNumber.setText(mList.get(position));
        holder.tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.tvNumber.getText().equals(" "))
                {
                    pinActivityInterface.numberClick(holder.tvNumber.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {   public TextView tvNumber;
        public LinearLayout view;
             public ViewHolder(View itemView)
        {
            super(itemView);
            view=(LinearLayout)itemView.findViewById(R.id.llhead);
            tvNumber=(TextView)itemView.findViewById(R.id.tvNumber);
        }
    }
}
