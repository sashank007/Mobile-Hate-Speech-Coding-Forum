package com.example.codingforum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HateActivity extends Activity {
    ListView listViewHate;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        ArrayList<String> allHate = i.getExtras().getStringArrayList("hate");
        setContentView(R.layout.activity_hate_posts);
        listViewHate=findViewById(R.id.list_view_hate);
        allHate.add("i hate jews");
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allHate);
        listViewHate.setAdapter(itemsAdapter);

    }
}
