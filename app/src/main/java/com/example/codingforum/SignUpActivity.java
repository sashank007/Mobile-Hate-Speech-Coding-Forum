package com.example.codingforum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignUpActivity extends Activity {

    public static String INTENT_PHONE = "INTENT_PHONE";
    public static String INTENT_EMAIL = "INTENT_EMAIL";
    public static String INTENT_WORD = "INTENT_WORD";
    public static String INTENT_SETTINGS = "INTENT_SETTINGS";
    public static String INTENT_FIRST_TIME = "INTENT_FIRST_TIME";
    public static String INTENT_TIME_WATCHED = "INTENT_TIME_WATCHED";
    public static String INTENT_TIME_WATCHED_VIDEO = "INTENT_TIME_WATCHED_VIDEO";
    public static String INTENT_URI = "INTENT_URI";
    public static String INTENT_SERVER_ADDRESS = "INTENT_SERVER_ADDRESS";
    public static String INTENT_PRACTICE = "INTENT_PRACTICE";
    private Button bt_login,bt_signup;
    private SharedPreferences sharedPreferences;
    private long time_to_login=0;
    private FirebaseAuth firebaseAuth;
    public static int RC_SIGN_IN=1;
    private DatabaseReference mDatabase;
    EditText et_email, et_phone , et_firstName  , et_password , et_userName;
    String email ,passWord,userName;
    String phone;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth  = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_signup);
        initializeVars();

    }


    private void initializeVars()
    {
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        time_to_login=System.currentTimeMillis();
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_userName = findViewById(R.id.et_username);


        et_password=findViewById(R.id.et_password);

        bt_signup=findViewById(R.id.btn_signup);
        bt_login=findViewById(R.id.btn_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();

            }
        });
    }

    private void writeNewUser(String uid,String username  ,String email , String password) {

        System.out.println("write new user: " );
        final String f_email = email  , f_username=username, f_password = password;
        firebaseAuth.createUserWithEmailAndPassword(email,f_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser(); //You Firebase user
                    // user registered, start profile activity
                    Toast.makeText(SignUpActivity.this,"Account Created",Toast.LENGTH_LONG).show();
                    System.out.println("new user succesfully created");
                    User newUser = new User(f_username,f_password,f_email);
                    mDatabase.child("users").child(user.getUid()).setValue(newUser);

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Exception e = task.getException();

                    System.out.println("error creating user: " + task.getException().toString());
                    Toast.makeText(SignUpActivity.this,"Could not create account: " + e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void loginUser(String email , String password)
    {
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)) {

            Toast.makeText(getApplicationContext(),"Please provide email and password",Toast.LENGTH_LONG).show();
        }
        else
        {
            final String f_email = email, f_password = password;
            firebaseAuth.signInWithEmailAndPassword(f_email, f_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Not a valid email id or password" , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void signup() {


//        if( ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECEIVE_SMS)
//                != PackageManager.PERMISSION_GRANTED ) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            Toast.makeText(this,"Without access to SMS we cannot let you login." , Toast.LENGTH_LONG).show();
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.RECEIVE_SMS)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_SMS},
//                        101);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        100);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//
//        } else {
            if (et_email.getText().toString().isEmpty()||et_password.getText().toString().isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("ALERT");
                alertDialog.setMessage("Please Enter Appropriate Information!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {

                email = et_email.getText().toString().trim().toLowerCase();
                userName = et_userName.getText().toString().trim();
                passWord = et_password.getText().toString().trim();
                String uniqueID = UUID.randomUUID().toString();
                writeNewUser(uniqueID,userName, email,passWord);
//
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra(INTENT_EMAIL, email);
//                intent.putExtra(INTENT_PHONE, phone);
//
//                if (sharedPreferences.edit().putString(INTENT_EMAIL, email).commit() &&
//                        sharedPreferences.edit().putString(INTENT_PHONE, phone).commit() && sharedPreferences.edit().putString(INTENT_FIRST_TIME, "true").commit() ) {
//
//                    time_to_login = System.currentTimeMillis() - time_to_login;
//
//                    sharedPreferences.edit().putInt(getString(R.string.login), sharedPreferences.getInt(getString(R.string.login), 0) + 1).apply();
//                    HashSet<String> hashset = (HashSet<String>) sharedPreferences.getStringSet("LOGIN_TIME", new HashSet<String>());
//                    hashset.add("LOGIN_ATTEMPT_" + sharedPreferences.getInt(getString(R.string.login), 0) + "_" + phone + "_" + email + "_" + time_to_login);
//                    sharedPreferences.edit().putStringSet("LOGIN_TIME", hashset).apply();
//                    startActivity(intent);
//                    this.finish();
//
//                }


            }
        }
    }

