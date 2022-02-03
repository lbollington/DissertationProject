package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AppRequest extends AppCompatActivity {
    EditText requestReason, requestLocation, requestDescription, patientName, patientEmail;
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

        patientName = findViewById(R.id.etpatientName);
        patientEmail = findViewById(R.id.etpatientEmail);
        requestReason = findViewById(R.id.etTitle);
        requestLocation= findViewById(R.id.etLocation);
        requestDescription = findViewById(R.id.etDescription);
        submit = findViewById(R.id.btnSubmit);

        DocumentReference appointmentRequestsRef = fStore.collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(requestReason);
                checkField(requestLocation);
                checkField(requestDescription);
                checkField(patientName);

                if(valid) {
                    userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(AppRequest.this, "Request submitted", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fStore.collection("appointmentRequests").document(userID);
                    appointmentRequestsRef.update("NumOfAppRequests", FieldValue.increment(1));
                    Map<String, Object> Requests = new HashMap<>();
                    Requests.put("PatientName", patientName.getText().toString());
                    Requests.put("PatientEmail", patientEmail.getText().toString());
                    Requests.put("AppointmentType", requestReason.getText().toString());
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

