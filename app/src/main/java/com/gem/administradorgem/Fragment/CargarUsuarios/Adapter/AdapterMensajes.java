package com.gem.administradorgem.Fragment.CargarUsuarios.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gem.administradorgem.Fragment.Chat.ChatGEM;
import com.gem.administradorgem.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.MensajesViewHolder>{
    private List<Mensaje> mensajeList;
    private Activity activity;
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public AdapterMensajes(Activity activity) {
        this.activity = activity;
        mensajeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MensajesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_mensaje_fragment,null);
        return new MensajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesViewHolder holder, final int position) {
        holder.txtMensaje.setText(mensajeList.get(position).getMensaje());
        holder.txtNombre.setText(mensajeList.get(position).getNombre());

        holder.lnContenedor.setLayoutParams(params);
        holder.lnContenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ChatGEM.class);
                i.putExtra("matricula", mensajeList.get(position).getId());

                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mensajeList.size();
    }

    public void addMensaje(Mensaje mensaje){
        mensajeList.add(mensaje);
        notifyItemInserted(mensajeList.size());
    }

    public void removeMensaje(Mensaje mensaje){
        for (int i =0;i<mensajeList.size();i++){
            if (mensajeList.get(i).getId().equals(mensaje.getId())){
                mensajeList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }


    }

    static class MensajesViewHolder extends RecyclerView.ViewHolder{
        protected TextView txtNombre;
        protected TextView txtMensaje;
        protected LinearLayout lnContenedor;

        public MensajesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtName);
            lnContenedor = itemView.findViewById(R.id.lnUsuario);

            txtMensaje = itemView.findViewById(R.id.txtMensaje);
        }
    }


}
