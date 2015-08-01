package com.smartlockinc.smartlocks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;


/**
 * Created by SunnySingh on 7/10/2015.
 */
public class Signout extends Fragment implements  View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;

    private static final String TAG = "MainActivity";

    private static final int PROFILE_PIC_SIZE = 800;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;
    SessionManager session;
    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;
    private ConnectionResult mConnectionResult;

    private Button googlesignout, signout;
    Button fbbutton;
    private Context mContext;
    private Activity mActivity;

    public Signout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.signout,
                container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mGoogleApiClient.connect();
        googlesignout = (Button) view.findViewById(R.id.glogout);
        googlesignout.setOnClickListener(this);
        signout = (Button) view.findViewById(R.id.signout);
        signout.setOnClickListener(this);
        fbbutton = (Button) view.findViewById(R.id.fbbutton);
        fbbutton.setOnClickListener(this);
     return view;

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.glogout:

                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    session = new SessionManager(mContext);
                    session.atlogout();
                    Toast.makeText(mContext,"Loggedout!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mContext,Signupstartup.class);
                    startActivity(i);
                    getActivity().finish();
                break;
            case R.id.signout:
                session = new SessionManager(mContext);
                session.atlogout();
                Toast.makeText(mContext, "logged out!", Toast.LENGTH_LONG).show();
                Intent p = new Intent(mContext,Signupstartup.class);
                startActivity(p);
                getActivity().finish();
                break;
            case R.id.fbbutton:
                LoginManager.getInstance().logOut();
                session = new SessionManager(mContext);
                session.atlogout();
                Intent q = new Intent(mContext,Signupstartup.class);
                startActivity(q);
                getActivity().finish();
        }

    }
        @Override
    public void onActivityResult(int reqCode, int resCode, Intent i) {

       //


    }





    public void onStart() {
        super.onStart();
    }

   public void onStop() {
        super.onStop();
       if (mGoogleApiClient.isConnected()) {
           mGoogleApiClient.disconnect();
       }

   }
    private void resolveSignInError() {
        //
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
       //
    }


    @Override
    public void onConnected(Bundle arg0) {


    }


    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
