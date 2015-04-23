package ph.org.mfi.jandrell.demoonmaterialdesign.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.activities.MainActivity;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.NavList;

/**
 * Created by Jandrell on 2/11/2015.
 */
public class NavListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<NavList> data = Collections.emptyList();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private NavList selectedItem;

    public NavListAdapter(Context context, List<NavList> data, int selectedItemIndex) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.selectedItem = data.get(selectedItemIndex);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = this.inflater.inflate(R.layout.drawer_header, viewGroup, false);
            HeaderViewHolder viewHolder = new HeaderViewHolder(view);
            return viewHolder;
        } else {
            View view = this.inflater.inflate(R.layout.drawer_item_row, viewGroup, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof HeaderViewHolder) {

        } else {
            NavList currentObject = this.data.get(position - 1);
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            if(currentObject == selectedItem) {
                itemViewHolder.parentView.setSelected(true);
                itemViewHolder.parentView.setEnabled(false);
            }
            itemViewHolder.title.setText(currentObject.getTitle());
            itemViewHolder.title.setTextColor(context.getResources().getColorStateList(R.color.tab_selector));
            itemViewHolder.imageView.setImageResource(currentObject.getIconId());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return this.data.size() + 1;
    }

    public NavList getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(NavList selectedItem) {
        this.selectedItem = selectedItem;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageView;
        View parentView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            parentView = itemView;
            this.title = (TextView) itemView.findViewById(R.id.listText);
            this.imageView = (ImageView) itemView.findViewById(R.id.listIcon);
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        public HeaderViewHolder(View itemView) {
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.txt_user);
            user.setText(PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.KEY_USERNAME, ""));
        }

    }

}
