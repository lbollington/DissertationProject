package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AppSchedule extends AppCompatActivity {
    EditText reason, location, description, patientname, patientemail;
    Button addEvent;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_schedule);

        patientname= findViewById(R.id.etpatientName);
        patientemail= findViewById(R.id.etpatientEmail);
        reason = findViewById(R.id.etReason);
        location= findViewById(R.id.etLocation);
        description = findViewById(R.id.etDescription);
        addEvent = findViewById(R.id.btnAdd);

        fStore = FirebaseFirestore.getInstance();

        //ReadAppRequest();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!reason.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, reason.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                    intent.putExtra(Intent.EXTRA_EMAIL, "test@yahoo.com");

                    FirebaseAuth.getInstance();
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(AppSchedule.this, "There is no app that can support this action", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                finish();
            }
        });
    }

    /*private void ReadAppRequest() {
        DocumentReference appReadRequests = fStore.collection("appointmentRequests").document("FF694lCGOqcdNTQpv1FcVvqUSy42"); //arraylist reference needed
        appReadRequests.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    patientname.setText(documentSnapshot.getString("PatientName"));
                    patientemail.setText(documentSnapshot.getString("PatientEmail"));
                    reason.setText(documentSnapshot.getString("AppointmentType"));
                    location.setText(documentSnapshot.getString("Location"));
                    description.setText(documentSnapshot.getString("Description"));
                }
            }
        });
    }*/
}



