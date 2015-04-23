package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.NewsFeedAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.NewsFeedInfo;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.NewsActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.json.Keys;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.network.VolleySingleton;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.MultiSwipeRefreshLayout;

/**
 * Created by Jandrell on 2/14/2015.
 */
public class NewsFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String URL = "http://mfiapp.site88.net/mfi_app/RestfulService/";

    private final static String KEY_HAS_NEWS = "refresh_news";
    private final static String KEY_LIST_NEWS = "newsList";
    private final static String FILENAME = "pref_file.dat";
    private static final String STATE_NEWS = "state_news";

    private Bundle savedInstance;


    private List<NewsFeedInfo> newsList;

    private RecyclerView recyclerView;
    private NewsFeedAdapter newsFeedAdapter;

    private MultiSwipeRefreshLayout refreshNews;

    private ProgressDialog pDialog;

    private static final String TAG_TITLE = "TITLE";
    private static final String TAG_SUBTITLE = "SUBTITLE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(refreshNews.isRefreshing()) {
            refreshNews.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_NEWS, (ArrayList<NewsFeedInfo>) newsList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.news_feed_view);
        newsList = new ArrayList<>();

        refreshNews = (MultiSwipeRefreshLayout) view.findViewById(R.id.refresh_news);
        refreshNews.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        refreshNews.setSwipeableChildren(R.id.news_feed_view);
        refreshNews.setOnRefreshListener(this);
        newsFeedAdapter = new NewsFeedAdapter(getActivity());
        if (savedInstanceState != null) {
            Log.d("JD", "Saved news feed");
            recyclerView = new RecyclerView(getActivity());
            newsList = savedInstanceState.getParcelableArrayList(STATE_NEWS);
            newsFeedAdapter.setNewsFeedInfos((ArrayList<NewsFeedInfo>) newsList);
            recyclerView.setAdapter(newsFeedAdapter);
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    refreshNews.setRefreshing(true);
                    sendNewsRequest();
                }
            });

        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addOnItemTouchListener(new NewsFeedTouchListener(getActivity(), this.recyclerView, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                TextView title = (TextView) view.findViewById(R.id.news_title);
                TextView content = (TextView) view.findViewById(R.id.news_content);

                intent.putExtra(Keys.NewsFeedKeys.KEY_NEWS_TITLE, title.getText().toString());
                intent.putExtra(Keys.NewsFeedKeys.KEY_NEWS_CONTENT, content.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.getChildLayoutPosition(recyclerView.findChildViewUnder(dx, dy)) == -1) {
                    actionBar.setElevation(0);
                } else {
                    actionBar.setElevation(10);
                }
            }
        });

        view.setAnimation(new AlphaAnimation(0, 1));
//        Toast.makeText(getActivity(), recyclerView.getParent().toString(), Toast.LENGTH_LONG).show();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        refreshNews.setRefreshing(true);
//        refreshNews.setOnRefreshListener(this);
//        refreshNews.setColorSchemeColors(android.R.color.black);
    }

    @Override
    public void onRefresh() {
        sendNewsRequest();
    }


    public static void saveToPreferences(Context context, String key, boolean value) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sh.edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public static boolean readFromPreferences(Context context, String key, boolean defaultValue) {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        return sh.getBoolean(key, defaultValue);
    }

    //
//    public static List<NewsFeedInfo> getNewsFeed() {
//        List<NewsFeedInfo> newsFeedInfoList = new ArrayList<>();
//        for(int x=0;x<100;x++) {
//            NewsFeedInfo newsFeedInfo = new NewsFeedInfo();
//            newsFeedInfo.setmTitle("Title " + (x+1));
//            newsFeedInfo.setmBody("Subtitle " + (x + 1));
//            newsFeedInfoList.add(newsFeedInfo);
//        }
//        return newsFeedInfoList;
//    }

    public void sendNewsRequest() {
        String url = "";
        url = NewsFeedFragment.URL + "?news=1";
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                newsList = parseNewsJSON(response);
                newsFeedAdapter.setNewsFeedInfos((ArrayList<NewsFeedInfo>) newsList);
                recyclerView.setAdapter(newsFeedAdapter);
                if (refreshNews.isRefreshing()) {
                    refreshNews.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                newsList = new ArrayList<>();
                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    NewsFeedInfo noNews = new NewsFeedInfo(null, "No Data Retrieved", "Swipe up to refresh", null, null);
                    newsList.add(noNews);
                    toast = Toast.makeText(getActivity(), "No Connection.", Toast.LENGTH_LONG);

                } else if (error instanceof AuthFailureError) {
                    //TODO - Authentication Failure
                } else if (error instanceof ServerError) {
                    //TODO - Server Error
                } else if (error instanceof NetworkError) {
                    NewsFeedInfo noNews = new NewsFeedInfo(null, "Network Error", "Swipe up to refresh", null, null);
                    newsList.add(noNews);
                    toast = Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_LONG);
                } else if (error instanceof ParseError) {
                    //TODO - Parse Error
                    toast = Toast.makeText(getActivity(), "Parse error.", Toast.LENGTH_LONG);
                }
                toast.show();
                if (refreshNews.isRefreshing()) {
                    refreshNews.setRefreshing(false);
                }
