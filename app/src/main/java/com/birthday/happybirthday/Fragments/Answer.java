package com.birthday.happybirthday.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birthday.happybirthday.Entity.wish;
import com.birthday.happybirthday.R;
import com.birthday.happybirthday.RoundedImageView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


public class Answer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Answer() {
        // Required empty public constructor
    }

    private wish mWish;
    private TextView textView;
    private RoundedImageView rImageView;
    private ImageView bg;
    private Picasso picasso;
    private String baseLink = "http://bdaymsg.esy.es/DNE/images/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);
        textView = (TextView)v.findViewById(R.id.msgTxt);
        rImageView = (RoundedImageView)v.findViewById(R.id.msgImg);
        bg = (ImageView)v.findViewById(R.id.bgMsg);
        picasso = new Picasso.Builder(getActivity())
                .memoryCache(new LruCache(24000))
                .build();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText(mWish.getMessage());
        if(Build.VERSION.SDK_INT>=19) {
            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        }
        bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picasso.load(baseLink+mWish.getSrNo()+".jpg").resize(200, 200).placeholder(R.drawable.def).centerCrop().error(R.drawable.def).into(rImageView);
        picasso.load(baseLink + mWish.getSrNo() + ".jpg").placeholder(R.drawable.def).placeholder(R.drawable.def).error(R.drawable.def).into(bg);

    }

    public void setUp(wish mWish){
        this.mWish = mWish;
    }
}
