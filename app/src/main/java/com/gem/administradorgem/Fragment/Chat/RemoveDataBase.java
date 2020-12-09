package com.gem.administradorgem.Fragment.Chat;

import android.app.Service;
import android.content.Context;
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
        int index = getSharedPreferences("ADMIN", Context.MODE_PRIVATE).getInt("index",0);
        String url[] = new String[]{
                /*Mante*/"https://conalep360-mante.firebaseio.com/",
                /*Matamoros*/"https://conalep360-matamoros.firebaseio.com/",
                /*Miguel Aleman*/"https://conalep360-miguelaleman.firebaseio.com/",
                /*Nuevo Laredo*/"https://conalep360-nuevolaredo.firebaseio.com/",
                /*Reynosa*/"https://conalep360-reynosa.firebaseio.com/",
                /*Rio Bravo*/"https://conalep360-riobravo.firebaseio.com/",
                /*Tampico*/"https://conalep360-tampico.firebaseio.com/",
                /*Victoria*/"https://conalep360-victoria.firebaseio.com/",
                /*Cast*/"https://conalep360-cast.firebaseio.com/"
        };
        reference = FirebaseDatabase.getInstance(url[index]).getReference();

        if (intent != null) {
            keys = intent.getStringArrayListExtra("key");
            matricula = intent.getStringExtra("matricula");
        }

        if (keys != null) {
            for (String key : keys) {
                reference.child("Chat/-"+matricula +"/"+key).removeValue();
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
