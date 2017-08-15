package com.maya.memories.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.memories.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionImageViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DescriptionImageViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescriptionImageViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionImageViewFragment newInstance(String param1, String param2) {
        DescriptionImageViewFragment fragment = new DescriptionImageViewFragment();
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

    TextView tvHead,tvMessage;
    ImageView imageLove,imageNoti,imageChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_description_image_view, container, false);
        imageChat=(ImageView)v.findViewById(R.id.imageChat);
        imageLove=(ImageView)v.findViewById(R.id.imageLove);
        imageNoti=(ImageView)v.findViewById(R.id.imageNoti);

        tvHead=(TextView)v.findViewById(R.id.tvHead);
        tvMessage=(TextView)v.findViewById(R.id.tvMessage);

        if(mParam2!=null)
        {
            if (mParam2.equals("1"))
            {
                imageLove.setVisibility(View.VISIBLE);
                imageNoti.setVisibility(View.INVISIBLE);
                imageChat.setVisibility(View.INVISIBLE);

                tvHead.setText("GET MEMORIES");
                tvMessage.setText("even bittersweet ones\nare better than nothing");
            }
            else if(mParam2.equals("2"))
            {
                imageLove.setVisibility(View.INVISIBLE);
                imageNoti.setVisibility(View.INVISIBLE);
                imageChat.setVisibility(View.VISIBLE);

                tvHead.setText("GET CHAT");
                tvMessage.setText("permanent testimony of\n relationship");

            }
            else
            {
                imageLove.setVisibility(View.INVISIBLE);
                imageNoti.setVisibility(View.VISIBLE);
                imageChat.setVisibility(View.INVISIBLE);

                tvHead.setText("GET NOTIFY");
                tvMessage.setText("Always stay notified\n instantly");


            }

        }


        return v;
    }

}
