package ph.org.mfi.jandrell.demoonmaterialdesign.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.application.MFIApplication;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.ServiceHandler;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.singleton.VolleySingleton;

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


    private ProgressDialog pDialog;

    private JSONArray jsonArrayUsers = null;
    private ArrayList<HashMap<String, String>> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        String savedUsername = sh.getString(MainActivity.KEY_USERNAME, null);
        String savedPassword = sh.getString(MainActivity.KEY_PASSWORD, null);
        if(!(savedUsername == null && savedPassword == null)) {
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
                String username = ((TextView) findViewById(R.id.txt_username)).getText().toString();
                String password = ((TextView) findViewById(R.id.txt_password)).getText().toString();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("login", "1");
                params.put("username", username);
                params.put("password", password);
                RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
                JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(MFIApplication.getAppContext(), response.getJSONArray("user").toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MFIApplication.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(request);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

//    class GetUsers extends AsyncTask<Void, Void, Void> {
//
//        private List<NameValuePair> params = null;
//        private boolean success = false;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(LoginActivity.this);
//            pDialog.setTitle("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (pDialog.isShowing()) pDialog.dismiss();
//            Log.d("ONPOSTEX", " > " + user.toString());
//
//            if (isNetworkAvailable() || isInternetAvailable()) {
//                if (success) {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("userLogged", user);
//
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                    SharedPreferences.Editor shEdit = sharedPreferences.edit();
//                    shEdit.putString(MainActivity.KEY_USERNAME, user.get(0).get(TAG_USERNAME));
//                    shEdit.putString(MainActivity.KEY_PASSWORD, user.get(0).get(TAG_PASSWORD));
//                    shEdit.apply();
//                    startActivity(intent);
//                    LoginActivity.this.onStop();
//                } else {
//                    new AlertDialog.Builder(LoginActivity.this).setTitle("Information")
//                            .setMessage("Invalid username or password.")
//                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).create().show();
//
//                }
//            } else {
//                new AlertDialog.Builder(LoginActivity.this).setTitle("Information")
//                        .setMessage("No Internet Connection.")
//                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create().show();
//            }
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                ServiceHandler sh = new ServiceHandler();
//
//                String username = ((EditText) findViewById(R.id.txt_username)).getText().toString();
//                String password = ((EditText) findViewById(R.id.txt_password)).getText().toString();
//
//                this.params = new ArrayList<NameValuePair>();
//                this.params.add(new BasicNameValuePair("login", "1"));
//                this.params.add(new BasicNameValuePair("username", username));
//                this.params.add(new BasicNameValuePair("password", password));
//                this.params.add(new BasicNameValuePair("format", "json"));
//
//                String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, this.params);
//
//                if (jsonStr != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(jsonStr);
//
//                        if (jsonObject.getString(TAG_USER).equals("null")) {
//                            return null;
//                        } else {
//
//
//                            JSONObject jObj = jsonObject.getJSONObject(TAG_USER);
//                            HashMap<String, String> u = new HashMap<>();
//                            user = new ArrayList<HashMap<String, String>>();
//
//                            u.put(TAG_USER_ID, jObj.getString(TAG_USER_ID));
//                            u.put(TAG_USERNAME, jObj.getString(TAG_USERNAME));
//                            u.put(TAG_PASSWORD, jObj.getString(TAG_PASSWORD));
//                            u.put(TAG_USER_TYPE_ID, jObj.getString(TAG_USER_TYPE_ID));
//                            u.put(TAG_ACTIVE, jObj.getString(TAG_ACTIVE));
//                            u.put(TAG_LAST_ACTIVITY, jObj.getString(TAG_LAST_ACTIVITY));
//                            u.put(TAG_VALID, jObj.getString(TAG_VALID));
//
//                            Log.d("UserValid", " > " + u.toString());
//                            user.add(u);
//                            success = true;
//                        }
//
//                        Log.d("UsersValid", " > " + user.toString());
////                            }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
////                new AlertDialog.Builder(LoginActivity.this)
////                        .setTitle("ServiceHandler")
////                        .setMessage("Couldn't get any data from the url.")
////                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        }).create().show();
//
//                    Log.d("ServiceHandler", "Couldn't get any data from the url.");
//                }
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//
//            return null;
//        }
//    }
//
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
