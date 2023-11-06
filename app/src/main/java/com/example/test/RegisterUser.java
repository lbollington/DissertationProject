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

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity
{
    EditText userfullName, useremail, userpassword, userphone;
    Button userregBtn, goToAdmin;
    boolean valid = true;
    CheckBox isDentistBox, isPatientBox;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    FirebaseFirestore fStore;
    String userID;

    AccountNotificationsAdapter myAdapter;
    ArrayList<UsersData> list;

    CardView cardOneUserReg, cardTwoUserReg, cardThreeUserReg, cardFourUserReg;
    private boolean isAtLeast8UserReg = false, hasUpperCaseUserReg = false, hasNumberUserReg = false, hasSymbolUserReg = false, isSpaceUserReg = false, validUserEmail = false, validPhoneNumber = false, validPassword = false, emptyNameCheck = false, emptyEmailCheck =false, emptyPasswordCheck =false, emptyPhoneNumberCheck =false, noEmptyFieldsExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        list = new ArrayList<UsersData>();

        myAdapter = new AccountNotificationsAdapter(RegisterUser.this, list);

        userfullName = findViewById(R.id.userregisterName);
        useremail = findViewById(R.id.userregisterEmail);
        userpassword = findViewById(R.id.userregisterPassword);
        userphone = findViewById(R.id.userregisterPhone);
        userregBtn = findViewById(R.id.userregisterBtn);
        goToAdmin = findViewById(R.id.goToAdmin);

        isDentistBox = findViewById(R.id.isDentist);
        isPatientBox = findViewById(R.id.isPatient);

        cardOneUserReg = findViewById(R.id.card1UserReg);
        cardTwoUserReg = findViewById(R.id.card2UserReg);
        cardThreeUserReg = findViewById(R.id.card3UserReg);
        cardFourUserReg = findViewById(R.id.card4UserReg);

        //ReadAccountRequest();
        inputChange();

        isPatientBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isDentistBox.setChecked(false);
                }
            }
        });

        isDentistBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isPatientBox.setChecked(false);
                }
            }
        });

        userregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyNameCheck = checkField(userfullName);
                emptyEmailCheck = checkField(useremail);
                emptyPasswordCheck = checkField(userpassword);
                emptyPhoneNumberCheck = checkField(userphone);

                if (emptyNameCheck && emptyEmailCheck && emptyPasswordCheck && emptyPhoneNumberCheck){
                    noEmptyFieldsExist = true;
                }else{
                    noEmptyFieldsExist = false;
                }

                if (validUserEmail && validPhoneNumber && validPassword && noEmptyFieldsExist) {
                    fAuth.createUserWithEmailAndPassword(useremail.getText().toString(), userpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid(); // needs userID this returns adminID
                            Toast.makeText(RegisterUser.this, "Account approved", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("approvedUsers").document(userID); // technically admin ID
                            Map<String, Object> userInfo = new HashMap<>();
                            if (isDentistBox.isChecked()) {
                                userInfo.put("FullName", userfullName.getText().toString());
                                userInfo.put("UserEmail", useremail.getText().toString());
                                userInfo.put("Password", userpassword.getText().toString());
                                userInfo.put("PhoneNumber", userphone.getText().toString());
                                userInfo.put("UserType", "Dentist");

                                finish();
                                df.set(userInfo);
                            }
                            if (isPatientBox.isChecked()) {
                                userInfo.put("FullName", userfullName.getText().toString());
                                userInfo.put("UserEmail", useremail.getText().toString());
                                userInfo.put("PhoneNumber", userphone.getText().toString());
                                userInfo.put("UserType", "Patient");

                                finish();
                                df.set(userInfo);
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterUser.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
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
        String Email = useremail.getText().toString();
        String emailRegex = "[\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";

        //check for whitespace
        for(char currentChar : Email.toCharArray()){
            isSpaceUserReg = Character.isWhitespace(currentChar);
        }
        Pattern user_email_chars = Pattern.compile(emailRegex);
        Matcher user_email_m = user_email_chars.matcher(Email);
        if(user_email_m.matches() && !isSpaceUserReg){
            validUserEmail = true;
        }else{
            validUserEmail = false;
        }

    }

    @SuppressLint("ResourceType")
    public void passwordCheck () {
        String Password = userpassword.getText().toString();
        int PassLength = Password.length();
        //8 characters
        if(PassLength >= 8 && PassLength <= 15 && !isSpaceUserReg){
            isAtLeast8UserReg= true;
            cardOneUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            isAtLeast8UserReg = false;
            cardOneUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));

        }
        //check for whitespace
        for(char currentChar : Password.toCharArray()){
            isSpaceUserReg = Character.isWhitespace(currentChar);
        }


        //for uppercase
        Pattern upper = Pattern.compile("(.*[A-Z].*)");
        Matcher upper_m = upper.matcher(Password);
        if(upper_m.matches()){
            hasUpperCaseUserReg = true;
            cardTwoUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasUpperCaseUserReg = false;
            cardTwoUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for number
        Pattern number = Pattern.compile("(.*[0-9].*)");
        Matcher number_m = number.matcher(Password);
        if(number_m.matches()){
            hasNumberUserReg =true;
            cardThreeUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasNumberUserReg = false;
            cardThreeUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for symbol
        Pattern symbol = Pattern.compile("[a-zA-Z0-9]*");
        Matcher symbol_m = symbol.matcher(Password);
        System.out.println(symbol_m);
        if(!symbol_m.matches() && !isSpaceUserReg){
            hasSymbolUserReg=true;
            cardFourUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }else{
            hasSymbolUserReg =false;
            cardFourUserReg.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        if(isAtLeast8UserReg && hasUpperCaseUserReg && hasNumberUserReg && hasSymbolUserReg && !isSpaceUserReg){
            validPassword = true;
        }else{
            validPassword = false;
        }

    }

    public void phoneNumberCheck(){
    String PhoneNumber = userphone.getText().toString();
        int PhoneNumberLength = PhoneNumber.length();

        for (char currentChar : PhoneNumber.toCharArray()) {
            isSpaceUserReg = Character.isWhitespace(currentChar);
        }

        if (PhoneNumberLength == 11 && !isSpaceUserReg) {
            validPhoneNumber = true;

        } else {
            validPhoneNumber = false;

        }
    }


    private void inputChange(){
        userpassword.addTextChangedListener(new TextWatcher() {
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

        useremail.addTextChangedListener(new TextWatcher() {
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

        userphone.addTextChangedListener(new TextWatcher() {
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

    /*private void ReadAccountRequest() {
        DocumentReference accReadRequests = fStore.collection("Users").document("xCeNM6wvdIQAUBCQ9LDkc7LwXWr2"); //arraylist reference needed
        accReadRequests.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    userfullName.setText(documentSnapshot.getString("FullName"));
                    useremail.setText(documentSnapshot.getString("UserEmail"));
                    userpassword.setText(documentSnapshot.getString("Password"));
                    userphone.setText(documentSnapshot.getString("PhoneNumber"));

                    if(documentSnapshot.getString("UserType").equals("Patient")){
                        isPatientBox.setChecked(true);
                    }
                    if(documentSnapshot.getString("UserType").equals("Dentist")){
                        isDentistBox.setChecked(true);
                    }
                }
            }
        });
    }*/
}



