package com.davidlin54.ilovezappos;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.math.BigDecimal;


public class PriceAlertFragment extends Fragment {

    private CurrencyEditText mValueEditText;
    private TextView mCurrentPriceTextView;
    private Button mSetAlertButton;

    private BigDecimal mCurrentPrice;

    private FirebaseJobDispatcher mDispatcher;

    private static final String JOB_TAG = "PriceAlertJob";
    private static final String SCHEDULED = "isScheduled";

    public PriceAlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_price_alert, container, false);

        mValueEditText = (CurrencyEditText) view.findViewById(R.id.etValue);
        mCurrentPriceTextView = (TextView) view.findViewById(R.id.tvCurrentPrice);
        mSetAlertButton = (Button) view.findViewById(R.id.button);
        mSetAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if alert not set, set; otherwise, cancel
                if (mValueEditText.isEnabled()) {
                    BigDecimal value = new BigDecimal(mValueEditText.getText().toString());
                    setScheduledValue(value);

                    Bundle bundle = new Bundle();
                    bundle.putString(PriceAlertJobService.VALUE_KEY, value.toString());

                    Job job = mDispatcher.newJobBuilder()
                            .setService(PriceAlertJobService.class)
                            .setTag(JOB_TAG)
                            .setRecurring(true)
                            .setLifetime(Lifetime.FOREVER)
                            .setTrigger(Trigger.executionWindow(60 * 60, 60 * 60 + 10))
                            .setReplaceCurrent(true)
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            .setExtras(bundle)
                            .build();

                    Log.i("read54", "f2");

                    mDispatcher.mustSchedule(job);
                    updateView();
                } else {
                    setScheduledValue(new BigDecimal("-1"));
                    mDispatcher.cancel(JOB_TAG);
                    updateView();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    // update views
    private void updateView() {
        if (mCurrentPriceTextView != null) {
            mCurrentPriceTextView.setText(mCurrentPrice != null ? "$" + mCurrentPrice.toString() : "Loading...");

            if (getScheduledValue().compareTo(new BigDecimal("-1")) == 0) {
                mValueEditText.setEnabled(true);
                mSetAlertButton.setText("Set Price Alert");
            } else {
                mValueEditText.setText(String.format("%.2f", getScheduledValue()));
                mValueEditText.setEnabled(false);
                mSetAlertButton.setText("Cancel Price Alert");
            }
        }
    }

    public void updateCurrentPrice(TickerHour tickerHour) {
        mCurrentPrice = tickerHour.getLast();
        updateView();
    }

    // set value in preferences
    private void setScheduledValue(BigDecimal value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putString(SCHEDULED, value.toString()).commit();
    }

    private BigDecimal getScheduledValue() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return new BigDecimal(preferences.getString(SCHEDULED, "-1"));
    }
}
