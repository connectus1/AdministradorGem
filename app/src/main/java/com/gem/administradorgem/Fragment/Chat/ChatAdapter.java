package com.gem.administradorgem.Fragment.Chat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.gem.administradorgem.R;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<itemChat> listMensajes = new ArrayList<>();
    private Context context;
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public ChatAdapter(Context context) {

        this.context = context;
    }
    public void addMensaje(itemChat mensaje) {
        listMensajes.add(mensaje);
        notifyItemInserted(listMensajes.size());

    }

    public void addMensajeDatabase(itemChat mensaje) {
        listMensajes.add(0, mensaje);
        notifyItemInserted(0);

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mensaje,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.txtMensaje.setText(listMensajes.get(position).getMensaje());
        holder.contendor.setLayoutParams(params);

        holder.imgChatAlumno.setVisibility(View.INVISIBLE);
        holder.imgChatEscuela.setVisibility(View.INVISIBLE);

        holder.cardMensaje.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.verde)));

        if (listMensajes.get(position) != null) {
            if (listMensajes.get(position).isId()) {
                holder.imgChatAlumno.setVisibility(View.VISIBLE);
            } else {
                holder.imgChatEscuela.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.drawable.logo_gem).into(holder.imgChatEscuela);
                holder.cardMensaje.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.blanco)));
                holder.txtMensaje.setTextColor(Color.parseColor("#2B2B2B"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return listMensajes.size();
    }

    protected static class ChatViewHolder extends RecyclerView.ViewHolder{

        private TextView txtMensaje;
        private LottieAnimationView imgChatAlumno;
        private ImageView imgChatEscuela;
        private LinearLayout contendor;
        private CardView cardMensaje;


        public ChatViewHolder(View itemView) {
            super(itemView);
            txtMensaje = (TextView) itemView.findViewById(R.id.txtMensaje);
            imgChatAlumno = (LottieAnimationView) itemView.findViewById(R.id.imgChatAlumno);
            imgChatEscuela = (ImageView) itemView.findViewById(R.id.imgChatEscuela);
            contendor = (LinearLayout) itemView.findViewById(R.id.lnContenedor);
            cardMensaje = (CardView) itemView.findViewById(R.id.cardMensaje);

        }
    }
}
