package com.birthday.happybirthday.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.birthday.happybirthday.CustomDrawerLayout;
import com.birthday.happybirthday.Entity.wish;
import com.birthday.happybirthday.R;
import com.birthday.happybirthday.RoundedImageView;
import com.birthday.happybirthday.SimpleDividerItemDecoration;
import com.birthday.happybirthday.adapters.RecyclerAdapter;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NavigationFragment extends Fragment {
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    private View containerView;
    private ArrayList<wish> wishes= new ArrayList<wish>();
    private CustomDrawerLayout customDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RoundedImageView imageView;
    private SwipeRefreshLayout refreshLayout;
    private boolean mUserLearnedDrawer;
    private Toolbar toolbar;
    private boolean mFromSavedInstanceState;
    private TextView textView;
    private FragmentManager fragmentManager;
    private ProgressBar progressView;
    private Picasso picasso;
    private ImageView img;
    private String baseLink = "http://bdaymsg.esy.es/DNE/images/";

    public NavigationFragment() {
        // Required empty public constructor
    }
    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_navigation, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.namesRecycler);
        refreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.refresher);
        imageView = (RoundedImageView)v.findViewById(R.id.home);
        img =(ImageView)v.findViewById(R.id.navbg);
        progressView = (ProgressBar)v.findViewById(R.id.progress);
        recyclerAdapter = new RecyclerAdapter(getActivity());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerAdapter.setItems(wishes);
        recyclerView.isClickable();
        picasso = new Picasso.Builder(getActivity())
                .memoryCache(new LruCache(24000))
                .build();
        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] cols = getResources().getIntArray(R.array.cols);
        refreshLayout.setColorSchemeColors(cols);
        progressView.setVisibility(View.INVISIBLE);
        progressView.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.royalBlue), android.graphics.PorterDuff.Mode.MULTIPLY);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getDataFromServer().execute();
            }
        });
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picasso.load(baseLink+"bdayGirl.jpg").resize(75, 75).centerCrop().placeholder(R.drawable.def).error(R.drawable.def).into(imageView);
        picasso.load(baseLink + "bdayGirl.jpg").placeholder(R.drawable.def).error(R.drawable.def).into(img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setTitle(getString(R.string.app_name));
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Question frag = new Question();
                if(Build.VERSION.SDK_INT>=19) {
                    frag.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                    setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                }
                frag.setUp(toolbar);
                fragmentTransaction.replace(R.id.frag_container, frag, "myFrag");
                fragmentTransaction.commit();
                customDrawerLayout.closeDrawer(containerView);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*String s = "Sr no: " + wishes.get(position).getSrNo() + "\nName: " +
                        wishes.get(position).getName() + "\nQuestion: " + wishes.get(position).getQuestion() +
                        "\nAnswer: " + wishes.get(position).getAnswer() + "\nMessage" + wishes.get(position).getMessage();*/
          //      textView.setText(s);
                toolbar.setTitle(wishes.get(position).getName());
                //fragment.setUp(wishes.get(position));
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mainFragment frag = new mainFragment();
                if(Build.VERSION.SDK_INT>=19) {
                    frag.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                    setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform));
                }
                frag.setUp(wishes.get(position),fragmentManager);
                fragmentTransaction.replace(R.id.frag_container,frag,"quesFrag");
                fragmentTransaction.commit();
                customDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //async task
    private JSONArray jsonArray;
    public void addData(){
        try {

            jsonArray = new JSONArray(data);
            JSONObject jsonObject;
            wishes.clear();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                wish w = new wish();
                w.setName(jsonObject.getString("firstName") + " " + jsonObject.getString("lastName"));
                w.setSrNo("" + jsonObject.getInt("srNo"));
                w.setMessage(jsonObject.getString("message"));
                w.setQuestion(jsonObject.getString("question"));
                w.setAnswer(jsonObject.getString("answer"));
                wishes.add(w);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //drawerFragment.setUp((CustomDrawerLayout)findViewById(R.id.drawer),R.id.navFragment,mToolbar,wishes, textView);
            //textView.setText(str);
            //Toast.makeText(getActivity(),wishes.get(0).getName(),Toast.LENGTH_SHORT).show();
            recyclerAdapter.setItems(wishes);
        }

    }
    private String link = "http://bdaymsg.esy.es/responseJSON.php";
    private String data=null;
    public class getDataFromServer extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(25000);
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                data = convertStreamToString(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("SLIMF", "exveption " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("SLIMF", "exveption " + e.getMessage() );
            }
            return null;
        }
        private String convertStreamToString(InputStream inputStream) {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!refreshLayout.isRefreshing())
                progressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            addData();
            if(progressView.getVisibility()==View.VISIBLE)
                progressView.setVisibility(View.INVISIBLE);
            if(refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
        }
    }
    //async task end


    public void setUp(CustomDrawerLayout customDrawerLayout, int fragmentID, Toolbar toolbar, FragmentManager fragmentManager){
        containerView=getActivity().findViewById(fragmentID);
        //this.textView = textView;
        this.customDrawerLayout = customDrawerLayout;
        this.toolbar = toolbar;
        //recyclerAdapter.setItems(wishes);
        this.fragmentManager = fragmentManager;
        new getDataFromServer().execute();
        drawerToggle = new ActionBarDrawerToggle(getActivity(),customDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,String.valueOf(mUserLearnedDrawer));
                }
                new getDataFromServer().execute();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            customDrawerLayout.openDrawer(containerView);
        }
        customDrawerLayout.setDrawerListener(drawerToggle);
        customDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();

            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }
}
