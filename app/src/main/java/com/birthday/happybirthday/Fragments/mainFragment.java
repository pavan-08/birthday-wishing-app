package com.birthday.happybirthday.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.birthday.happybirthday.CustomDrawerLayout;
import com.birthday.happybirthday.Entity.wish;
import com.birthday.happybirthday.R;
import com.birthday.happybirthday.RoundedImageView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class mainFragment extends Fragment {

    private String photoName;
    private wish mWish=null;
    private TextView text;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String baseLink = "http://bdaymsg.esy.es/DNE/images/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Picasso picasso;
    private ImageView bg;
    private Button skipButton;
    private EditText ansText;
    private TextInputLayout wrapper;
    private RoundedImageView imageView;
    private FragmentManager fragmentManager;

    public mainFragment() {
        // Required empty public constructor
    }

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
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        text = (TextView)v.findViewById(R.id.sampleText);
        skipButton = (Button)v.findViewById(R.id.skip_button);
        ansText = (EditText)v.findViewById(R.id.editAns);
        wrapper = (TextInputLayout)v.findViewById(R.id.textLayout);
        wrapper.setHint("Answer");
        wrapper.setHapticFeedbackEnabled(true);
        imageView = (RoundedImageView)v.findViewById(R.id.personImage);
        bg = (ImageView)v.findViewById(R.id.bg);
        skipButton.requestFocus();
        picasso = new Picasso.Builder(getActivity())
                .memoryCache(new LruCache(24000))
                .build();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text.setText(mWish.getQuestion());
        photoName = mWish.getSrNo();
        bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picasso.load(baseLink+photoName+".jpg").resize(200, 200).centerCrop().placeholder(R.drawable.def).error(R.drawable.def).into(imageView);
        picasso.load(baseLink + photoName + ".jpg").placeholder(R.drawable.def).placeholder(R.drawable.def).error(R.drawable.def).into(bg);
        //Toast.makeText(getActivity(), baseLink + photoName + ".jpg", Toast.LENGTH_LONG).show();
        wrapper.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Toast.makeText(getActivity(),ansText.getText().toString()+" "+mWish.getAnswer(),Toast.LENGTH_SHORT).show();
                if (s.toString().equalsIgnoreCase(mWish.getAnswer())) {

                    wrapper.setErrorEnabled(false);
                    /*
                    hiding keyboard start
                     */
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    /*
                    hiding keyboard end
                     */
                    commitTransaction();
                } else{
                    wrapper.setError("wrong, keep trying.");
                }
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Blondeness, the answer is '" + mWish.getAnswer() + "'");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commitTransaction();
                    }
                });
                alertDialog.show();

            }
        });
    }

    public void commitTransaction(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Answer frag = new Answer();
        frag.setUp(mWish);
        if(Build.VERSION.SDK_INT>=19) {
            frag.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
        }
        fragmentTransaction.addSharedElement(imageView, "image");
        fragmentTransaction.addSharedElement(text,"text");
        fragmentTransaction.replace(R.id.frag_container, frag, "quesFrag");
        fragmentTransaction.commit();
    }
/*
    public void setUp(CustomDrawerLayout drawerLayout, int fragmentID, LayoutInflater inflater){
        this.drawerLayout = drawerLayout;
        layoutInflater = inflater;
        containerView = getActivity().findViewById(fragmentID);
        bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picasso.load(baseLink+"bdayGirl.jpg").placeholder(R.drawable.def).error(R.drawable.def).into(bg);
        picasso.load(baseLink+"bdayGirl.jpg").resize(200, 200).centerCrop().placeholder(R.drawable.def).error(R.drawable.def).into(imageView);
    }
*/
    public void setUp( wish mWish, FragmentManager fragmentManager){
        this.mWish =mWish;
        this.fragmentManager = fragmentManager;
    }
}
