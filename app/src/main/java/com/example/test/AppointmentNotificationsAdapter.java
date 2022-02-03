package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AppSchedule.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userAppList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PatientName, PatientEmail, AppointmentType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PatientName = itemView.findViewById(R.id.tvPatientName);
            PatientEmail = itemView.findViewById(R.id.tvPatientEmail);
            AppointmentType = itemView.findViewById(R.id.tvAppointmentType);
        }
    }
}
