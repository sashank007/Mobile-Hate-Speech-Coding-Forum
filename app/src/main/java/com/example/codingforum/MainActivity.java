package com.example.codingforum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.firebase.ui.auth.data.model.User;
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

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static String TAG="MainActivity";
    ListView listView;
    List<String> questionsPosted = new ArrayList<>();
    DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser mUser;
    private FirebaseJobDispatcher dispatcher;
    public static String nograkUrl="http://0b35cd83.ngrok.io";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(MainActivity.this);
        listView = findViewById(R.id.list_view);


        //firebase declrarations
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dispatcher =
                new FirebaseJobDispatcher(
                        new GooglePlayDriver(this)
                );


        //get intent for question if exists
        Intent i = getIntent();
        if(i.hasExtra("question"))
        {
            String q = i.getExtras().getString("question");
            String b  = i.getExtras().getString("body");
            questionsPosted.add(q);
            addQuestion(q,b);

        }
        //fetch all the questions from firebase database
        getQuestions();
        //start the job dispatcher
        scheduleJob();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getApplicationContext(),QuestionsActivity.class));
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,AdminActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addQuestion(String question,String body) {
        Log.d(TAG,"inside add question");
        uploadPost(question);
        String uniqueID = UUID.randomUUID().toString();
        Question q = new Question(uniqueID, question,body );
        mDatabase.child("Questions").child(uniqueID).setValue(q);

    }

    private void scheduleJob()
    {
        Log.d("MainActivity","scheduledjob");
        dispatcher.mustSchedule(
                dispatcher.newJobBuilder()
                        .setService(ProcessPostsService.class)
                        .setTag("ProcessPostsService")
                        .setLifetime(Lifetime.FOREVER)
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(1800  , 1805))
                        .build());
    }
//
//    private void getQuestions() {
//
//        Query myQuery = mDatabase.child("Questions");
//        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            List<Question> myList = new ArrayList<>();
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
////                for (DataSnapshot snap : dataSnapshot.getChildren()) {
////                    Question ques = snap.getValue(Question.class);
//                    getQuestionValues((Map<String,Object>) dataSnapshot.getValue());
////                    Log.d(TAG,"Current questio : " + ques);
////                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void getQuestionValues(Map<String,Object> questions) {
//
//        ArrayList<String> allQ = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : questions.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            allQ.add((String) singleUser.get("question"));
//        }
//
//        updateArrayAdapter(allQ);
//    }

//    private void uploadPost(String post)
//    {
//        try {
//             Boolean bool = new AsyncPost(post).execute().get();
//            Log.d(TAG,"Finished uploading post : "  + bool );
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


//    private void updateArrayAdapter(ArrayList<String> allQ)
//    {
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allQ);
//        listview.setAdapter(itemsAdapter);
//        listview.invalidate();
//    }
    private void uploadPost(String post) {
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(MainActivity.this,"Post Submitted" , Toast.LENGTH_LONG).show();
        client.get(nograkUrl+"/home/?post="+post, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.e("msg success", statusCode + "");
                if (statusCode == 200) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });
    }
    private void getQuestions() {

        pd.show();
        Query myQuery = mDatabase.child("Questions");
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Question> myList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Question exp = snap.getValue(Question.class);
                    myList.add(exp);
                }
                updateArrayAdapter(myList);
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    Question ques = snap.getValue(Question.class);
//                getQuestionValues((Map<String,Object>) dataSnapshot.getValue());
//                    Log.d(TAG,"Current questio : " + ques);
//                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //    private void getQuestionValues(Map<String,Object> questions) {
//
//        ArrayList<String> allQ = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : questions.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            allQ.add((String) singleUser.get("question"));
//        }
//
//        updateArrayAdapter(allQ);
//    }
    private void updateArrayAdapter(ArrayList<Question> allQ)
    {
        pd.dismiss();
        PostsAdapter adapter = new PostsAdapter(MainActivity.this,R.layout.activity_item,allQ);
        listView.setAdapter(adapter);
        listView.invalidate();
    }
    public void removeCard()
    {
        Toast.makeText(getApplicationContext(),"Card removed",Toast.LENGTH_LONG).show();
    }

}