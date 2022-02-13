package com.example.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PasswordRequestNotifications extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;

    RecyclerView recyclerViewPass;
    FirebaseFirestore db;
    PasswordRequestNotificationsAdapter myPassAdapter;
    ArrayList<PassRequestData> passRequestList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_pass_request);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerViewPass = findViewById(R.id.passRequests);
        recyclerViewPass.setHasFixedSize(true);
        recyclerViewPass.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        passRequestList = new ArrayList<PassRequestData>();

        myPassAdapter = new PasswordRequestNotificationsAdapter(PasswordRequestNotifications.this, passRequestList);

        recyclerViewPass.setAdapter(myPassAdapter);

        AppEventChangeListener();


    }

    private void AppEventChangeListener() {
        db.collection("passwordResetRequests").orderBy("ResetName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                passRequestList.add(dc.getDocument().toObject(PassRequestData.class));

                            }

                            myPassAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }
                });


    }


}



