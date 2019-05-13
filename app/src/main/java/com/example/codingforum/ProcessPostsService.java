package com.example.codingforum;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;


import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Calendar;


import cz.msebera.android.httpclient.Header;

import static com.example.codingforum.MainActivity.nograkUrl;

public class ProcessPostsService extends JobService
{
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = ProcessPostsService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = firebaseAuth.getCurrentUser();
        Log.d(TAG, "onStartJob: my process posts service class is called.");
        StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
//        pushNotification("Alarm triggered","Alarm!");
        startProcessing();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job)
    {
        return false;
    }


//    public void pushNotification(String msgText , String msgTitle )
//    {
//        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                intent, 0);
//
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.mom_logo)
//                .setTicker(getString(R.string.app_name))
//                //     .setPriority(Notification.PRIORITY_MAX)
//                .setContentTitle(msgTitle)
//                .setContentText(msgText)
//                .setContentIntent(pendingIntent)
//                .setContentInfo("Info");
//
//        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
//    }

    private void startProcessing()
    {

        AsyncHttpClient client = new AsyncHttpClient();

        ProgressDialog dialog;
        //practice video check
        dialog = new ProgressDialog(this);
        //practice video check
        Toast.makeText(this,"Processing the tweets, you will be notified once done."  , Toast.LENGTH_LONG).show();


        client.get(nograkUrl+"/process", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.d("msg success", statusCode + "");
                if (statusCode == 200) {
                    Log.d("msg success", "200");
                    Toast.makeText(getApplicationContext(), "Processing Completed", Toast.LENGTH_SHORT).show();
                    deleteQueueQuestions();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });

    }
    private void deleteQueueQuestions()
    {
        AsyncHttpClient client = new AsyncHttpClient();

        ProgressDialog dialog;
        //practice video check
//        dialog = new ProgressDialog(this);
        //practice video check
        Toast.makeText(this,"All questions in queue deleted."  , Toast.LENGTH_LONG).show();


        client.get(nograkUrl+"/deleteall", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.d("msg success", statusCode + "");
                if (statusCode == 200) {
                    Log.d("deleted questions", "200");
                    Toast.makeText(getApplicationContext(),"Processing complete, all quetions in queue deleted" , Toast.LENGTH_LONG).show();

//                    allQuestions.clear();
//                    updateArrayAdapter(allQuestions);
//                    deleteQueueQuestions();

                } else {
                    Log.d(TAG ,"Failure in deleting all questions");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });
    }


}