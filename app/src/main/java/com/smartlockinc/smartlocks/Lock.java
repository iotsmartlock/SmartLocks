package com.smartlockinc.smartlocks;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anant on 02/08/15.
 */
public class Lock extends IntentService {
    final String URl = "http://smartlock-smith.appspot.com/smartlock";//TODO Add the url to send the lock
    final String TAG = Lock.class.getSimpleName();
    public Lock() {
        super("Lock");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        lockMe(getApplicationContext());

    }

    public void lockMe(final Context context) {


        RequestQueue rq = Volley.newRequestQueue(context);
        CustomRequest request = new CustomRequest(Request.Method.POST, URl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString("action");
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action","lock");
                params.put("islogin","true");
                return params;
            }
        };
        rq.add(request);

    }
        public class CustomRequest extends Request<JSONObject> {

            private Response.Listener<JSONObject> listener;
            private Map<String, String> params;

            public CustomRequest(String url, Map<String, String> params,
                                 Listener<JSONObject> reponseListener, ErrorListener errorListener) {
                super(Method.GET, url, errorListener);
                this.listener = reponseListener;
                this.params = params;
            }

            public CustomRequest(int method, String url, Map<String, String> params,
                                 Listener<JSONObject> reponseListener, ErrorListener errorListener) {
                super(method, url, errorListener);
                this.listener = reponseListener;
                this.params = params;
            }

            protected Map<String, String> getParams()
                    throws com.android.volley.AuthFailureError {

                return params;
            };

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                // TODO Auto-generated method stub
                listener.onResponse(response);
            }
        }

    }
