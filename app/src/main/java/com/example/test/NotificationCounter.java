package com.example.test;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NotificationCounter {
    TextView notificationNumber;

    private final int maximum = 99;
    private int notification_number_counter = 0;

    public NotificationCounter(View view){
        notificationNumber = view.findViewById(R.id.notificationNumber);
    }


    public void increaseNum(){
        notification_number_counter += notification_number_counter;

        if(notification_number_counter > maximum){
            Log.d("Counter", "Maximum number reached");
        }else{
            notificationNumber.setText(String.valueOf(notification_number_counter));
        }
    }

}
