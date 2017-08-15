package com.maya.memories.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.maya.memories.R;
import com.maya.memories.interfaces.InterfaceToClassmatesPhotoActivity;

import static android.R.attr.bitmap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static InterfaceToClassmatesPhotoActivity interfaceToClassmates;
    public static Bitmap bitmapMain;

    public ImageViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageViewFragment newInstance(String param1, String param2, Bitmap bitmap, InterfaceToClassmatesPhotoActivity interfaceToClassmatesPhotoActivity) {
        ImageViewFragment fragment = new ImageViewFragment();
        interfaceToClassmates=interfaceToClassmatesPhotoActivity;
        bitmapMain=bitmap;
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

    RelativeLayout close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_image_view, container, false);
        close=(RelativeLayout)view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceToClassmates.onClose();
            }
        });

        SubsamplingScaleImageView imageView=(SubsamplingScaleImageView) view.findViewById(R.id.imageView);
        if(bitmapMain!=null)
        imageView.setImage(ImageSource.bitmap(bitmapMain));
        return view;
    }

}
