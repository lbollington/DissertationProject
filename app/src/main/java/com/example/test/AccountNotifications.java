package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AccountNotifications extends AppCompatActivity {
    TextView accountRequestOne, accountRequestTwo, accountRequestThree, accountRequestFour, accountRequestFive, accountRequestSix, accountRequestSeven, accountRequestEight;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;

    Button docBack;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    FirebaseFirestore db;                                   //defined variables
    AccountNotificationsAdapter myAdapter;
    ArrayList<UsersData> list;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);               //onCreate method to set the content view

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);                    //process dialog in the event there is no records in the recyclerview
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        docBack = findViewById(R.id.docBackAcc);
        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);                                     //setting variables
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<UsersData>();

        myAdapter = new AccountNotificationsAdapter(AccountNotifications.this, list);
                                                                                                //initialising adapter
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();                                                      //calling method

        docBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(recyclerView.getContext(), AdminActivity.class));          //on-click listener to navigate back to Admin dashboard
                finish();
            }
        });


    }

    private void EventChangeListener() {
        db.collection("Users").orderBy("FullName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());                                                     //loops for document changes and orders by ascending fullname
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                list.add(dc.getDocument().toObject(UsersData.class));

                            }

                            myAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())                               //process dialog shown
                                progressDialog.dismiss();

                        }
                    }
                });


                    }


                }

