package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class DentalHistoryAdapter extends RecyclerView.Adapter<DentalHistoryAdapter.MyViewHolder> {
    Context contextDoc;
    ArrayList<DentalHistoryData> documentList;

    public DentalHistoryAdapter(Context contextDoc, ArrayList<DentalHistoryData> documentList) {
        this.contextDoc = contextDoc;
        this.documentList = documentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vDoc = LayoutInflater.from(contextDoc).inflate(R.layout.activity_dental_history_item, parent, false);
                                                                            //onCreate method to set content view
        return new MyViewHolder(vDoc);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DentalHistoryData document = documentList.get(position);
        holder.AppName.setText(document.AppName);
        holder.DentistName.setText(document.DentistName);           //holder values
        holder.Day.setText(document.Day);
        holder.Month.setText(document.Month);
        holder.Year.setText(document.Year);
        holder.imageurl.setText(document.imageurl);
        holder.PatientName.setText(document.PatientName);

        Glide.with(holder.AppName.getContext()).load(holder.imageurl.getText().toString()).into(holder.imageView);  //sets the image to the item in recyclerview

    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView AppName, DentistName, Day, Month, Year, imageurl, PatientName; //variables
        Button viewDocument;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AppName = itemView.findViewById(R.id.tvAppName);
            DentistName = itemView.findViewById(R.id.tvDentistEmail);
            Day = itemView.findViewById(R.id.tvDay);
            Month = itemView.findViewById(R.id.tvMonth);            //initialise variables
            Year = itemView.findViewById(R.id.tvYear);
            imageurl = itemView.findViewById(R.id.tvURL);
            PatientName = itemView.findViewById(R.id.tvPatientDocEmail);

            imageView = itemView.findViewById(R.id.docImageView);

        }
    }
}
