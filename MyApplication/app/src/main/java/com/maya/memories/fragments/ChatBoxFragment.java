package com.maya.memories.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.memories.R;
import com.maya.memories.activities.ChatActivity;
import com.maya.memories.activities.MainActivity;
import com.maya.memories.activities.UserListActivity;
import com.maya.memories.adapters.ChatBoxAdapter;
import com.maya.memories.constants.Constants;
import com.maya.memories.databases.DataInOut;
import com.maya.memories.interfaces.InterfaceToChatBoxFragment;
import com.maya.memories.models.ChatBoxItem;
import com.maya.memories.utils.Utility;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatBoxFragment extends Fragment implements InterfaceToChatBoxFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChatBoxFragment() {
        // Required empty public constructor
    }


    public static ChatBoxFragment chatBoxFragment=null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatBoxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatBoxFragment newInstance(String param1, String param2) {
        ChatBoxFragment fragment = new ChatBoxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    SwipeRefreshLayout refresher;
    RecyclerView recyclerView;
    DataInOut dataInOut;
    InterfaceToChatBoxFragment interfaceToChatBoxFragment;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        chatBoxFragment=this;
        View v= inflater.inflate(R.layout.fragment_chat_box, container, false);
        interfaceToChatBoxFragment=(InterfaceToChatBoxFragment)this;
        dataInOut=new DataInOut(getActivity());
        refresher=(SwipeRefreshLayout)v.findViewById(R.id.refresher);
        recyclerView=(RecyclerView) v.findViewById(R.id.recyclerView);
        fab=(FloatingActionButton) v.findViewById(R.id.fab);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        List<ChatBoxItem> list=dataInOut.getChatBox();
        dataInOut.close();
        if(list!=null)
        recyclerView.setAdapter(new ChatBoxAdapter(list,getActivity(),interfaceToChatBoxFragment));


        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                dataInOut.open(getActivity());
                List<ChatBoxItem> list=dataInOut.getChatBox();
                dataInOut.close();
                if(list!=null)
                recyclerView.setAdapter(new ChatBoxAdapter(list,getActivity(),interfaceToChatBoxFragment));
                refresher.setRefreshing(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Utility.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent(getActivity(), UserListActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Utility.showToast(getActivity(),"Error",Constants.CONNECTION_ERROR,false);
                }
            }
        });





        return v;
    }

    @Override
    public void onItemClick(ChatBoxItem chatBoxItem)
    {
            dataInOut=new DataInOut(getActivity());
            dataInOut.updateReadMessage(chatBoxItem.u_uid);
            dataInOut.close();
            Intent intent=new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("type","0");
            intent.putExtra("ChatBoxItem",chatBoxItem);
            startActivity(intent);
    }

    public void changeView()
    {
        dataInOut=new DataInOut(getActivity());
        List<ChatBoxItem> list=dataInOut.getChatBox();
        dataInOut.close();
        if(list!=null)
        recyclerView.setAdapter(new ChatBoxAdapter(list,getActivity(),interfaceToChatBoxFragment));
    }
}
