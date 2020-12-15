package com.gem.administradorgem.Fragment.Chat.FirebaseChat;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gem.administradorgem.Fragment.Chat.Adapter.ChatAdapter;
import com.gem.administradorgem.Fragment.Chat.Adapter.itemChat;
import com.gem.administradorgem.Fragment.Chat.DatabaseSQL.DatabaseSql;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseMessage {

    private DatabaseReference ref;
    private ChatAdapter adapter;
    private Activity activity;

    private ArrayList<String> keyEliminar;
    private String matricula;

    private DatabaseSql databaseSql;

    public FirebaseMessage(String matricula, Activity activity) {
        this.ref = FirebaseDatabase.getInstance("https://gem360.firebaseio.com/")
                .getReference("Chat/" + matricula);

        this.activity = activity;
        keyEliminar = new ArrayList<>();
        this.matricula = matricula;

        databaseSql = new DatabaseSql(matricula, activity);
    }

    public void destroy() {
        ref.removeEventListener(listener);
        ref = null;
    }

    public void enviarMensaje(itemChat mensaje) {
        ref.push().setValue(mensaje);

    }

    public void getMensaje(ChatAdapter adapter) {
        this.adapter = adapter;
        this.ref.addChildEventListener(listener);
    }


    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            itemChat mensaje = snapshot.getValue(itemChat.class);
            if (!mensaje.getNombre().equals("GEM")) {
                adapter.addMensaje(mensaje);

                databaseSql.registrarMensaje(mensaje);
                keyEliminar.add(snapshot.getKey());

                //Manda a llamar un IntentService que elimina los mensajes recibidos
                Intent i = new Intent(activity, RemoveDataBase.class);
                i.putStringArrayListExtra("key", keyEliminar);
                i.putExtra("matricula", matricula);
                activity.startService(i);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}
