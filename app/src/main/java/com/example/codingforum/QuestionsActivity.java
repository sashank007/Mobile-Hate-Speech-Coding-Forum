package com.example.codingforum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

public class QuestionsActivity  extends Activity {
    EditText newQuestion , newBody;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        newQuestion = findViewById(R.id.et_question);
        newBody = findViewById(R.id.et_body);

        submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(newQuestion.getText().toString()))
                    submitQuestion(newQuestion.getText().toString() , newBody.getText().toString());

            }
        });

    }
    private void submitQuestion(String question,String body)
    {
        addToDatabase(question);
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("question",question);
        i.putExtra("body",body);
        startActivity(i);


    }
    private void addToDatabase(String question)
    {

    }

}

