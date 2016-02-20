package com.birthday.happybirthday;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.birthday.happybirthday.Entity.wish;
import com.birthday.happybirthday.Fragments.NavigationFragment;
import com.birthday.happybirthday.Fragments.Question;
import com.birthday.happybirthday.Fragments.mainFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ArrayList<wish> wishes;
    private Toolbar mToolbar;
    private NavigationFragment drawerFragment;
    private Question fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolBar);
       // textView = (TextView)findViewById(R.id.textView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        wishes = new ArrayList<wish>();
        /*fragment = (Question)getSupportFragmentManager().findFragmentById(R.id.main_frag);
        fragment.setUp(mToolbar,R.id.main_frag);*/
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new Question();
        fragment.setUp(mToolbar);
        fragmentTransaction.add(R.id.frag_container,fragment,"myFrag");
        fragmentTransaction.commit();
        drawerFragment =(NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navFragment);
        drawerFragment.setUp((CustomDrawerLayout)findViewById(R.id.drawer),R.id.navFragment,mToolbar, fragmentManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
