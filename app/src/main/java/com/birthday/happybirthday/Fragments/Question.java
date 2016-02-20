package com.birthday.happybirthday.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.birthday.happybirthday.R;
import com.birthday.happybirthday.RoundedImageView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


public class Question extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static Question newInstance(String param1, String param2) {
        Question fragment = new Question();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Question() {
        // Required empty public constructor
    }

    private Picasso picasso;
    private ImageView bg;
    private RoundedImageView rImg;
    private Toolbar toolbar;

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
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        bg = (ImageView)v.findViewById(R.id.bgMain);
        rImg = (RoundedImageView)v.findViewById(R.id.mainImage);
        picasso = new Picasso.Builder(getActivity())
                .memoryCache(new LruCache(24000))
                .build();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picasso.load(baseLink+"bdayGirl.jpg").placeholder(R.drawable.def).error(R.drawable.def).into(bg);
        picasso.load(baseLink+"bdayGirl.jpg").resize(200, 200).centerCrop().placeholder(R.drawable.def).error(R.drawable.def).into(rImg);
    }
    private String baseLink = "http://bdaymsg.esy.es/DNE/images/";
    public void setUp(Toolbar toolbar){
        this.toolbar = toolbar;

    }
}
