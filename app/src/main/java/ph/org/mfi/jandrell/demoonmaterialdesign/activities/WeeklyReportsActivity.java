package ph.org.mfi.jandrell.demoonmaterialdesign.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;

public class WeeklyReportsActivity extends ActionBarActivity {

    public static final String EXTRA_ACCOMPLISHMENTS = "accomplishment";
    public static final String EXTRA_DIFFICULTIES = "difficulties";
    public static final String EXTRA_WEEK_NO = "week_no";
    public static final String EXTRA_START_DATE = "start_date";
    public static final String EXTRA_END_DATE = "end_date";

    ActionBar actionBar;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_reports);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        TextView weekNo = (TextView) findViewById(R.id.title_week_no);
        TextView accomplishments = (TextView) findViewById(R.id.body_accomplishments);
        TextView difficulties = (TextView) findViewById(R.id.body_difficulties);

        weekNo.setText("Week No: " + getIntent().getStringExtra(EXTRA_WEEK_NO));
        accomplishments.setText(getIntent().getStringExtra(EXTRA_ACCOMPLISHMENTS));
        difficulties.setText(getIntent().getStringExtra(EXTRA_DIFFICULTIES));
    }

}
