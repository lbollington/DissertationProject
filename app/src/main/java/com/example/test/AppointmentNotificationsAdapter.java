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

public class AppointmentNotificationsAdapter extends RecyclerView.Adapter<AppointmentNotificationsAdapter.MyViewHolder> {

    Context contextApp;
    ArrayList<AppUsersData> userAppList;

    public AppointmentNotificationsAdapter(Context contextApp, ArrayList<AppUsersData> userAppList) {
        this.contextApp = contextApp;
        this.userAppList = userAppList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vApp = LayoutInflater.from(contextApp).inflate(R.layout.activity_notify_app_item, parent, false);

        return new MyViewHolder(vApp);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AppUsersData user = userAppList.get(position);
        holder.PatientName.setText(user.PatientName);
        holder.PatientEmail.setText(user.PatientEmail);
        holder.AppointmentType.setText(user.AppointmentType);
        holder.Location.setText(user.Location);
        holder.Dentist.setText(user.Dentist);
        holder.AppointmentTime.setText(user.AppointmentTime);

        holder.btnAppEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup_app))
                        .setExpanded(true, 1500)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText namePatient = view.findViewById(R.id.txtPatientName);
                EditText emailPatient = view.findViewById(R.id.txtPatientEmailAddress);
                EditText appType = view.findViewById(R.id.txtAppType);
                EditText appDentist = view.findViewById(R.id.txtAppDentist);
                EditText location = view.findViewById(R.id.txtLocation);
                EditText AppointmentTime = view.findViewById(R.id.txtAppointmentTime);

                Button btnAppUpdate = view.findViewById(R.id.btnAppUpdate);

                namePatient.setText(user.getPatientname());
                emailPatient.setText(user.getPatientEmail());
                appType.setText(user.getAppointmentType());
                appDentist.setText(user.getDentist());
                location.setText(user.getLocation());
                AppointmentTime.setText(user.getAppointmentTime());

                dialogPlus.show();

                holder.btnAppDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.PatientName.getContext());
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Deleted appointments can't be recovered.");

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference dfDeleteAppBtn = FirebaseFirestore.getInstance().collection("appointmentRequests").document(emailPatient.getText().toString());
                                DocumentReference appRequestsRefBtn = FirebaseFirestore.getInstance().collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");

                                dfDeleteAppBtn.delete();
                                appRequestsRefBtn.update("NumOfAppRequests", FieldValue.increment(-1));

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

                btnAppUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference dfAppApprove = FirebaseFirestore.getInstance().collection("approvedAppointments").document(emailPatient.getText().toString());
                        DocumentReference dfAppDelete = FirebaseFirestore.getInstance().collection("appointmentRequests").document(emailPatient.getText().toString());
                        DocumentReference appRequestsRef = FirebaseFirestore.getInstance().collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");

                        Map<String, Object> map = new HashMap<>();
                        map.put("PatientName", namePatient.getText().toString());
                        map.put("PatientEmail", emailPatient.getText().toString());
                        map.put("AppointmentType", appType.getText().toString());
                        map.put("Location", location.getText().toString());
                        map.put("Dentist", appDentist.getText().toString());
                        map.put("AppointmentTime", AppointmentTime.getText().toString());
                        dfAppApprove.set(map);
                        ////push userID ?
                        dfAppDelete.delete();

                        appRequestsRef.update("NumOfAppRequests", FieldValue.increment(-1));


                        Toast.makeText(holder.PatientName.getContext(), "Account Approved", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();

                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setData(CalendarContract.Events.CONTENT_URI);
                        intent.putExtra(CalendarContract.Events.TITLE, appType.getText().toString());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, appDentist.getText().toString());
                        intent.putExtra(CalendarContract.Events.ALL_DAY, true); //appointment time
                        intent.putExtra(Intent.EXTRA_EMAIL, "test@yahoo.com");

                        if (intent.resolveActivity(contextApp.getPackageManager()) != null){
                            contextApp.startActivity(intent);
                        }else{
                            Toast.makeText(holder.PatientName.getContext(), "There is no app that can support this action", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

            }

        });

    }

    @Override
    public int getItemCount() {
        return userAppList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PatientName, PatientEmail, AppointmentType, Location, Dentist, AppointmentTime;
        Button btnAppEdit, btnAppDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PatientName = itemView.findViewById(R.id.tvPatientName);
            PatientEmail = itemView.findViewById(R.id.tvPatientEmail);
            AppointmentType = itemView.findViewById(R.id.tvAppointmentType);
            Location = itemView.findViewById(R.id.tvLocation);
            Dentist = itemView.findViewById(R.id.tvAppDentist);
            AppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);

            btnAppEdit = (Button) itemView.findViewById(R.id.btnAppEdit);
            btnAppDelete = (Button) itemView.findViewById(R.id.btnAppDelete);


        }
    }
}
