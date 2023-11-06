package com.example.test;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SessionTimeout extends AppCompatActivity {
    private LogoutListener listener;

    public void startUserSession(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.onSessionLogout();

            }
        }, 5000);
    }

    public void registerSessionListener(LogoutListener listener) {
        this.listener = listener;
    }
}
