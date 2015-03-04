package ph.org.mfi.jandrell.demoonmaterialdesign;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;

public class NewsActivity extends ActionBarActivity {

    private static final String EXTRA_TITLE = "";

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        this.toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(this.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);

        TextView newsTitle = (TextView) findViewById(R.id.main_news_title);
        TextView newsContent = (TextView) findViewById(R.id.main_news_content);

        Intent intent = getIntent();

        newsTitle.setText(intent.getStringExtra(NewsFeedFragment.TAG_NEWS_TITLE));
        newsContent.setText(intent.getStringExtra(NewsFeedFragment.TAG_NEWS_CONTENT));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
