package ph.org.mfi.jandrell.demoonmaterialdesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ph.org.mfi.jandrell.demoonmaterialdesign.R;
import ph.org.mfi.jandrell.demoonmaterialdesign.data.CardViewInfo;

/**
 * Created by ITD11 on 3/23/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    List<CardViewInfo> cardViewInfoList = Collections.emptyList();
    LayoutInflater inflater;
    Context context;
    int position;

    public CardViewAdapter(List<CardViewInfo> infoList, Context context, int position) {
        this.cardViewInfoList = infoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.position = position;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.fragment_handbook_card_view, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.cardViewInfoList.size();
    }
    class CardViewHolder extends RecyclerView.ViewHolder {

        public CardViewHolder(View itemView) {
            super(itemView);
        }



    }
}
