package com.davidlin54.ilovezappos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryFragment extends Fragment {
    private List<Entry> mEntries = new ArrayList<>();

    private LineChart mLineChart;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        mLineChart = (LineChart)view.findViewById(R.id.lineChart);

        // set empty description
        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);

        // format the x axis values
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return convertUnixToTime((long)value);
            }
        });

        // format the y axis values
        IAxisValueFormatter yAxisValueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "$" + String.format(Locale.US, "%.2f", value);
            }
        };
        mLineChart.getAxisLeft().setValueFormatter(yAxisValueFormatter);
        mLineChart.getAxisRight().setEnabled(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLineChart();
    }

    private void updateLineChart() {
        if (mEntries.size() == 0) {
            return;
        }

        LineDataSet dataSet = new LineDataSet(mEntries, "Price in USD");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);

        LineData data = new LineData(dataSet);
        data.setHighlightEnabled(false);
        mLineChart.setData(data);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

    public void updateTransactionHistory(List<Transaction> transactions) {
        if (mLineChart == null) {
            return;
        }

        mEntries.clear();

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            mEntries.add(new Entry(transaction.getDate(), transaction.getPrice().floatValue()));
        }

        updateLineChart();
    }

    private String convertUnixToTime(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
