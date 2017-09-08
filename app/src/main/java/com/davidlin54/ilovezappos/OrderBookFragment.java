package com.davidlin54.ilovezappos;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class OrderBookFragment extends Fragment {
    private RecyclerView mBidsRecyclerView;
    private RecyclerView mAsksRecyclerView;
    private OrderBookAdapter mBidsAdapter;
    private OrderBookAdapter mAsksAdapter;

    private List<PriceAmountPair> mBids;
    private List<PriceAmountPair> mAsks;

    private LinearLayout mLayout;

    public OrderBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int orientation = getResources().getConfiguration().orientation;
        View view = inflater.inflate(orientation == Configuration.ORIENTATION_LANDSCAPE ? R.layout.fragment_order_book_land : R.layout.fragment_order_book, container, false);

        mLayout = (LinearLayout)view.findViewById(R.id.layout);

        mBidsRecyclerView = (RecyclerView) view.findViewById(R.id.rvBids);
        mAsksRecyclerView = (RecyclerView) view.findViewById(R.id.rvAsks);

        mBidsAdapter = new OrderBookAdapter();
        mBidsRecyclerView.setAdapter(mBidsAdapter);
        mBidsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mAsksAdapter = new OrderBookAdapter();
        mAsksRecyclerView.setAdapter(mAsksAdapter);
        mAsksRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        if (mBids == null) {
            return;
        }

        if (mBidsAdapter != null) {
            mBidsAdapter.updateList(mBids);
            mBidsAdapter.notifyDataSetChanged();
        }
        if (mAsksAdapter != null) {
            mAsksAdapter.updateList(mAsks);
            mAsksAdapter.notifyDataSetChanged();
        }
    }

    public void updateOrderBook(OrderBook orderBook) {
        // update lists and notify recyclerview
        mBids = orderBook.getBids();
        mAsks = orderBook.getAsks();

        updateViews();
    }

    public class OrderBookAdapter extends RecyclerView.Adapter<OrderBookAdapter.ViewHolder> {

        private List<PriceAmountPair> mList;

        public void updateList(List<PriceAmountPair> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View bidAskView = inflater.inflate(R.layout.item_bidask, parent, false);
            return new ViewHolder(bidAskView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PriceAmountPair pair = mList.get(position);

            holder.mPriceView.setText("$" + pair.getPrice().toString());
            holder.mAmountView.setText(pair.getAmount()+"");
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mPriceView, mAmountView;

            public ViewHolder(View itemView) {
                super(itemView);

                mPriceView = (TextView)itemView.findViewById(R.id.tvPrice);
                mAmountView = (TextView)itemView.findViewById(R.id.tvAmount);
            }
        }
    }
}
