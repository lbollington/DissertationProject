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

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class AppRequest extends AppCompatActivity {
    EditText requestReason, requestLocation, requestDentist, patientEmail, dayRequest, monthRequest, yearRequest, hourRequest, minsRequest;
    Button submit;
                              //variables

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_request_app);      //onCreate method to set content view

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        patientEmail = findViewById(R.id.etpatientEmail);
        requestReason = findViewById(R.id.etTitle);
        requestLocation= findViewById(R.id.etLocation);
        requestDentist = findViewById(R.id.etDentist);      //intialise variables

        dayRequest = findViewById(R.id.etDay);
        monthRequest = findViewById(R.id.etMonth);
        yearRequest = findViewById(R.id.etYear);
        hourRequest = findViewById(R.id.etHour);
        minsRequest = findViewById(R.id.etMins);

        submit = findViewById(R.id.btnSubmit);

        DocumentReference appointmentRequestsRef = fStore.collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");
                                                                                                //reference for counter
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(requestLocation);    //check fields
                checkField(requestDentist);
                checkField(requestReason);

                if(valid) {
                    userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(AppRequest.this, "Request submitted", Toast.LENGTH_SHORT).show();
                    DocumentReference df = fStore.collection("appointmentRequests").document(patientEmail.getText().toString());
                    appointmentRequestsRef.update("NumOfAppRequests", FieldValue.increment(1)); //increment counter
                    Map<String, Object> Requests = new HashMap<>();
                    //Requests.put("PatientName", patientName.getText().toString());
                    Requests.put("PatientEmail", patientEmail.getText().toString());
                    Requests.put("AppointmentType", requestReason.getText().toString());    //writes to the request collection
                    Requests.put("Location", requestLocation.getText().toString());
                    Requests.put("Dentist", requestDentist.getText().toString());

                    Requests.put("Day", dayRequest.getText().toString());
                    Requests.put("Month", monthRequest.getText().toString());
                    Requests.put("Year", yearRequest.getText().toString());
                    Requests.put("Hour", hourRequest.getText().toString());
                    Requests.put("Mins", minsRequest.getText().toString());

                    df.set(Requests);

                    FirebaseAuth.getInstance();
                    startActivity(new Intent(AppRequest.this, MainActivity.class)); //sends the user back to the dashboard
                    finish();
                }
                else{
                    Toast.makeText(AppRequest.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button back = findViewById(R.id.requestbtnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override                                                                   //brings the user back to the dashboard
            public void onClick(View view) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(AppRequest.this, MainActivity.class));
                finish();
            }
        });
    }


    public boolean checkField(EditText textField) {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{                                                                  //method for check field
            valid = true;
        }
        return valid;
    }

}

