package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.constants.Constants;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.WeeklyReportsData;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandbookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandbookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Toolbar mToolbar;
    private SlidingTabLayout mTabs;
    private ViewPager mPager;
    private static FragmentTransaction transaction;
    private static ArrayList<WeeklyReportsData> weeklyReports;
    private static int position;

    RecyclerView recyclerView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandbookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandbookFragment newInstance(String param1, String param2) {
        HandbookFragment fragment = new HandbookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setUp(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }

    public HandbookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_handbook, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setElevation(0);
        mTabs = (SlidingTabLayout) layout.findViewById(R.id.tabs);
        mPager = (ViewPager) layout.findViewById(R.id.pager);
        mPager.setCurrentItem(position);
        mPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        mTabs.setViewPager(mPager);
        mTabs.setDistributeEvenly(false);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(android.R.color.white);
            }
        });
        mTabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("JD", "onPageSelected position: " + position);
                MyFragment.position = position;
                MyFragment myFragment = MyFragment.getInstanceOf(MyFragment.position);
            }
        });

//        mTabs.setSelectedIndicatorColors(getResources().getColor(android.R.color.white));

        return layout;
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("JD", "getItem position: " + position);
            Fragment fragment = null;
            switch(position) {
                case Constants.TAB_SUM_OF_ALT:
                    fragment = MyFragment.getInstanceOf(position);
                    break;
                case Constants.TAB_DTR:
                    fragment = MyFragment.getInstanceOf(position);
                    break;
                case Constants.TAB_WEEKLY:
                    fragment = new WeeklyReportsFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    public static class MyFragment extends Fragment {
        private static int position = 0;
        RecyclerView cardRecyclerView;

        public static MyFragment getInstanceOf(int position) {
            Log.d("JD", "getInstanceOf position: " + position);
            MyFragment myFragment = new MyFragment();
            return myFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_weekly_reports, container, false);
            cardRecyclerView = (RecyclerView) layout.findViewById(R.id.card_view_my);
            cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//            sendWeeklyReportRequest();
//            cardRecyclerView.setAdapter(weeklyReportsAdapter);
//            cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            return layout;
        }
//
//        public void sendWeeklyReportRequest(View view) {
//            String url = Constants.KEY_URL_MFI + "?" + Constants.KEY_URL_USER_ID + "=82&" + Constants.KEY_URL_WEEKLY_REPORT + "=1";
//            weeklyReports = new ArrayList<>();
////            HashMap<String, String> params = new HashMap<>();
////            params.put(Constants.KEY_URL_USER_ID, "82");
////            params.put(Constants.KEY_URL_WEEKLY_REPORT, "1");
//            RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    weeklyReports = parseWeeklyJSON(response);
//                    weeklyReportsAdapter = new WeeklyReportsAdapter(weeklyReports, getActivity());
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    String errorMessage = validateError(error);
//                    CardView cardView = (CardView) getActivity().findViewById(R.id.handbook_card_error);
//                    cardView.setVisibility(View.VISIBLE);
//                    Log.d("JD", errorMessage);
//                }
//            });
//            requestQueue.add(jsonRequest);
//        }
//
//        public String validateError(VolleyError error) {
//
//            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                return "No connection. Swipe down to refresh.";
//            } else if (error instanceof AuthFailureError) {
//                return "Authentication Failure.";
//            } else if (error instanceof ServerError) {
//                return "Server Error. Swipe down to refresh.";
//            } else if (error instanceof ParseError) {
//                return "Parsing the data error" + error.getMessage();
//            } else if (error instanceof NetworkError) {
//                return "Network error. Swipe down to refresh.";
//            } else {
//                return "";
//            }
//        }
//
//        public ArrayList<WeeklyReportsData> parseWeeklyJSON(JSONObject response) {
//            ArrayList<WeeklyReportsData> data = new ArrayList<>();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//            if (response.has(Constants.KEY_URL_WEEKLY_REPORT)) {
//                try {
//                    JSONArray arrayWeeklyReports = response.getJSONArray(Constants.KEY_URL_WEEKLY_REPORT);
//                    for (int i = 0; i < arrayWeeklyReports.length(); i++) {
//                        if (arrayWeeklyReports.getJSONObject(i).has(Constants.KEY_REPORT)) {
//                            JSONObject weeklyReport = arrayWeeklyReports.getJSONObject(i).getJSONObject(Constants.KEY_REPORT);
//                            WeeklyReportsData report = new WeeklyReportsData(Integer.parseInt(weeklyReport.getString(Constants.KEY_WWR_ID)),
//                                    Integer.parseInt(weeklyReport.getString(Constants.KEY_OJT_ID)),
//                                    Integer.parseInt(weeklyReport.getString(Constants.KEY_INFO_ID)),
//                                    Integer.parseInt(weeklyReport.getString(Constants.KEY_WEEK_NO)),
//                                    format.parse(weeklyReport.getString(Constants.KEY_START_DATE)),
//                                    format.parse(weeklyReport.getString(Constants.KEY_END_DATE)),
//                                    Html.fromHtml(weeklyReport.getString(Constants.KEY_ACCOMPLISHMENTS)).toString(),
//                                    Html.fromHtml(weeklyReport.getString(Constants.KEY_DIFFICULTIES)).toString());
////                            Toast.makeText(getActivity(), report.toString(), Toast.LENGTH_LONG).show();
//                            data.add(report);
////                            Toast.makeText(getActivity(), Html.fromHtml(weeklyReport.getString(Constants.KEY_ACCOMPLISHMENTS)).toString(), Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            return data;
//        }



        public static interface OnItemTouchListener {
            public void onClick(View view, int position);
            public void onLongClick(View view, int position);
        }
    }


}
