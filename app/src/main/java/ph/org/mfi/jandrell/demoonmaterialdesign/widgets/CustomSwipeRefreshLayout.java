package ph.org.mfi.jandrell.demoonmaterialdesign.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Jandrell on 3/4/2015.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    RecyclerView recyclerView;
    public boolean canScrollChild;

    public CustomSwipeRefreshLayout(Context context, RecyclerView view) {
        super(context);
        recyclerView = view;
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dx == 0) {
                    canScrollChild = false;
                } else {
                    canScrollChild = true;
                }
            }
        });
        return canScrollChild;
    }



}
