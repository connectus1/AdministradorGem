package com.gem.administradorgem.Fragment.CargarUsuarios;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.Fragment.CargarUsuarios.Adapter.AdapterMensajes;
import com.gem.administradorgem.Fragment.CargarUsuarios.Adapter.Mensaje;
import com.gem.administradorgem.Fragment.Chat.Adapter.itemChat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class FirebaseMensaje {
    private DatabaseReference reference;
    private Activity activity;
    private AdapterMensajes adapter;

    public FirebaseMensaje(Activity activity,String url) {
        this.activity = activity;
        reference = FirebaseDatabase.getInstance(url).getReference("Chat");
    }

    public void DeleteObject(){
        reference.removeEventListener(listener);
        reference = null;
        listener = null;
    }

    public void getMensajes(AdapterMensajes adapter, RecyclerView rv){
        this.adapter = adapter;
        rv.setVisibility(View.VISIBLE);
        reference.addChildEventListener(listener);

    }

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getValue()!= null){
                Mensaje mensaje = new Mensaje();
                mensaje.setId(snapshot.getKey());

                for (DataSnapshot data : snapshot.getChildren()) {
                    itemChat chat = data.getValue(itemChat.class);

                    mensaje.setMensaje(chat.getMensaje());
                    mensaje.setNombre(chat.getNombre());

                    break;
                }

                if (!mensaje.getNombre().equals("GEM")) {
                    adapter.addMensaje(mensaje);
                    if (FragmentUsuarios.lottie.getVisibility() == View.VISIBLE) {
                        FragmentUsuarios.lottie.setVisibility(View.INVISIBLE);
                        FragmentUsuarios.lottie.pauseAnimation();

                        FragmentUsuarios.txtChat.setVisibility(View.INVISIBLE);
                    }
                }


            }

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue()!= null){

                Mensaje mensaje = new Mensaje();
                mensaje.setId(snapshot.getKey());

                for (DataSnapshot data: snapshot.getChildren()){
                    itemChat chat = data.getValue(itemChat.class);

                    mensaje.setMensaje(chat.getMensaje());
                    mensaje.setNombre(chat.getNombre());


                    break;
                }


                adapter.removeMensaje(mensaje);

            }

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    };

}
