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

public class DentalHistory extends AppCompatActivity {

    String data;

    RecyclerView recyclerView;                                                          //variables
    FirebaseFirestore db;
    DentalHistoryAdapter documentAdapter;
    ArrayList<DentalHistoryData> list_of_documents;
    ProgressDialog progressDialog;

    Button docBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                         //onCreate method to set content view
        setContentView(R.layout.activity_dental_history);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");                      //process dialog
        progressDialog.show();

        data = DentistUpload.getInstance().getData();

        recyclerView = findViewById(R.id.documentList);
        docBack = findViewById(R.id.docBack);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list_of_documents = new ArrayList<DentalHistoryData>();

        documentAdapter = new DentalHistoryAdapter(DentalHistory.this, list_of_documents);      //initialise adapter

        recyclerView.setAdapter(documentAdapter);

        EventChangeListener();

        docBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                               //takes user back to dashboard
                FirebaseAuth.getInstance();
                startActivity(new Intent(recyclerView.getContext(), MainActivity.class));
                finish();
            }
        });

    }

    private void EventChangeListener() {
        db.collection(data).orderBy("AppName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if(progressDialog.isShowing())                                                       //displays item in recycler view and checks for document changes
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                list_of_documents.add(dc.getDocument().toObject(DentalHistoryData.class));

                            }

                            documentAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();           //process dialog

                        }
                    }
                });


    }


}