//                Toast.makeText(MFIApplication.getAppContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }

    public List<NewsFeedInfo> parseNewsJSON(JSONObject response) {
        if (response.has(Keys.NewsFeedKeys.KEY_ANNOUNCEMENTS)) {
            JSONArray jsonArray = null;
            try {
                newsList = new ArrayList<>();
                jsonArray = response.getJSONArray(Keys.NewsFeedKeys.KEY_ANNOUNCEMENTS);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i).getJSONObject(Keys.NewsFeedKeys.KEY_NEWS);
                    NewsFeedInfo newsFeedInfo = new NewsFeedInfo(jObj.getString(Keys.NewsFeedKeys.KEY_NEWS_ID),
                            jObj.getString(Keys.NewsFeedKeys.KEY_NEWS_TITLE),
                            Html.fromHtml(jObj.getString(Keys.NewsFeedKeys.KEY_NEWS_CONTENT)).toString(),
                            jObj.getString(Keys.NewsFeedKeys.KEY_NEWS_PUBLISH_DATE),
                            jObj.getString(Keys.NewsFeedKeys.KEY_POSTER_ID));

                    newsList.add(newsFeedInfo);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newsList;
    }

    class NewsFeedTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ItemClickListener itemClickListener;

        public NewsFeedTouchListener(Context context, final RecyclerView recyclerView, final ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && itemClickListener != null) {
                        itemClickListener.onLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
//
//                @Override
//                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                    Log.d("JD", " > onScroll : " + distanceX + " = x AND " + distanceY + " = y");
//                    return super.onScroll(e1, e2, distanceX, distanceY);
//                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && this.itemClickListener != null && this.gestureDetector.onTouchEvent(e)) {
                this.itemClickListener.onClick(childView, rv.getChildLayoutPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }

    public static interface ItemClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);

    }

//    class GetNews extends AsyncTask<Void, Void, Void> {
//
//        private List<NameValuePair> params;
//        private boolean success = false;
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            refreshNews.setRefreshing(true);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (!(isInternetAvailable() || isNetworkAvailable())) {
//                newsList = new ArrayList<>();
//                newsList.add(new NewsFeedInfo(null, "No connection", "retry?", null, null));
//                recyclerView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            }
//            newsFeedAdapter = new NewsFeedAdapter(getActivity(), newsList);
//            recyclerView.setAdapter(newsFeedAdapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            refreshNews.setRefreshing(false);
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                ServiceHandler sh = new ServiceHandler();
//                this.params = new ArrayList<NameValuePair>();
//                this.params.add(new BasicNameValuePair("news", "1"));
//                newsList = new ArrayList<NewsFeedInfo>();
//
//                String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET, this.params);
//
//                if (jsonStr != null) {
//                    JSONObject jsonObject = new JSONObject(jsonStr);
//
//                    if (jsonObject.getString(KEY_ANNOUNCEMENTS).equals("null")) {
//                        return null;
//                    } else {
//                        JSONArray jsonArray = jsonObject.getJSONArray(KEY_ANNOUNCEMENTS);
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jObj = jsonArray.getJSONObject(i).getJSONObject(KEY_NEWS);
////                                HashMap<String, String> news = new HashMap<String, String>();
//                            NewsFeedInfo newsFeedInfo = new NewsFeedInfo(jObj.getString(KEY_NEWS_ID),
//                                    jObj.getString(KEY_NEWS_TITLE),
//                                    Html.fromHtml(jObj.getString(KEY_NEWS_CONTENT)).toString(),
//                                    jObj.getString(KEY_NEWS_PUBLISH_DATE),
//                                    jObj.getString(KEY_POSTER_ID));
//
//                            newsList.add(newsFeedInfo);
//                        }
//                        success = true;
//                    }
//                }
//
//
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//            return null;
//        }
//    }


    public boolean isNetworkAvailable() {
        ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
