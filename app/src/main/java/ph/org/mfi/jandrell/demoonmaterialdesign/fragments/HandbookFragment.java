package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.CardViewAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout =  inflater.inflate(R.layout.fragment_handbook, container, false);
        mPager = (ViewPager) layout.findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) layout.findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
        mTabs.setDistributeEvenly(false);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(android.R.color.white);
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
            MyFragment myFragment = MyFragment.getInstanceOf(position);
            return myFragment;
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
        private static int position;
        RecyclerView cardRecyclerView;
        public static MyFragment getInstanceOf(int position) {
            MyFragment myFragment = new MyFragment();
            MyFragment.position = position;
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_my, container, false);
            cardRecyclerView = (RecyclerView) layout.findViewById(R.id.card_view_my);
            cardRecyclerView.setAdapter(new CardViewAdapter(null, getActivity(), position));
            cardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            return layout;
        }
    }

    // WEEKLY REPORTS
    public class GetWeeklyReports extends AsyncTask<Void, Void, Void> {
        URL url;
        URLConnection urlConnection;



        protected void onPostExecute(Void aVoid) {

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                url = new URL("http://mfiapp.site88.net/mfi_app/RestfulService/");

                urlConnection = url.openConnection();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
