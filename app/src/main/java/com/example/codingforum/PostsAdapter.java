package com.example.codingforum;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostsAdapter extends ArrayAdapter<Question> {
    private Activity activity;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private List<Question> lQuestion;
    private static LayoutInflater inflater = null;
    private HashMap<String , Integer> iconMapper = new HashMap<>();
    public PostsAdapter (Activity activity, int textViewResourceId,List<Question> _lExpense) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            this.lQuestion = _lExpense;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lQuestion.size();
    }

    public Question getItem(Question position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView text_body , text_qbody , qid , postedBy;
        public Button delete;
        public ImageView icon;
        public TextView display_type;
        public TextView display_date;

    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {


                vi = inflater.inflate(R.layout.activity_item, null);
                holder = new ViewHolder();

                holder.text_body = (TextView) vi.findViewById(R.id.tv_body);
                holder.text_qbody = (TextView) vi.findViewById(R.id.tv_qbody);
                holder.qid = (TextView)vi.findViewById(R.id.tv_qid);
                holder.delete=vi.findViewById(R.id.btn_delete);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.text_body.setText(lQuestion.get(position).question);
            holder.text_qbody.setText(lQuestion.get(position).questionBody);
            holder.qid.setText(lQuestion.get(position).qid);

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getContext(),"Clicked",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Are you sure you want to delete this post?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getContext(),"Yes",Toast.LENGTH_LONG).show();
                            String currentPostId = lQuestion.get(position).qid;
                            removeChild(currentPostId);

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
                    alertDialogBuilder.show();
                }

            });

        } catch (Exception e) {


        }
        return vi;
    }
    public String getDate(long millis)
    {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(millis));
        return dateString;
    }
    public void removeChild(String id)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Questions").child(id).removeValue();
//        notifyDataSetChanged();
        Toast.makeText(getContext(),"Post "+id+" successfully deleted." , Toast.LENGTH_LONG).show();
    }

}