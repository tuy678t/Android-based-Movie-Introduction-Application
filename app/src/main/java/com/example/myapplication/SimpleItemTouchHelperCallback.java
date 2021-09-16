package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final WatchListAdapter mAdapter;
    private int dragFrom=-1;
    private int dragTo=-1;

    public SimpleItemTouchHelperCallback(WatchListAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.notifyDataSetChanged();
        //Log.d("clearViewFrom",String.valueOf(dragFrom));
        //Log.d("clearViewTo",String.valueOf(dragTo));
        //if(dragFrom!=-1&&dragTo!=-1&&dragFrom!=dragTo){
        //    mAdapter.onItemMove(dragFrom,dragTo);
        //}
        //dragFrom=-1;
        //dragTo=-1;
        //Log.d("onSelectedChangedFrom",String.valueOf(dragFrom));
        //Log.d("onSelectedChangedTo",String.valueOf(dragTo));
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        //if(dragFrom==-1) dragFrom=viewHolder.getAdapterPosition();
        //dragTo=target.getAdapterPosition();
        //Log.d("onMoveFrom",String.valueOf(dragFrom));
        //Log.d("onMoveTo",String.valueOf(dragTo));
        try {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }



}