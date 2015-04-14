package ph.org.mfi.jandrell.demoonmaterialdesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.NewsFeedInfo;

/**
 * Created by Jandrell on 2/14/2015.
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>{

    private LayoutInflater inflater;
    private List<NewsFeedInfo> newsFeedInfos = Collections.emptyList();
    private Context context;

    public NewsFeedAdapter(Context context, List<NewsFeedInfo> data) {
        this.inflater = LayoutInflater.from(context);
        this.newsFeedInfos = data;
        this.context = context;
    }

    public NewsFeedAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setNewsFeedInfos(ArrayList<NewsFeedInfo> newsFeedList) {
        this.newsFeedInfos = newsFeedList;
        notifyItemRangeChanged(0, newsFeedList.size());
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.news_feed, parent, false);
        NewsFeedViewHolder newsFeedViewHolder = new NewsFeedViewHolder(view);
        return newsFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsFeedViewHolder holder, int position) {
        NewsFeedInfo newsFeedInfo = this.newsFeedInfos.get(position);
        holder.title.setText(newsFeedInfo.getmTitle());
        holder.content.setText(newsFeedInfo.getmBody());
    }

    @Override
    public int getItemCount() {
        return this.newsFeedInfos.size();
    }

    class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.news_title);
            this.content = (TextView) itemView.findViewById(R.id.news_content);
        }
    }
}
