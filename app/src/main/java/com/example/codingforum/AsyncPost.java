package com.example.codingforum;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class AsyncPost extends AsyncTask<String, Void, Boolean> {
    String post;
    String count;
    public static String TAG="ASYNCPOST";

    public AsyncPost(String post) {
        this.post=post;
    }
    private String postUploadUris[] = {"http://127.0.0.1:5000/home/?post=hello",""};

    protected Boolean doInBackground(String... params) {
        postUploadUris[0]+=post;
        Log.d(TAG,"asyncpost called");
        System.setProperty("https.protocols", "TLSv1.1");

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(postUploadUris[0]);


        HttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            Log.d(TAG,"inside try...");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        int status = response.getStatusLine().getStatusCode();
            int status = 200;

//            Log.d(TAG,"Sending get request..." +response.getEntity().toString());
            if (status == 200) {
//                HttpEntity entity = response.getEntity();
                String data = null;
//                try {
//                    data = EntityUtils.toString(entity);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.print("current data for GET REQUEST: " + data);
                Log.d(TAG,"200 in asyncpost");
                return true;
            }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
//
//        processResult(result);
    }

    public String processResult(String result) {
        System.out.println("result : " + result);
        return result;
    }

}