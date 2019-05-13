package com.example.codingforum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;

import static com.example.codingforum.MainActivity.nograkUrl;

public class AdminActivity extends Activity {
    DatabaseReference mDatabase;
    ArrayList<String> allQuestions = new ArrayList<>();
    ArrayList<String> allHatePosts = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    ListView listview , listViewHate;
    Button process;
    FirebaseUser mUser;
    Button gotoHate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        listview = findViewById(R.id.list_view_2);
        process=findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        listViewHate = findViewById(R.id.list_view_hate);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        gotoHate=findViewById(R.id.btn_gotohate);
//        getQuestions();
        getFlaskQuestions();
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProcessing();
            }
        });
        gotoHate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this,HateActivity.class);
                i.putExtra("hate",allHatePosts);
                startActivity(i);
            }
        });
    }

    private void getQuestions() {

        Query myQuery = mDatabase.child("Questions");
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Question> myList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    Question exp = snap.getValue(Question.class);
//                    myList.add(exp);
//                }
//                updateArrayAdapter(myList);
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Question ques = snap.getValue(Question.class);
                getQuestionValues((Map<String,Object>) dataSnapshot.getValue());
//                    Log.d(TAG,"Current questio : " + ques);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFlaskQuestions()
    {
        AsyncHttpClient client = new AsyncHttpClient();
//        Toast.makeText(AdminActivity.this,"Post Submitted" , Toast.LENGTH_LONG).show();

        client.get(nograkUrl+"/getAll", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                String s = new String(bytes);

                try {
                    JSONObject  j = new JSONObject(s);
                    JSONArray jArra = j.getJSONArray("question");

                    for(int i = 0 ; i <jArra.length();i++) {
                        allQuestions.add(jArra.getString(i));
                        Log.d("each question ", jArra.getString(i));
                    }

                    updateArrayAdapter(allQuestions);

                    Log.d("json", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // handle success response
                Log.d("msg success", statusCode + "");
                Log.d("bytes", s);
                if (statusCode == 200) {

//                    Toast.makeText(AdminActivity.this, "Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AdminActivity.this, "Failed to retrieve all posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });
    }
        private void getQuestionValues(Map<String,Object> questions)
        {

        ArrayList<String> allQ = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : questions.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allQ.add((String) singleUser.get("question"));
        }

        updateArrayAdapter(allQ);
    }
    private void updateArrayAdapter(ArrayList<String> allQ)
    {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allQ);
        listview.setAdapter(itemsAdapter);
    }
    private void updateHateArray(ArrayList<String> allQ)
    {

        if(allQ.size()!=0)
            Toast.makeText(this,"New Hate Speech Detected" , Toast.LENGTH_LONG).show();
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allQ);
//        listViewHate.setAdapter(itemsAdapter);
    }

    private void startProcessing()
    {


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(600000);
        client.setConnectTimeout(600000);
        ProgressDialog dialog;
        //practice video check
        dialog = new ProgressDialog(AdminActivity.this);
        //practice video check
        Toast.makeText(AdminActivity.this,"Processing the tweets, you will be notified once done."  , Toast.LENGTH_LONG).show();


        client.get(nograkUrl+"/process", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.d("msg success", statusCode + "");
                if (statusCode == 200) {
                    Log.d("msg success", "200");
                    Toast.makeText(AdminActivity.this, "Processing Completed", Toast.LENGTH_SHORT).show();
                    deleteQueueQuestions();
                    String s = new String(bytes);
                    try {
                        JSONArray jArray = new JSONArray(s);
                        for(int i= 0 ; i<jArray.length();i++)
                        {
                            allHatePosts.add(jArray.getString(i));
                        }
//                        Log.d("HATE",jArray.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                updateHateArray(allHatePosts);
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
        dialog = new ProgressDialog(AdminActivity.this);
        //practice video check
        Toast.makeText(AdminActivity.this,"All questions in queue deleted."  , Toast.LENGTH_LONG).show();


        client.get(nograkUrl+"/deleteall", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                // handle success response
                Log.d("msg success", statusCode + "");

                if (statusCode == 200) {
                    Log.d("deleted questions", "200");
                    Toast.makeText(AdminActivity.this, "Processing Completed", Toast.LENGTH_SHORT).show();
                    allQuestions.clear();
                    updateArrayAdapter(allQuestions);
//                    deleteQueueQuestions();


                } else {
                    Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });
    }


}
