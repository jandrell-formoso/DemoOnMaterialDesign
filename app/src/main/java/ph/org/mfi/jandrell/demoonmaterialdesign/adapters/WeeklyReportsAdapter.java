package ph.org.mfi.jandrell.demoonmaterialdesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.WeeklyReportsData;

/**
 * Created by ITD11 on 3/27/2015.
 */
public class WeeklyReportsAdapter extends RecyclerView.Adapter<WeeklyReportsAdapter.ViewHolder> {

    private static final String TEXT_DATE_STARTED = "Date Started";
    private static final String TEXT_DATE_ENDED = "Date Ended";

    private ArrayList<WeeklyReportsData> reports;
    private LayoutInflater inflater;
    private Context context;
    private SimpleDateFormat dateFormat;

    public WeeklyReportsAdapter(ArrayList<WeeklyReportsData> reports, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.reports = reports;
        this.context = context;
    }

    @Override
    public WeeklyReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.weekly_reports, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeeklyReportsAdapter.ViewHolder holder, int position) {
        WeeklyReportsData report = reports.get(position);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        holder.weekNo.setText(report.getWeekNo() + "");
        holder.startDate.setText(TEXT_DATE_STARTED + ": " + dateFormat.format(report.getStartDate()));
        holder.endDate.setText(TEXT_DATE_ENDED + ": " + dateFormat.format(report.getEndDate()));
    }



    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weekNo;
        private TextView startDate;
        private TextView endDate;

        public ViewHolder(View itemView) {
            super(itemView);
            weekNo = (TextView) itemView.findViewById(R.id.week_no);
            startDate = (TextView) itemView.findViewById(R.id.start_date);
            endDate = (TextView) itemView.findViewById(R.id.end_date);
        }
    }
}
