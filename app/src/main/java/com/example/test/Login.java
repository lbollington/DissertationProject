package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText password, email;
    TextView goToPasswordReset, goToRegister;
    Button loginBtn;
    boolean valid = true;
    FirebaseAuth fAuth;                             //variables
    FirebaseFirestore fStore;
    FirebaseDatabase database;
    String FullName, Email;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);        //onCreate method to set content view

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        appName = findViewById(R.id.appName);
        password = findViewById(R.id.loginPassword);    //set variables
        email = findViewById(R.id.loginEmail);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.goToRegister);
        goToPasswordReset = findViewById(R.id.tvForgotPass);

        database = FirebaseDatabase.getInstance();

        loginBtn.setOnClickListener(view -> {
            checkField(email);      //check fields
            checkField(password);

            if (valid) {
                fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(authResult -> {                                      //authenticates as a known user
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        checkUserLevel();
                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    } else {
                        user.sendEmailVerification();       //first time user must verify before logging in
                        Toast.makeText(Login.this, "You must verify your account to be granted access, please check your email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        goToPasswordReset.setOnClickListener(v -> {                                               //navigates to password reset
            startActivity(new Intent(Login.this, ForgottenPassword.class));
        });

        //navigates to data protection form
        goToRegister.setOnClickListener(view -> startActivity(new Intent(Login.this, DataProtectionForm.class)));
    }
    public void checkField(EditText textField)
    {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }                                                               //check field
    }

    private void checkUserLevel() {
        DatabaseReference myRef = database.getReference("ApprovedUsers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                  //checks the type of user logging in
                if(Objects.requireNonNull(snapshot.getValue()).toString().equals("Dentist")){
                    startActivity(new Intent(Login.this, DentistActivity.class));
                    finish();
                }
                else if(Objects.requireNonNull(snapshot.getValue()).toString().equals("Patient")){
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else if(Objects.requireNonNull(snapshot.getValue()).toString().equals("Admin")){
                    startActivity(new Intent(Login.this, AdminActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DocumentReference df = fStore.collection("approvedUsers").document(email.getText().toString());
        //extract data
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

            FullName = Objects.requireNonNull(documentSnapshot.get("FullName")).toString();
            Email = Objects.requireNonNull(documentSnapshot.get("UserEmail")).toString();

            //user access level
            if (Objects.equals(documentSnapshot.getString("UserType"), "Dentist")) {
                startActivity(new Intent(getApplicationContext(), DentistActivity.class));
                finish();
            }                                                                                       //displays the relevant dashboard to the user

            else if (Objects.equals(documentSnapshot.getString("UserType"), "Patient")) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            else if (Objects.equals(documentSnapshot.getString("UserType"), "Admin")) {
                Intent adminActivity = new Intent(getApplicationContext(), AdminActivity.class);
                adminActivity.putExtra("FullName", FullName);
                adminActivity.putExtra("UserEmail", Email);
                startActivity(adminActivity);
                finish();
            }
        });
    }

    }


