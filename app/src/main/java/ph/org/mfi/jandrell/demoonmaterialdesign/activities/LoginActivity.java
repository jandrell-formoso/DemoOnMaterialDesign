package ph.org.mfi.jandrell.demoonmaterialdesign.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.application.MFIApplication;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.network.CustomJSONRequest;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.network.VolleySingleton;

public class LoginActivity extends ActionBarActivity {

    private final static String TAG_USER = "user";
    private final static String TAG_USER_ID = "user_id";
    private final static String TAG_USERNAME = "username";
    private final static String TAG_PASSWORD = "password";
    private final static String TAG_USER_TYPE_ID = "user_type_id";
    private final static String TAG_ACTIVE = "active";
    private final static String TAG_LAST_ACTIVITY = "last_activity";
    private final static String TAG_VALID = "valid";

    String url = "http://mfiapp.site88.net/mfi_app/RestfulService/";

    private Context getActivity;

    private ProgressDialog pDialog;

    private JSONArray jsonArrayUsers = null;
    private ArrayList<HashMap<String, String>> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity = this;
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername = sh.getString(MainActivity.KEY_USERNAME, null);
        String savedPassword = sh.getString(MainActivity.KEY_PASSWORD, null);
        if (!(savedUsername == null && savedPassword == null)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.onStop();
        }

        setContentView(R.layout.activity_login);
        Log.d("JD: ", "onCreate called.");

        user = new ArrayList<HashMap<String, String>>();

        Button btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                final String username = ((TextView) findViewById(R.id.txt_username)).getText().toString();
                final String password = ((TextView) findViewById(R.id.txt_password)).getText().toString();
                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("login", "1");
                params.put("username", username);
                params.put("password", password);
                RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

                CustomJSONRequest request = new CustomJSONRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONResponse(response);

                        progressDialog.dismiss();
                        if (user.size() <= 0) {
                            new AlertDialog.Builder(getActivity)
                                    .setTitle("Information").setMessage("Invalid username or password.")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create().show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userLogged", user);

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor shEdit = sharedPreferences.edit();
                            shEdit.putString(MainActivity.KEY_USERNAME, user.get(0).get(TAG_USERNAME));
                            shEdit.putString(MainActivity.KEY_PASSWORD, user.get(0).get(TAG_PASSWORD));
                            shEdit.apply();
                            startActivity(intent);
                            LoginActivity.this.onStop();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        AlertDialog dialog = new AlertDialog.Builder(getActivity)
                                .setTitle("Information")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.setMessage(showResponseError(error));
                        dialog.show();
                    }
                });

//                StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Toast.makeText(MFIApplication.getAppContext(), response, Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MFIApplication.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        return params;
//                    }
//                };
                requestQueue.add(request);

            }
        });
    }


    public String showResponseError(VolleyError error) {
        String errorMsg = "";

        if(error instanceof NoConnectionError || error instanceof TimeoutError) {
            errorMsg = "No connection.";
        } else if(error instanceof NetworkError) {
            errorMsg = "Network error.";
        } else if(error instanceof AuthFailureError) {
            //TODO - Authentication failure
        } else if(error instanceof ServerError) {
            //TODO - Server Error
        } else if(error instanceof ParseError) {
            //TODO - ParseError
        }

        return errorMsg;
    }


    public void parseJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return;
        }
        user = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < response.length(); i++) {
            if (response.has(TAG_USER)) {
                try {
                    JSONObject jObj = response.getJSONObject(TAG_USER);
                    HashMap<String, String> u = new HashMap<String, String>();
                    u.put(TAG_USER_ID, jObj.getString(TAG_USER_ID));
                    u.put(TAG_USERNAME, jObj.getString(TAG_USERNAME));
                    u.put(TAG_PASSWORD, jObj.getString(TAG_PASSWORD));
                    u.put(TAG_USER_TYPE_ID, jObj.getString(TAG_USER_TYPE_ID));
                    u.put(TAG_ACTIVE, jObj.getString(TAG_ACTIVE));
                    u.put(TAG_LAST_ACTIVITY, jObj.getString(TAG_LAST_ACTIVITY));
                    u.put(TAG_VALID, jObj.getString(TAG_VALID));

                    user.add(u);
//
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


//    public boolean isNetworkAvailable() {
//        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = conManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    public boolean isInternetAvailable() {
//        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
//
//            if (ipAddr.equals("")) {
//                return false;
//            } else {
//                return true;
//            }
//
//        } catch (Exception e) {
//            return false;
//        }
//
//    }

}
