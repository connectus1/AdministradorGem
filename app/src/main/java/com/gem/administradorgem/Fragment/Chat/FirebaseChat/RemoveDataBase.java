package com.gem.administradorgem.Fragment.Chat.FirebaseChat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RemoveDataBase extends Service {
    private DatabaseReference reference;
    private ArrayList<String> keys;
    private CharSequence matricula;

    public RemoveDataBase() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        reference = FirebaseDatabase.getInstance("https://gem360.firebaseio.com/").getReference();

        if (intent != null) {
            keys = intent.getStringArrayListExtra("key");
            matricula = intent.getStringExtra("matricula");
        }

        if (keys != null) {
            for (String key : keys) {
                reference.child("Chat/" + matricula + "/" + key).removeValue();
            }
        }

        onDestroy();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        reference = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
