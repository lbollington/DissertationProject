package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
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

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AppUsersData user = userAppList.get(position);
        holder.PatientEmail.setText(user.PatientEmail);
        holder.AppointmentType.setText(user.AppointmentType);
        holder.Location.setText(user.Location);
        holder.Dentist.setText(user.Dentist);
        holder.Day.setText(user.Day);
        holder.Month.setText(user.Month);
        holder.Year.setText(user.Year);
        holder.Hour.setText(user.Hour);
        holder.Mins.setText(user.Mins);

        holder.btnAppEdit.setOnClickListener(v -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.itemView.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup_app))
                    .setExpanded(true, 1900)
                    .create();

            View view = dialogPlus.getHolderView();

            EditText emailPatient = view.findViewById(R.id.txtPatientEmailAddress);
            EditText appType = view.findViewById(R.id.txtAppType);
            EditText appDentist = view.findViewById(R.id.txtAppDentist);
            EditText location = view.findViewById(R.id.txtLocation);


            EditText day = view.findViewById(R.id.txtDay);
            EditText month = view.findViewById(R.id.txtMonth);
            EditText year = view.findViewById(R.id.txtYear);
            EditText hour = view.findViewById(R.id.txtHour);
            EditText mins = view.findViewById(R.id.txtMins);

            Button btnAppUpdate = view.findViewById(R.id.btnAppUpdate);

            emailPatient.setText(user.getPatientEmail());
            appType.setText(user.getAppointmentType());
            appDentist.setText(user.getDentist());
            location.setText(user.getLocation());
            day.setText(user.getDay());
            month.setText(user.getMonth());
            year.setText(user.getYear());
            hour.setText(user.getHour());
            mins.setText(user.getMins());

            dialogPlus.show();

            holder.btnAppDelete.setOnClickListener(v1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.PatientEmail.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted appointments can't be recovered.");

                builder.setPositiveButton("Delete", (dialog, which) -> {
                    DocumentReference dfDeleteAppBtn = FirebaseFirestore.getInstance().collection("appointmentRequests").document(emailPatient.getText().toString());
                    DocumentReference appRequestsRefBtn = FirebaseFirestore.getInstance().collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");

                    dfDeleteAppBtn.delete();
                    appRequestsRefBtn.update("NumOfAppRequests", FieldValue.increment(-1));

                });

                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    //Toast.makeText(holder.fullName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            });

            btnAppUpdate.setOnClickListener(v12 -> {
                DocumentReference dfAppApprove = FirebaseFirestore.getInstance().collection("approvedAppointments").document(emailPatient.getText().toString());
                DocumentReference dfAppDelete = FirebaseFirestore.getInstance().collection("appointmentRequests").document(emailPatient.getText().toString());
                DocumentReference appRequestsRef = FirebaseFirestore.getInstance().collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");

                Map<String, Object> map = new HashMap<>();
                //map.put("PatientName", namePatient.getText().toString());
                map.put("PatientEmail", emailPatient.getText().toString());
                map.put("AppointmentType", appType.getText().toString());
                map.put("Location", location.getText().toString());
                map.put("Dentist", appDentist.getText().toString());

                map.put("Day", day.getText().toString());
                map.put("Month", month.getText().toString());
                map.put("Year", year.getText().toString());
                map.put("Hour", hour.getText().toString());
                map.put("Mins", mins.getText().toString());

                dfAppApprove.set(map);
                dfAppDelete.delete();

                appRequestsRef.update("NumOfAppRequests", FieldValue.increment(-1));


                Toast.makeText(holder.PatientEmail.getContext(), "Appointment Approved", Toast.LENGTH_SHORT).show();
                dialogPlus.dismiss();

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, appType.getText().toString());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, appType.getText().toString());
                Calendar startTime = Calendar.getInstance();
                startTime.set(Integer.parseInt(year.getText().toString()), Integer.parseInt(month.getText().toString())-1, Integer.parseInt(day.getText().toString()), Integer.parseInt(hour.getText().toString()), Integer.parseInt(mins.getText().toString()));
                Calendar endTime = Calendar.getInstance();
                endTime.set(Integer.parseInt(year.getText().toString()), Integer.parseInt(month.getText().toString())-1, Integer.parseInt(day.getText().toString()), Integer.parseInt(hour.getText().toString())+1, Integer.parseInt(mins.getText().toString()));
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailPatient.getText().toString(), appDentist.getText().toString()});

                if (intent.resolveActivity(contextApp.getPackageManager()) != null){
                    contextApp.startActivity(intent);
                }else{
                    Toast.makeText(holder.PatientEmail.getContext(), "There is no app that can support this action", Toast.LENGTH_SHORT).show();
                }

            });

        });

    }

    @Override
    public int getItemCount() {
        return userAppList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PatientEmail, AppointmentType, Location, Dentist, Day, Month, Year, Hour, Mins;
        Button btnAppEdit, btnAppDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            PatientEmail = itemView.findViewById(R.id.tvPatientEmail);
            AppointmentType = itemView.findViewById(R.id.tvAppointmentType);
            Location = itemView.findViewById(R.id.tvLocation);
            Dentist = itemView.findViewById(R.id.tvAppDentist);



            Day = itemView.findViewById(R.id.tvDay);
            Month = itemView.findViewById(R.id.tvMonth);
            Year = itemView.findViewById(R.id.tvYear);
            Hour = itemView.findViewById(R.id.tvHour);
            Mins = itemView.findViewById(R.id.tvMins);

            btnAppEdit = (Button) itemView.findViewById(R.id.btnAppEdit);
            btnAppDelete = (Button) itemView.findViewById(R.id.btnAppDelete);


        }
    }
}
