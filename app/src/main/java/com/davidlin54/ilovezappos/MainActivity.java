package com.davidlin54.ilovezappos;

import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

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
    public void updateTransactionHistory(List<Transaction> transactions) {
        mTransactionHistoryFragment.updateTransactionHistory(transactions);
    }

    @Override
    public void updateOrderBook(OrderBook orderBook) {
        mOrderBookFragment.updateOrderBook(orderBook);
    }

    @Override
    public void updateTickerHour(TickerHour tickerHour) {
        mPriceAlertFragment.updateCurrentPrice(tickerHour);
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragments.set(position, fragment);
            return fragment;
        }
    }
}
