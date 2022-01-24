package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPatient extends AppCompatActivity
{
    EditText patientfullName, patientemail, patientpassword, patientphone;
    Button patientregBtn, goToAdmin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    CardView cardOnePatientReg, cardTwoPatientReg, cardThreePatientReg, cardFourPatientReg;
    private boolean isAtLeast8PatientReg = false, hasUpperCasePatientReg = false, hasNumberPatientReg = false, hasSymbolPatientReg = false, isSpacePatientReg = false, validPatientEmail = false, validPhoneNumber = false, validPassword = false, emptyNameCheck = false, emptyEmailCheck =false, emptyPasswordCheck =false, emptyPhoneNumberCheck =false, noEmptyFieldsExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        patientfullName = findViewById(R.id.patientregisterName);
        patientemail = findViewById(R.id.patientregisterEmail);
        patientpassword = findViewById(R.id.patientregisterPassword);
        patientphone = findViewById(R.id.patientregisterPhone);
        patientregBtn = findViewById(R.id.patientregisterBtn);
        goToAdmin = findViewById(R.id.goToAdmin);

        cardOnePatientReg = findViewById(R.id.card1PatientReg);
        cardTwoPatientReg = findViewById(R.id.card2PatientReg);
        cardThreePatientReg = findViewById(R.id.card3PatientReg);
        cardFourPatientReg = findViewById(R.id.card4PatientReg);

        inputChange();

        patientregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyNameCheck = checkField(patientfullName);
                emptyEmailCheck = checkField(patientemail);
                emptyPasswordCheck = checkField(patientpassword);
                emptyPhoneNumberCheck = checkField(patientphone);

                if (emptyNameCheck && emptyEmailCheck && emptyPasswordCheck && emptyPhoneNumberCheck){
                    noEmptyFieldsExist = true;
                }else{
                    noEmptyFieldsExist = false;
                }

                if (validPatientEmail && validPhoneNumber && validPassword && noEmptyFieldsExist) {
                    fAuth.createUserWithEmailAndPassword(patientemail.getText().toString(), patientpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(RegisterPatient.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Patient").document(userID);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Full Name", patientfullName.getText().toString());
                            userInfo.put("User Email", patientemail.getText().toString());
                            userInfo.put("Phone Number", patientphone.getText().toString());
                            userInfo.put("isPatient", "1");
                            df.set(userInfo);
                            }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPatient.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        goToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
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

    public void emailCheck(){
        String Email = patientemail.getText().toString();
        String emailRegex = "[\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";

        //check for whitespace
        for(char currentChar : Email.toCharArray()){
            isSpacePatientReg = Character.isWhitespace(currentChar);
        }
        Pattern patient_email_chars = Pattern.compile(emailRegex);
        Matcher patient_email_m = patient_email_chars.matcher(Email);
        if(patient_email_m.matches() && !isSpacePatientReg){
            validPatientEmail = true;
        }else{
            validPatientEmail = false;
        }

    }

    @SuppressLint("ResourceType")
    public void passwordCheck () {
        String Password = patientpassword.getText().toString();
        int PassLength = Password.length();
        //8 characters
        if(PassLength >= 8 && PassLength <= 15 && !isSpacePatientReg){
            isAtLeast8PatientReg= true;
            cardOnePatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            isAtLeast8PatientReg = false;
            cardOnePatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));

        }
        //check for whitespace
        for(char currentChar : Password.toCharArray()){
            isSpacePatientReg = Character.isWhitespace(currentChar);
        }


        //for uppercase
        Pattern upper = Pattern.compile("(.*[A-Z].*)");
        Matcher upper_m = upper.matcher(Password);
        if(upper_m.matches()){
            hasUpperCasePatientReg = true;
            cardTwoPatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasUpperCasePatientReg = false;
            cardTwoPatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for number
        Pattern number = Pattern.compile("(.*[0-9].*)");
        Matcher number_m = number.matcher(Password);
        if(number_m.matches()){
            hasNumberPatientReg =true;
            cardThreePatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasNumberPatientReg = false;
            cardThreePatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for symbol
        Pattern symbol = Pattern.compile("[a-zA-Z0-9]*");
        Matcher symbol_m = symbol.matcher(Password);
        System.out.println(symbol_m);
        if(!symbol_m.matches() && !isSpacePatientReg){
            hasSymbolPatientReg=true;
            cardFourPatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasSymbolPatientReg =false;
            cardFourPatientReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        if(isAtLeast8PatientReg && hasUpperCasePatientReg && hasNumberPatientReg && hasSymbolPatientReg && !isSpacePatientReg){
            validPassword = true;
        }else{
            validPassword = false;
        }

    }

    public void phoneNumberCheck(){
    String PhoneNumber = patientphone.getText().toString();
        int PhoneNumberLength = PhoneNumber.length();

        for (char currentChar : PhoneNumber.toCharArray()) {
            isSpacePatientReg = Character.isWhitespace(currentChar);
        }

        if (PhoneNumberLength == 11 && !isSpacePatientReg) {
            validPhoneNumber = true;

        } else {
            validPhoneNumber = false;

        }
    }


    private void inputChange(){
        patientpassword.addTextChangedListener(new TextWatcher() {
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

        patientemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                emailCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        patientphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                phoneNumberCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}


