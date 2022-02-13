package com.example.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountNotificationsAdapter extends RecyclerView.Adapter<AccountNotificationsAdapter.MyViewHolder> {

    Context context;

    ArrayList<UsersData> list;

    public AccountNotificationsAdapter(Context context, ArrayList<UsersData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_notify_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        UsersData user = list.get(position);
        holder.fullName.setText(user.FullName);
        holder.UserEmail.setText(user.UserEmail);
        holder.UserType.setText(user.UserType);
        holder.PhoneNumber.setText(user.PhoneNumber);
        holder.Password.setText(user.Password);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1300)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.txtFullName);
                EditText email = view.findViewById(R.id.txtEmailAddress);
                EditText password = view.findViewById(R.id.txtPassword);
                EditText phone = view.findViewById(R.id.txtPhoneNumber);
                EditText userType = view.findViewById(R.id.txtUserType);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(user.getFullname());
                email.setText(user.getUserEmail());
                password.setText(user.getPassword());
                phone.setText(user.getPhoneNumber());
                userType.setText(user.getUserType());

                dialogPlus.show();

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.fullName.getContext());
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Deleted accounts can't be recovered.");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference dfDeleteBtn = FirebaseFirestore.getInstance().collection("Users").document(email.getText().toString());
                                DocumentReference accountRequestsRefBtn = FirebaseFirestore.getInstance().collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");


                                dfDeleteBtn.delete();
                                accountRequestsRefBtn.update("NumOfAccRequests", FieldValue.increment(-1));
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(holder.fullName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference dfApprove = FirebaseFirestore.getInstance().collection("approvedUsers").document(email.getText().toString());
                        DocumentReference dfDelete = FirebaseFirestore.getInstance().collection("Users").document(email.getText().toString());
                        DocumentReference accountRequestsRef = FirebaseFirestore.getInstance().collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("FullName", name.getText().toString());
                                    map.put("UserEmail", email.getText().toString());
                                    map.put("Password", password.getText().toString());
                                    map.put("PhoneNumber", phone.getText().toString());
                                    map.put("UserType", userType.getText().toString());
                                    dfApprove.set(map);
                                    ////push userID ?
                                    dfDelete.delete();

                                    accountRequestsRef.update("NumOfAccRequests", FieldValue.increment(-1));


                                    Toast.makeText(holder.fullName.getContext(), "Account Approved", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            }
                        });

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, UserEmail, UserType, Password, PhoneNumber;
        Button btnEdit, btnDelete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.tvFullName);
            UserEmail = itemView.findViewById(R.id.tvUserEmail);
            UserType = itemView.findViewById(R.id.tvUserType);
            Password = itemView.findViewById(R.id.tvPassword);
            PhoneNumber = itemView.findViewById(R.id.tvPhoneNum);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

        }
    }


    }
