package com.smartlockinc.smartlocks;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import android.content.BroadcastReceiver;
import android.util.Log;
import static com.smartlockinc.smartlocks.CommonUtilities.*;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.smartlockinc.smartlocks.R;


import  com.smartlockinc.smartlocks.adapter.FragmentDrawer;
import com.smartlockinc.smartlocks.adapter.NavigationDrawerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {
    private static String TAG = MainActivity.class.getSimpleName();
    TextView gcmmessgae;
    AsyncTask<Void,Void,Void>mRegisterTask;
    AlertDialogueManager alert= new AlertDialogueManager();
    ConnectionDetector cd;
    SessionManager session;
    Gcmsessionmanager gcmsessionmanager;
    Register register;
    private Toolbar mToolbar;


    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);

        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingtoInternet()) {
            alert.ShowALert(MainActivity.this, "No Internet Conection", "Connect and login again", false);

        }
        else {
            session = new SessionManager(MainActivity.this);
            if (session.checklogin() == false) {
                 Intent intent = new Intent(MainActivity.this,Signupstartup.class);
                startActivity(intent);
                finish();
                GCMRegistrar.checkManifest(this);
                GCMRegistrar.checkDevice(this);
                GCMRegistrar.register(this, SENDER_ID);

            } else {
                Toast.makeText(MainActivity.this, session.Email(), Toast.LENGTH_LONG).show();
                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        DISPLAY_MESSAGE_ACTION));
                GCMRegistrar.checkManifest(this);
                GCMRegistrar.checkDevice(this);
                gcmsessionmanager = new Gcmsessionmanager(MainActivity.this);
                if (gcmsessionmanager.getregid() == false) {
                    GCMRegistrar.register(this, SENDER_ID);
                }
            }
        }
        GCMRegistrar.setRegisteredOnServer(this, true);

        if(GCMRegistrar.isRegisteredOnServer(this))
                    {
                        gcmsessionmanager = new Gcmsessionmanager(MainActivity.this);

                        Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),gcmsessionmanager.id() , Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                        alert.ShowALert(this, "Not Registered", "Register again", false);
                        //uncomment when server is ready
                       // Intent intent = new Intent(MainActivity.this,Register.class);
                        //startActivity(intent);
                    }

            }






    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            gcmmessgae.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }


    @Override
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

        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new LockLogsFragment();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new SharedKeyFragment();
                title = getString(R.string.title_messages);
                break;
            case 3:
                    session = new SessionManager(MainActivity.this);
                    if(session.checklogin()==true) {
                        fragment = new Signout();
                        title = getString(R.string.title_signout);
                    }
                else alert.ShowALert(MainActivity.this,"Not logged in","Login In First",false);
                break;
            case 4:
                    fragment = new Register();
                    title = getString(R.string.title_register);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void unlockbuttonOnClick(View v1) {
        session = new SessionManager(MainActivity.this);
        if (session.checklogin() == true) {
            String keyword = "unlock";
            postmethod(this, keyword);
        }
        else
        {
            alert.ShowALert(MainActivity.this,"Can not unlock","Login with a valid account",false);
        }

    }

    public void lockbuttonOnClick(View v1) {
        session = new SessionManager(MainActivity.this);
        if (session.checklogin() == true) {
            String keyword = "lock";
            postmethod(this, keyword);
        }
        else
        {
            alert.ShowALert(MainActivity.this,"Can not lock","Login with a valid account",false);
        }

    }

    public  void postmethod(final Context context, final String keyword) {

            RequestQueue rq = Volley.newRequestQueue(context);
            JsonObjectRequest postReq = new JsonObjectRequest(Request.Method.POST, "http://ip.jsontest.com/", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String data = response.getString("ip");
                        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");

                }
            }) {
                @Override

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyword", keyword);
                    return params;
                }


            };

            rq.add(postReq);



    }



}

