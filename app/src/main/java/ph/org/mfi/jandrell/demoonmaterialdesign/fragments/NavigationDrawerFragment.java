package ph.org.mfi.jandrell.demoonmaterialdesign.fragments;


import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.adapters.NavListAdapter;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.NavList;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.LoginActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.MainActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.SettingsActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.SlidingTabLayout;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {


    private FragmentTransaction fragmentTransaction;

    private RecyclerView recyclerView;
    private static String PREF_FILE_NAME = "samplepref";
    private static String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private NavListAdapter navListAdapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSaveInstanceState;

    private Toolbar mToolbar;
    private SlidingTabLayout tabs;
    private ViewPager vPager;

    private View contentView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSaveInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView settingsRecycler = new RecyclerView(getActivity());
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ArrayList<String> title = new ArrayList<>();
        title.add("Home");
        title.add("Handbook");
        title.add("Settings");
        title.add("Log out");
        int[] icons = {R.drawable.ic_action_action_home, R.drawable.ic_action_action_book,
                R.drawable.ic_action_settings, R.drawable.ic_action_action_book};
        this.recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        navListAdapter = new NavListAdapter(getActivity(), getData(title, icons));
        this.recyclerView.setAdapter(navListAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), this.recyclerView, new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Log.d("JD", "onCLick" + position);
                switch (position) {
                    case 0:
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        NewsFeedFragment nFragment = new NewsFeedFragment();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Fade fade = new Fade();
                            fade.setDuration(1000);
                            nFragment.setEnterTransition(fade);
                        }
                        fragmentTransaction.replace(R.id.fragment_container, nFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        mToolbar.setTitle("MFI");
                        mDrawerLayout.closeDrawer(contentView);
                        break;
                    case 1:
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new HandbookFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        mToolbar.setTitle("Handbook");
                        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
                        mDrawerLayout.closeDrawer(contentView);
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), SettingsActivity.class));
                        break;
                    case 3:
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor edit = sh.edit();
                        edit.putString(MainActivity.KEY_USERNAME, null);
                        edit.putString(MainActivity.KEY_PASSWORD, null);
                        edit.apply();
                        saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d("JD", "onLongCLick" + position);
            }
        }));
//        title = new ArrayList<>();
//        title.add("Settings");
//        title.add("Logout");
//        InfoAdapter settingsInfo = new InfoAdapter(getActivity(), getData(title, icons));
//        settingsRecycler.setAdapter(settingsInfo);
//        settingsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        settingsRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), settingsRecycler, new ItemClickListener(){
//
//            @Override
//            public void onClick(View view, int position) {
//                Log.d("JD", "onCLick" + position);
//                switch (position) {
//                    case 1:
//                        startActivity(new Intent(getActivity(), HandbookActivity.class));
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                Log.d("JD", "onLongCLick" + position);
//            }
//        }));

        return layout;
    }

    public static List<NavList> getData(ArrayList<String> title, int[] icons) {
        List<NavList> data = new ArrayList<>();

        for (int i = 0; i < title.size() && i < icons.length; i++) {
            NavList info = new NavList();
            info.setTitle(title.get(i));
            info.setIconId(icons[i]);
            data.add(info);
        }

        return data;

    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        contentView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }
        };
        if (!mUserLearnedDrawer && !mFromSaveInstanceState) {
            mDrawerLayout.openDrawer(contentView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }

    /*
    * Class for RecyclerView.OnItemTouchListener
    * */

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ItemClickListener itemClickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && itemClickListener != null) {
                        itemClickListener.onLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View childView = rv.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && this.itemClickListener != null && gestureDetector.onTouchEvent(e)) {
                this.itemClickListener.onClick(childView, rv.getChildPosition(childView));
            }

            Log.d("JD", "onInterceptTouchEvent MotionEvent: " + e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("JD", "onTouchEvent MotionEvent: " + e);
        }
    }

    public static interface ItemClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public class ContentAdapter extends FragmentStatePagerAdapter {

        Fragment fragment;
        String pageTitle;

        public ContentAdapter(FragmentManager fm, String pageTitle, Fragment f) {
            super(fm);
            this.pageTitle = pageTitle;
            fragment = f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

}
