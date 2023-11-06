package com.example.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PasswordRequestNotificationsAdapter extends RecyclerView.Adapter<PasswordRequestNotificationsAdapter.MyViewHolder> {

    Context contextPass;
    ArrayList<PassRequestData> passRequestList;

    public PasswordRequestNotificationsAdapter(Context contextApp, ArrayList<PassRequestData> passRequestList) {
        this.contextPass = contextApp;
        this.passRequestList = passRequestList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vPass = LayoutInflater.from(contextPass).inflate(R.layout.activity_notify_pass_request_item, parent, false);

        return new MyViewHolder(vPass);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PassRequestData user = passRequestList.get(position);
        holder.ResetName.setText(user.ResetName);
        holder.UserEmail.setText(user.UserEmail);
        holder.PassRequest.setText(user.PassRequest);

        holder.btnPassEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup_pass_request))
                        .setExpanded(true, 900)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText resetName = view.findViewById(R.id.txtUserName);
                EditText userEmail = view.findViewById(R.id.txtUserEmailAddress);
                EditText passRequest = view.findViewById(R.id.txtPassRequest);
                Button btnPassUpdate = view.findViewById(R.id.btnPassUpdate);

                resetName.setText(user.getResetName());
                userEmail.setText(user.getUserEmail());
                passRequest.setText(user.getPassRequest());

                dialogPlus.show();

                holder.btnPassDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.ResetName.getContext());
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Deleted password requests can't be recovered.");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference dfDeletePassBtn = FirebaseFirestore.getInstance().collection("passwordResetRequests").document(userEmail.getText().toString());
                                DocumentReference passRequestsRefBtn = FirebaseFirestore.getInstance().collection("passwordResetRequestsNumber").document("qhm5HzQYgG897w9EVuqZ");

                                dfDeletePassBtn.delete();
                                passRequestsRefBtn.update("NumOfPassRequests", FieldValue.increment(-1));

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

                btnPassUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference dfPassApprove = FirebaseFirestore.getInstance().collection("approvedUsers").document(userEmail.getText().toString());
                        DocumentReference dfPassDelete = FirebaseFirestore.getInstance().collection("passwordResetRequests").document(userEmail.getText().toString());
                        DocumentReference passRequestsRef = FirebaseFirestore.getInstance().collection("passwordResetRequestsNumber").document("qhm5HzQYgG897w9EVuqZ");

                        //delete fAuth record
                        /*
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail.getText().toString(), passRequest.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {*/
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Password", passRequest.getText().toString());
                                    dfPassApprove.update(map);
                                    ////push userID ?
                                    dfPassDelete.delete();

                                    passRequestsRef.update("NumOfPassRequests", FieldValue.increment(-1));

                                    Toast.makeText(holder.ResetName.getContext(), "Password Approved", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                             //   }
                            }
                        });


                    }

                });

            }

       // });

   // }

    @Override
    public int getItemCount() {
        return passRequestList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ResetName, UserEmail, PassRequest;
        Button btnPassEdit, btnPassDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ResetName = itemView.findViewById(R.id.tvResetUserName);
            UserEmail = itemView.findViewById(R.id.tvResetUserEmail);
            PassRequest = itemView.findViewById(R.id.tvPassRequest);

            btnPassEdit = itemView.findViewById(R.id.btnPassEdit);
            btnPassDelete = itemView.findViewById(R.id.btnPassDelete);

        }
    }
}
