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
    private List<NavList> data = Collections.emptyList();
    private ItemClickListener itemClickListener;
    private Context context;

    public InfoAdapter(Context context, List<NavList> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.inflater.inflate(R.layout.custom_row, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        NavList currentObject = this.data.get(i);
        viewHolder.title.setText(currentObject.getTitle());
        viewHolder.imageView.setImageResource(currentObject.getIconId());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.title = (TextView) itemView.findViewById(R.id.listText);
            this.imageView = (ImageView) itemView.findViewById(R.id.listIcon);
        }

        @Override
        public void onClick(View v) {
//            context.startActivity(new Intent(context, HandbookActivity.class));
            if(itemClickListener != null) {
                itemClickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ItemClickListener {
        public void itemClicked(View view, int position);
    }
}
