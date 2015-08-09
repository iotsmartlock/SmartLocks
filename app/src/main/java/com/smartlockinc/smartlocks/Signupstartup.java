package com.smartlockinc.smartlocks;


    import android.app.Activity;
    import android.content.Context;
    import android.content.ContextWrapper;
    import android.content.DialogInterface;

    import com.gc.materialdesign.views.ButtonRectangle;
    import com.gc.materialdesign.views.CustomView;

    import android.content.Intent;
    import android.content.IntentSender;
    import android.content.pm.PackageInfo;
    import android.content.pm.PackageManager;
    import android.content.pm.Signature;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.drawable.Drawable;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.Environment;
    import android.support.v4.app.Fragment;
    import android.util.Base64;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.Toast;

    import com.facebook.GraphRequest;
    import com.facebook.GraphResponse;
    import com.facebook.internal.CallbackManagerImpl;
    import com.facebook.login.widget.LoginButton;
    import com.facebook.CallbackManager;
    import com.facebook.FacebookCallback;
    import com.facebook.FacebookException;
    import com.facebook.FacebookSdk;
    import com.facebook.login.LoginResult;
    import com.facebook.login.widget.LoginButton;
    import com.google.android.gms.common.SignInButton;
    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.GooglePlayServicesUtil;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
    import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
    import com.google.android.gms.plus.Plus;
    import com.google.android.gms.plus.model.people.Person;
    import com.facebook.login.LoginManager;
    import com.google.android.gcm.GCMRegistrar;

    import com.squareup.picasso.Picasso;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.Arrays;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.InputStream;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import com.squareup.picasso.Target;

    import static com.smartlockinc.smartlocks.CommonUtilities.*;

    /**
     * Created by SunnySingh on 7/5/2015.
     */
    public class Signupstartup extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        AlertDialogueManager alert = new AlertDialogueManager();
        private GoogleApiClient mGoogleApiClient;
        private CallbackManager callbackmanager;
        private LoginButton fbbutton;
        private static final int RC_SIGN_IN = 0;
        private boolean mIntentInProgress;
        private ConnectionResult mConnectionResult;
        private SignInButton signinButton;
        ConnectionDetector cd;
        SessionManager session;
        EditText password;
        photourl uri;
        EditText email;
        static String Emailid;
        static String Password;
        Context mcontext;
        ButtonRectangle register;
        private static int RC_FB_SIGN_IN;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.fragment_register);
            mcontext = getApplicationContext();

            fbbutton = (LoginButton) findViewById(R.id.fbbutton);
            fbbutton.setOnClickListener(this);
            RC_FB_SIGN_IN = fbbutton.getRequestCode();
            signinButton = (SignInButton) findViewById(R.id.btn_sign_in);
            signinButton.setOnClickListener(this);

            cd = new ConnectionDetector(getApplicationContext());

            if (!cd.isConnectingtoInternet()) {
                alert.ShowALert(getApplicationContext(), "Internet Connection error", "Connect to a network", false);


            }


            if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                    || SENDER_ID.length() == 0) {
                // GCM sernder id / server url is missing
                alert.ShowALert(getApplicationContext(), "Configuration Error!",
                        "Please set your Server URL and GCM Sender ID", false);
                // stop executing code by return

            }

            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();


            email = (EditText) findViewById(R.id.Email);
            password = (EditText) findViewById(R.id.Password);
            register = (ButtonRectangle) findViewById(R.id.btnRegister);

            register.setOnClickListener(new CustomView.OnClickListener() {
                public void onClick(View v) {
                    Password = password.getText().toString();
                    Emailid = email.getText().toString();

                    if (Password.equals("") && Emailid.equals("")) {
                        alert.ShowALert(Signupstartup.this, "Registering Error", "Please Enter Valid Details", false);

                    } else {
                        session = new SessionManager(mcontext);
                        session.atlogin(Emailid, Password);
                        Intent i = new Intent(mcontext, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }


            });

        }



        @Override
        protected void onActivityResult(int reqCode, int resCode, Intent i) {

                   if(reqCode==RC_SIGN_IN) {
                       if (resCode == Activity.RESULT_OK) {


                       }
                       mIntentInProgress = false;
                       if (!mGoogleApiClient.isConnecting()) {
                           mGoogleApiClient.connect();
                       }
                   }

                   if(reqCode== RC_FB_SIGN_IN) {
                       callbackmanager.onActivityResult(reqCode, resCode, i);
                   }


            }





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
        private void resolveSignInError() {
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult(this,RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (!result.hasResolution()) {
                GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
                return;
            }

            if (!mIntentInProgress) {
                // store mConnectionResult
                mConnectionResult = result;


            }
        }


        @Override
        public void onConnected(Bundle arg0) {
            session=new SessionManager(mcontext);
            if(session.checklogin()==false) {
                Toast.makeText(mcontext, "Connected", Toast.LENGTH_LONG).show();
                getProfileInformation();
            }
        }

        private void getProfileInformation() {
            try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    Password = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    Emailid = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    session = new SessionManager(mcontext);
                    //uri = new photourl(mcontext);
                    session.atlogin(Emailid, Password);
                    //uri.photourl(personPhotoUrl);
                    Intent i = new Intent(mcontext, MainActivity.class);
                    startActivity(i);
                    finish();



                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onConnectionSuspended(int cause) {
            mGoogleApiClient.connect();

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sign_in:
                    googlePlusLogin();
                    break;
                case R.id.fbbutton:
                    onfblogin();
                    break;

            }

        }


        private void googlePlusLogin() {
            if (!mGoogleApiClient.isConnecting()) {
                resolveSignInError();
            }
        }

        private void googlePlusLogout() {
            if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                session.atlogout();

            }

        }
        private void onfblogin()
        {
            callbackmanager = CallbackManager.Factory.create();

            // Set permissions
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

            LoginManager.getInstance().registerCallback(callbackmanager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            System.out.println("Success");
                            GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject json, GraphResponse response) {
                                            if (response.getError() != null) {
                                                // handle error
                                                System.out.println("ERROR");
                                            } else {
                                                System.out.println("Success");
                                                try {

                                                    String jsonresult = String.valueOf(json);
                                                    System.out.println("JSON Result" + jsonresult);
                                                    String str_id = json.getString("id");
                                                    session = new SessionManager(mcontext);
                                                    session.atlogin(str_id, str_id);
                                                    Intent fbLogged = new Intent();
                                                    fbLogged.setClass(mcontext, MainActivity.class);
                                                    startActivity(fbLogged);
                                                    finish();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                    }).executeAsync();

                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG,"On cancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d(TAG,error.toString());

                        }
                    });
        }

    }

