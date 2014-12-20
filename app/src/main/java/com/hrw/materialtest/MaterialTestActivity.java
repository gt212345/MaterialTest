package com.hrw.materialtest;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.PlusClient;


public class MaterialTestActivity extends ActionBarActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "MaterialTestActivity";
    private static final String PROFILE_ID = "107844609319480774924";
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 3;

    private GoogleApiClient mGoogleApiClient;
//    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;


    private AsyncProfPic asyncProfPic;
    private Toolbar toolbar;
    private Button intent;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView drawerList;
    private ListAdapter adapter;
    private Bitmap bitmap;
    private ImageView profPic;

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_test_activity);
        /**
          * Google Api Client init
          */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        profPic = (ImageView) findViewById(R.id.profPic);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerList = (ListView) findViewById(R.id.listitem);
        adapter = new ArrayAdapter(this,R.layout.adapter_list,
                new String[]{"Section 1","Section 2","Section 3"}
                //Drawer List
        );
        drawerList.setOnItemClickListener(this);
        drawerList.setAdapter(adapter);
        if (toolbar != null) {
            //Todo Change name
//            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        toolbar.setOnMenuItemClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            //override methods
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        intent = (Button)findViewById(R.id.intent);
        intent.setOnClickListener(this);
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_material_test_activiry, menu);
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
    }

    @Override
    public void onClick(View v) {
        Intent intent1;
        switch (v.getId()){
            case R.id.intent:
                intent1 = new Intent(this,TestActivity.class);
                startActivity(intent1);
//                this.finish();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_about:
                Log.v(TAG,"About clicked");
                break;
            case R.id.action_share:
                Log.v(TAG,"Share clicked");
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){

        }
        Log.v(TAG,position+" position clicked");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG,"Connected");
        asyncProfPic = new AsyncProfPic(mGoogleApiClient,this,bitmap, profPic);
        asyncProfPic.getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG,"Connection suspended");
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (responseCode == RESULT_OK && !mGoogleApiClient.isConnected()
                    && !mGoogleApiClient.isConnecting()) {
                // This time, connect should succeed.
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        REQUEST_CODE_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // Fetch a new result to start.
                mGoogleApiClient.connect();
            }
        }
    }
}
