package ph.org.mfi.jandrell.demoonmaterialdesign;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jandrell on 2/11/2015.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<Information> data = Collections.emptyList();

    public InfoAdapter(Context context, List<Information> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.inflater.inflate(R.layout.custom_row, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        Information currentObject = this.data.get(i);
        viewHolder.title.setText(currentObject.getTitle());
        viewHolder.imageView.setImageResource(currentObject.getIconId());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.listText);
            this.imageView = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
