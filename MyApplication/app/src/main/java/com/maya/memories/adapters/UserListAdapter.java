package com.maya.memories.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.memories.R;
import com.maya.memories.interfaces.InterfaceToUserListActivity;
import com.maya.memories.models.UserItem;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 8/2/2017.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
{
    List<UserItem> list;
    private List<UserItem> orig;
    Context context;
    InterfaceToUserListActivity interfaceToUserListActivity;
    public UserListAdapter(List<UserItem> list, Context context,InterfaceToUserListActivity interfaceToUserListActivity)
    {
        this.list=list;
        this.context=context;
        this.interfaceToUserListActivity=interfaceToUserListActivity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        UserListAdapter.ViewHolder viewHolder=new UserListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {

            Picasso
                    .with(context)
                    .load(list.get(position).profile_pic)
                    .fit()
                    .into((ImageView) holder.imgPerson);

            holder.tvPersonName.setText(Utility.getCamelCase(list.get(position).firstName+" "+list.get(position).lastName));

            holder.tvUserEmail.setText(list.get(position).email);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interfaceToUserListActivity.clickUser(list.get(position));
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
    {   public TextView tvPersonName,tvUserEmail;
        public ImageView imgPerson;
        public RelativeLayout view;
        public ViewHolder(View itemView)
        {
            super(itemView);
            view=(RelativeLayout)itemView.findViewById(R.id.view);
            tvPersonName=(TextView)itemView.findViewById(R.id.tvPersonName);
            tvUserEmail=(TextView)itemView.findViewById(R.id.tvUserEmail);
            imgPerson=(ImageView) itemView.findViewById(R.id.imgPerson);
        }
    }



    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<UserItem> results = new ArrayList<UserItem>();
                if (orig == null)
                    orig = list;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final UserItem g : orig) {
                            if ((g.getFirstName()+" "+g.getLastName()).toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<UserItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
