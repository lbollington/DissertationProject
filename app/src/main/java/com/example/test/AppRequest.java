package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AppRequest extends AppCompatActivity {
    EditText requestReason, requestLocation, requestDescription;
    Button submit;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_request_app);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        requestReason = findViewById(R.id.etTitle);
        requestLocation= findViewById(R.id.etLocation);
        requestDescription = findViewById(R.id.etDescription);
        submit = findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(requestReason);
                checkField(requestLocation);
                checkField(requestDescription);

                if(valid) {
                    userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(AppRequest.this, "Request submitted", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fStore.collection("Appointment_Requests").document(userID);
                    Map<String, Object> Requests = new HashMap<>();
                    Requests.put("Reason", requestReason.getText().toString());
                    Requests.put("Location", requestLocation.getText().toString());
                    Requests.put("Description", requestDescription.getText().toString());
                    df.set(Requests);

                    FirebaseAuth.getInstance();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(AppRequest.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button back = findViewById(R.id.requestbtnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }


    public boolean checkField(EditText textField) {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }
}

