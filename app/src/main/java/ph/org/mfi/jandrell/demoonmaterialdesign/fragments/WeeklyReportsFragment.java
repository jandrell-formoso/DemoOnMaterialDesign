package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.WeeklyReportsAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.constants.Constants;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.WeeklyReportsData;
import ph.org.mfi.jandrell.demoonmaterialdesign.json.Keys;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.network.VolleySingleton;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.MultiSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyReportsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<WeeklyReportsData> weeklyReports;
    private WeeklyReportsAdapter weeklyReportsAdapter;
    private MultiSwipeRefreshLayout refreshLayout;
    private View reportsView;
    ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyReportsFragment newInstance(String param1, String param2) {
        WeeklyReportsFragment fragment = new WeeklyReportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WeeklyReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_weekly_reports, container, false);
        reportsView = view;
        refreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.refresh_reports);
        refreshLayout.setSwipeableChildren(R.id.card_view_my);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.card_view_my);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        recyclerView.setAdapter(weeklyReportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void sendWeeklyReportRequest() {
        String url = Constants.KEY_URL_MFI + "?" + Constants.KEY_URL_USER_ID + "=82&" + Constants.KEY_URL_WEEKLY_REPORT + "=1";
        weeklyReports = new ArrayList<>();
//            HashMap<String, String> params = new HashMap<>();
//            params.put(Constants.KEY_URL_USER_ID, "82");
//            params.put(Constants.KEY_URL_WEEKLY_REPORT, "1");
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weeklyReports = parseWeeklyJSON(response);
                weeklyReportsAdapter = new WeeklyReportsAdapter(weeklyReports, getActivity());
                (reportsView.findViewById(R.id.linlaHeaderProgress)).setVisibility(View.GONE);
                if(refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = validateError(error);
                CardView cardView = (CardView) reportsView.findViewById(R.id.handbook_card_error);
                cardView.setVisibility(View.VISIBLE);
                Log.d("JD", errorMessage);
            }
        });
        requestQueue.add(jsonRequest);
    }

    public String validateError(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            return "No connection. Swipe down to refresh.";
        } else if (error instanceof AuthFailureError) {
            return "Authentication Failure.";
        } else if (error instanceof ServerError) {
            return "Server Error. Swipe down to refresh.";
        } else if (error instanceof ParseError) {
            return "Parsing the data error" + error.getMessage();
        } else if (error instanceof NetworkError) {
            return "Network error. Swipe down to refresh.";
        } else {
            return "";
        }
    }

    public ArrayList<WeeklyReportsData> parseWeeklyJSON(JSONObject response) {
        ArrayList<WeeklyReportsData> data = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        if (response.has(Constants.KEY_URL_WEEKLY_REPORT)) {
            try {
                JSONArray arrayWeeklyReports = response.getJSONArray(Constants.KEY_URL_WEEKLY_REPORT);
                for (int i = 0; i < arrayWeeklyReports.length(); i++) {
                    if (arrayWeeklyReports.getJSONObject(i).has(Keys.WeeklyReportsKeys.KEY_REPORT)) {
                        JSONObject weeklyReport = arrayWeeklyReports.getJSONObject(i).getJSONObject(Keys.WeeklyReportsKeys.KEY_REPORT);
                        WeeklyReportsData report = new WeeklyReportsData(Integer.parseInt(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_WWR_ID)),
                                Integer.parseInt(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_OJT_ID)),
                                Integer.parseInt(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_INFO_ID)),
                                Integer.parseInt(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_WEEK_NO)),
                                format.parse(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_START_DATE)),
                                format.parse(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_END_DATE)),
                                Html.fromHtml(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_ACCOMPLISHMENTS)).toString(),
                                Html.fromHtml(weeklyReport.getString(Keys.WeeklyReportsKeys.KEY_DIFFICULTIES)).toString());
//                            Toast.makeText(getActivity(), report.toString(), Toast.LENGTH_LONG).show();
                        data.add(report);
//                            Toast.makeText(getActivity(), Html.fromHtml(weeklyReport.getString(Constants.KEY_ACCOMPLISHMENTS)).toString(), Toast.LENGTH_LONG).show();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public void onRefresh() {
        sendWeeklyReportRequest();
    }
}
