package com.davidlin54.ilovezappos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by david.lin1ibm.com on 2017-09-08.
 */

public class PriceAlertJobService extends JobService {
    public final static String VALUE_KEY = "Value";
    private final static String NOTIFICATION_CHANNEL = "Channel";

    @Override
    public boolean onStartJob(final JobParameters job) {
        Call<TickerHour> call = ILoveZapposApplication.mClient.ticker_hour();
        call.enqueue(new Callback<TickerHour>() {
            @Override
            public void onResponse(Call<TickerHour> call, Response<TickerHour> response) {
                TickerHour tickerHour = response.body();
                if (tickerHour != null &&
                        job.getExtras() != null &&
                        tickerHour.getLast().compareTo(new BigDecimal(job.getExtras().getString(VALUE_KEY, "-1"))) == -1) {
                    sendNotification(new BigDecimal(job.getExtras().getString(VALUE_KEY, "-1")));
                }
            }

            @Override
            public void onFailure(Call<TickerHour> call, Throwable t) {

            }
        });

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public void sendNotification(BigDecimal value) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder =
                new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Bitcoin Price Alert")
                        .setContentText("The price of Bitcoin has dropped below $" + String.format("%.2f", value));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(NOTIFICATION_CHANNEL);
        }

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(1, builder.build());
    }
}
