package com.kevin.android_example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private AppCompatTextView mainTv;
    private final static int HANDLER_MESSAGE_CASE_TIME = 101;
    private MainHandler aMainHandler;
    private MainHandler bMainHandler;

    private static class MainHandler extends Handler {

        private final WeakReference<MainActivity> activityWeakReference;


        public MainHandler(MainActivity mainActivity) {
            super();
            activityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            MainActivity mainActivity = activityWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MESSAGE_CASE_TIME:
                    String message = (String) msg.obj;
                    String format = String.format("%s  hash：%s", message, hashCode());
                    mainActivity.mainTv.setText(format);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.but).setOnClickListener(v -> {

            new Thread(() -> {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "来自子线程的Toast", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }).start();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(6000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    if (aMainHandler != null) {
//                        Message message = Message.obtain();
//                        message.what = HANDLER_MESSAGE_CASE_TIME;
//                        message.obj = String.format("A时间戳：%s", System.currentTimeMillis());
//                        aMainHandler.sendMessage(message);
//                    }
//                }
//            }).start();


        });
        mainTv = findViewById(R.id.main_tv);
        bMainHandler = new MainHandler(this);
        aMainHandler = new MainHandler(this);
        Log.d("MainActivity", "a：" + aMainHandler.hashCode());
        Log.d("MainActivity", "b：" + bMainHandler.hashCode());
    }
}