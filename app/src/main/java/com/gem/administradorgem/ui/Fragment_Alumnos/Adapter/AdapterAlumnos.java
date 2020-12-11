package com.gem.administradorgem.ui.Fragment_Alumnos.Adapter;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAlumnos extends RecyclerView.Adapter<AdapterAlumnos.AlumnosViewHolder>{

    private List<Alumno> alumnoList;
    private Activity activity;

    public AdapterAlumnos(Activity activity) {
        this.activity = activity;
        alumnoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AlumnosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_mensaje_fragment, parent, false);
        return new AlumnosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlumnosViewHolder holder, final int position) {
        holder.pin.setVisibility(View.INVISIBLE);

        holder.txtName.setText(alumnoList.get(position).getNombre());
        holder.txtMatricula.setText(alumnoList.get(position).getMatricula());

        holder.lnContentenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ChatGEM.class);
                i.putExtra("matricula",alumnoList.get(position).getId());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alumnoList.size();
    }

    public void addAlumno(Alumno alumno){
        alumnoList.add(alumno);
        notifyItemInserted(alumnoList.size());
    }

    public void deleteAlumno(){
        int n = getItemCount();

        alumnoList.clear();
        notifyItemRangeRemoved(0,n);
    }


    static class AlumnosViewHolder extends RecyclerView.ViewHolder{

        protected TextView txtName;
        protected TextView txtMatricula;
        protected LinearLayout lnContentenedor;
        protected CircleImageView pin;

        public AlumnosViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            lnContentenedor = itemView.findViewById(R.id.lnUsuario);

            txtMatricula = itemView.findViewById(R.id.txtMensaje);
            pin = itemView.findViewById(R.id.pinChat);
        }
    }

}
