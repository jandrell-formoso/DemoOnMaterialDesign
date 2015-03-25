package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.NewsFeedAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.NewsFeedInfo;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.NewsActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.services.ServiceHandler;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.MultiSwipeRefreshLayout;

/**
 * Created by Jandrell on 2/14/2015.
 */
public class NewsFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String URL = "http://mfiapp.site88.net/mfi_app/RestfulService/";
    private final static String TAG_ANNOUNCEMENTS = "announcements";
    private final static String TAG_NEWS = "news";
    private final static String TAG_NEWS_ID = "news_id";
    public final static String TAG_NEWS_TITLE = "title";
    public final static String TAG_NEWS_CONTENT = "content";
    private final static String TAG_NEWS_PUBLISH_DATE = "publish_date";
    private final static String TAG_POSTER_ID = "poster_id";
    private final static String KEY_HAS_NEWS = "refresh_news";
    private final static String KEY_LIST_NEWS = "newsList";
    private final static String FILENAME = "pref_file.dat";

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

        new GetNews().execute();
        newsFeedAdapter = new NewsFeedAdapter(getActivity(), newsList);
        recyclerView.setAdapter(newsFeedAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addOnItemTouchListener(new NewsFeedTouchListener(getActivity(), this.recyclerView, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                TextView title = (TextView) view.findViewById(R.id.news_title);
                TextView content = (TextView) view.findViewById(R.id.news_content);

                intent.putExtra(TAG_NEWS_TITLE, title.getText().toString());
                intent.putExtra(TAG_NEWS_CONTENT, content.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
        new GetNews().execute();
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
                this.itemClickListener.onClick(childView, rv.getChildPosition(childView));
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

    class GetNews extends AsyncTask<Void, Void, Void> {

        private List<NameValuePair> params;
        private boolean success = false;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            refreshNews.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!(isInternetAvailable() || isNetworkAvailable())) {
                newsList = new ArrayList<>();
                newsList.add(new NewsFeedInfo(null, "No connection", "retry?", null, null));
                recyclerView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
            newsFeedAdapter = new NewsFeedAdapter(getActivity(), newsList);
            recyclerView.setAdapter(newsFeedAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            refreshNews.setRefreshing(false);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ServiceHandler sh = new ServiceHandler();
                this.params = new ArrayList<NameValuePair>();
                this.params.add(new BasicNameValuePair("news", "1"));
                newsList = new ArrayList<NewsFeedInfo>();

                String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET, this.params);

                if (jsonStr != null) {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.getString(TAG_ANNOUNCEMENTS).equals("null")) {
                        return null;
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_ANNOUNCEMENTS);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i).getJSONObject(TAG_NEWS);
//                                HashMap<String, String> news = new HashMap<String, String>();
                            NewsFeedInfo newsFeedInfo = new NewsFeedInfo(jObj.getString(TAG_NEWS_ID),
                                    jObj.getString(TAG_NEWS_TITLE),
                                    Html.fromHtml(jObj.getString(TAG_NEWS_CONTENT)).toString(),
                                    jObj.getString(TAG_NEWS_PUBLISH_DATE),
                                    jObj.getString(TAG_POSTER_ID));

                            newsList.add(newsFeedInfo);
                        }
                        success = true;
                    }
                }


            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }


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
