package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Register extends AppCompatActivity
{
    EditText fullName, email, password, phone;
    Button registerBtn, goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isDentistBox, isPatientBox, isAdminBox;
    String userID;
    CardView cardOne, cardTwo, cardThree, cardFour;
    private boolean isAtLeast8 = false, hasUpperCase = false, hasNumber = false, hasSymbol = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.goToLogin);

        isDentistBox = findViewById(R.id.isDentist);
        isPatientBox = findViewById(R.id.isPatient);
        isAdminBox = findViewById(R.id.isAdmin);

        cardOne = findViewById(R.id.cardOne);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        cardFour = findViewById(R.id.cardFour);

        inputChange();

        isPatientBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isDentistBox.setChecked(false);
                    isAdminBox.setChecked(false);
                }
            }
        });

        isDentistBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isPatientBox.setChecked(false);
                    isAdminBox.setChecked(false);
                }
            }
        });

        isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isPatientBox.setChecked(false);
                    isDentistBox.setChecked(false);
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                if(!(isDentistBox.isChecked() || isPatientBox.isChecked() || isAdminBox.isChecked())){
                    Toast.makeText(Register.this, "Select an Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(valid){
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(userID);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Full Name", fullName.getText().toString());
                            userInfo.put("User Email", email.getText().toString());
                            userInfo.put("Phone Number", phone.getText().toString());
                            //access level
                            if (isDentistBox.isChecked()) {
                                userInfo.put("isDentist", "1");
                                startActivity(new Intent(getApplicationContext(), DentistActivity.class));
                                finish();
                            }
                            if (isAdminBox.isChecked()) {
                                userInfo.put("isAdmin", "1");
                                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                finish();
                            }
                            if (isPatientBox.isChecked()) {
                                userInfo.put("isPatient", "1");
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                            df.set(userInfo);
                            }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    public boolean checkField(EditText textField)
    {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }

    @SuppressLint("ResourceType")
    public void passwordCheck () {
        String name = fullName.getText().toString(), Email = email.getText().toString(), Password = password.getText().toString();
        CharSequence cs = password.toString();
        //8 characters
        if(password.length() >=8){
            isAtLeast8 = true;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            isAtLeast8 = false;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));

        }

        //for uppercase
        Pattern upper = Pattern.compile("(.*[A-Z].*)");
        Matcher upper_m = upper.matcher(cs);
        if(upper_m.find()){
            hasUpperCase = true;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasUpperCase = false;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for number
        Pattern number = Pattern.compile("(.*[0-9].*)");
        Matcher number_m = number.matcher(password.toString());
        System.out.println(number_m);
        if(number_m.find()){
            hasNumber =true;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasNumber = false;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for symbol

        Pattern symbol = Pattern.compile("^(?=.*[_.()]).*$");
        Matcher symbol_m = symbol.matcher(cs);
        if(symbol_m.find()){
            hasSymbol=true;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasSymbol =false;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

    }

    private void inputChange(){
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}



