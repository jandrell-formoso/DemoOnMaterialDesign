package ph.org.mfi.jandrell.demoonmaterialdesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.DTRData;

/**
 * Created by ITD11 on 3/31/2015.
 */
public class DTRAdapter extends RecyclerView.Adapter<DTRAdapter.DTRViewHolder> {
    LayoutInflater inflater;
    ArrayList<DTRData> dtrs;
    SimpleDateFormat dateFormat;
    Context context;

    public DTRAdapter(Context context, ArrayList<DTRData> dtrs) {
        this.inflater = LayoutInflater.from(context);
        this.dtrs = dtrs;
        this.context = context;
    }

    @Override
    public DTRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_view_dtr, parent, false);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DTRViewHolder viewHolder = new DTRViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DTRViewHolder holder, int position) {
        holder.date.setText(dateFormat.format(dtrs.get(position).getDate()));
        holder.in.setText(DateFormat.getTimeInstance().format(dtrs.get(position).getIn()));
        holder.out.setText(DateFormat.getTimeInstance().format(dtrs.get(position).getOut()));
    }

    @Override
    public int getItemCount() {
        return dtrs.size();
    }

    static class DTRViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView in;
        TextView out;

        public DTRViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.dtr_date);
            in = (TextView) itemView.findViewById(R.id.dtr_time_in);
            out = (TextView) itemView.findViewById(R.id.dtr_time_out);
        }
    }
}
