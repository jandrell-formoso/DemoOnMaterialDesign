package ph.org.mfi.jandrell.demoonmaterialdesign.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import ph.org.mfi.jandrell.demoonmaterialdesign.fragments.HandbookFragment;
import ph.org.mfi.jandrell.demoonmaterialdesign.fragments.NavigationDrawerFragment;
import ph.org.mfi.jandrell.demoonmaterialdesign.fragments.NewsFeedFragment;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.widgets.SlidingTabLayout;


public class MainActivity extends ActionBarActivity {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    private FragmentTransaction fragmentTransaction;

    private static String PREF_FILE_NAME = "samplepref";

    private Drawer.Result mDrawerResult;
    private AccountHeader.Result accountHeader;

    private Toolbar mToolbar;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new NewsFeedFragment());
            fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
            fragmentTransaction.commit();
        }
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
//        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, mToolbar, 0);
        accountHeader = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.color.colorAccent)
                .withTranslucentStatusBar(false)
                .addProfiles(new ProfileDrawerItem().withName("Jandrell Ian L. Formoso").withIcon(getResources().getDrawable(R.mipmap.ic_launcher)))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        mDrawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withTranslucentStatusBar(false)
                .withAccountHeader(accountHeader)
                .addDrawerItems(new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_home).withName(R.string.drawer_name_home),
                        new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_book).withName(R.string.drawer_name_handbook),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_name_logout))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id, IDrawerItem iDrawerItem) {
                        switch(position) {
                            case 0:
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                NewsFeedFragment nFragment = new NewsFeedFragment();
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Fade fade = new Fade();
                                    fade.setDuration(1000);
                                    nFragment.setEnterTransition(fade);
                                }
                                fragmentTransaction.replace(R.id.fragment_container, nFragment);
                                fragmentTransaction.commit();
                                mToolbar.setTitle("MFI");
                                break;
                            case 1:
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, HandbookFragment.newInstance("", ""));
                                fragmentTransaction.commit();
                                mToolbar.setTitle("Handbook");
                                break;
                            case 3:
                                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor edit = sh.edit();
                                edit.putString(MainActivity.KEY_USERNAME, null);
                                edit.putString(MainActivity.KEY_PASSWORD, null);
                                edit.apply();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }

                    }
                })
                .build();
        mDrawerResult.setSelection(0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_notification) {
            int notificationId = 001;
            NotificationCompat.Builder notifBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("My Notification")
                            .setContentText("This is my new notification.")
                            .setDefaults(Notification.DEFAULT_ALL);
            NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notifManager.notify(notificationId, notifBuilder.build());
        }

        return super.onOptionsItemSelected(item);
    }
//    public class ContentAdapter extends FragmentStatePagerAdapter {
//
//        String[] pageTitle;
//
//        public ContentAdapter(FragmentManager fm, String[] pageTitle) {
//            super(fm);
//            this.pageTitle = pageTitle;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return pageTitle[position];
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return this.pageTitle.length;
//        }
//    }

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

}
