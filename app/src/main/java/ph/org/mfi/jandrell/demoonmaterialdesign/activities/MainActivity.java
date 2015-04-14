package ph.org.mfi.jandrell.demoonmaterialdesign.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ph.org.mfi.jandrell.demoonmaterialdesign.fragments.NavigationDrawerFragment;
import ph.org.mfi.jandrell.demoonmaterialdesign.fragments.NewsFeedFragment;
import ph.org.mfi.jandrell.demoonmaterialdesign.R;


public class MainActivity extends ActionBarActivity {

    public static final String KEY_USERNAME = "";
    public static final String KEY_PASSWORD = "";

    private FragmentTransaction fragmentTransaction;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new NewsFeedFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentTransaction.commit();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);
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
        } else if(id == R.id.action_notification) {
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

}
