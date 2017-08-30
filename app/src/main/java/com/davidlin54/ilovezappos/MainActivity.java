package com.davidlin54.ilovezappos;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView{

    private MainPresenter mPresenter;
    private TransactionHistoryFragment mTransactionHistoryFragment;
    private OrderBookFragment mOrderBookFragment;
    private PriceAlertFragment mPriceAlertFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mPresenter = new MainPresenterImpl(this);

        mTransactionHistoryFragment = new TransactionHistoryFragment();
        mOrderBookFragment = new OrderBookFragment();
        mPriceAlertFragment = new PriceAlertFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpMain);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addItem(mTransactionHistoryFragment, "Transaction History");
        adapter.addItem(mOrderBookFragment, "Order Book");
        adapter.addItem(mPriceAlertFragment, "Price Alert");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.layoutTab);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startFetchingData();
    }

    @Override
    protected void onPause() {
        mPresenter.stopFetchingData();
        super.onPause();
    }

    @Override
    public void updateData(final List<Transaction> transactions, final OrderBook orderBook, final TickerHour tickerHour) {
        if (transactions != null) runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionHistoryFragment.updateTransactionHistory(transactions);
            }
        });
        if (orderBook != null) mOrderBookFragment.updateOrderBook(orderBook);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentsTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentsTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentsTitles.get(position);
        }
    }
}
