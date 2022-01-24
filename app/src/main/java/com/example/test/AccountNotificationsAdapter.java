package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsersData user = list.get(position);
        holder.fullName.setText(user.FullName);
        holder.UserEmail.setText(user.UserEmail);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fullName, UserEmail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.tvFullName);
            UserEmail = itemView.findViewById(R.id.tvUserEmail);

        }
    }


    }
