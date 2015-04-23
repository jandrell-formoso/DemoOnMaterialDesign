package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.DTRAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.constants.Constants;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.DTRData;
import ph.org.mfi.jandrell.demoonmaterialdesign.json.Keys;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.network.VolleySingleton;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.MultiSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class DTRFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private MultiSwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ArrayList<DTRData> dtrData;
    private DTRAdapter dtrAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public DTRFragment() {
        // Required empty public constructor
    }

    public static DTRFragment newInstance(String param1, String param2) {
        DTRFragment fragment = new DTRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dtr, container, false);
        refreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.refresh_dtr);
        refreshLayout.setSwipeableChildren(R.id.list_dtr);
        refreshLayout.setOnRefreshListener(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                sendDTRRequest();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.list_dtr);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void sendDTRRequest() {
        String url = Constants.KEY_URL_MFI + "?" +
                Constants.KEY_URL_USER_ID + "=82&" +Keys.DTRKeys.KEY_URLParam_DTR + "=1";
        dtrData = new ArrayList<DTRData>();
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dtrData = parseJSONRequest(response);
                if(refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                dtrAdapter = new DTRAdapter(getActivity(), dtrData);
                recyclerView.setAdapter(dtrAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    toast = Toast.makeText(getActivity(), "No Connection.", Toast.LENGTH_LONG);

                } else if (error instanceof AuthFailureError) {
                    //TODO - Authentication Failure
                } else if (error instanceof ServerError) {
                    //TODO - Server Error
                } else if (error instanceof NetworkError) {
                    toast = Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_LONG);
                } else if (error instanceof ParseError) {
                    //TODO - Parse Error
                    toast = Toast.makeText(getActivity(), "Parse error.", Toast.LENGTH_LONG);
                }
                toast.show();
                if(refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public ArrayList<DTRData> parseJSONRequest(JSONObject response) {
        ArrayList<DTRData> data = new ArrayList<>();
        if(response.has(Keys.DTRKeys.KEY_USER_DTR)) {
            try {
                JSONArray array = response.getJSONArray(Keys.DTRKeys.KEY_USER_DTR);
                for(int i=0;i<array.length();i++) {
                    if (array.getJSONObject(i).has(Keys.DTRKeys.KEY_DTR)) {
                        JSONObject jsonObject = array.getJSONObject(i).getJSONObject(Keys.DTRKeys.KEY_DTR);
                        DTRData dtr = new DTRData(jsonObject.getString(Keys.DTRKeys.KEY_REMARKS),
                                Time.valueOf(jsonObject.getString(Keys.DTRKeys.KEY_HOURS)).getTime(),
                                Time.valueOf(jsonObject.getString(Keys.DTRKeys.KEY_TIME_OUT)).getTime(),
                                Time.valueOf(jsonObject.getString(Keys.DTRKeys.KEY_TIME_IN)).getTime(),
                                Date.valueOf(jsonObject.getString(Keys.DTRKeys.KEY_DATE)),
                                Integer.parseInt(jsonObject.getString(Keys.DTRKeys.KEY_INFO_ID)),
                                Integer.parseInt(jsonObject.getString(Keys.DTRKeys.KEY_OJT_ID)),
                                Integer.parseInt(jsonObject.getString(Keys.DTRKeys.KEY_DTR_ID)));
                        Log.d("JD", "DTRData > " + dtr.toString());
                        data.add(dtr);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return data;
    }


    @Override
    public void onRefresh() {
        sendDTRRequest();
    }
}
