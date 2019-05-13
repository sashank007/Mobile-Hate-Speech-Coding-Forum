package com.example.codingforum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class QuestionsFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;
    FirebaseUser mUser;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View v = lf.inflate(R.layout.fragment_posts, container, false);
        //get db


        listView = v.findViewById(R.id.listview_posts);


        return v;
    }


    private void getQuestions() {

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
        PostsAdapter adapter = new PostsAdapter(getActivity(),R.layout.activity_item,allQ);
        listView.setAdapter(adapter);
        listView.invalidate();
    }



}
