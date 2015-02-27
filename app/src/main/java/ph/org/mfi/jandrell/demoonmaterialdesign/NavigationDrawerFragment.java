package ph.org.mfi.jandrell.demoonmaterialdesign;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {



    private RecyclerView recyclerView;
    private static String PREF_FILE_NAME="samplepref";
    private static String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private InfoAdapter infoAdapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSaveInstanceState;

    private Toolbar mToolbar;

    private View contentView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState != null) {
            mFromSaveInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        this.recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        infoAdapter = new InfoAdapter(getActivity(), getData());
        this.recyclerView.setAdapter(infoAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), this.recyclerView, new ItemClickListener(){

            @Override
            public void onClick(View view, int position) {
                Log.d("JD", "onCLick" + position);
                switch (position) {
                    case 1:
                        startActivity(new Intent(getActivity(), HandbookActivity.class));
                        break;

                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d("JD", "onLongCLick" + position);
            }
        }));
        return layout;
    }

    public static List<NavList> getData() {
        List<NavList> data = new ArrayList<>();
        String[] title = {
                "Home", "Handbook"
        };
        int[] icons = {
                R.drawable.ic_action_action_home,
                R.drawable.ic_action_action_book
        };

        for(int i = 0; i<title.length && i<icons.length ; i++) {
            NavList info = new NavList();
            info.setTitle(title[i]);
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
                if(!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSaveInstanceState) {
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
                    if(childView!=null && itemClickListener!=null) {
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

            if(childView!=null && this.itemClickListener!=null && gestureDetector.onTouchEvent(e)) {
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
}
