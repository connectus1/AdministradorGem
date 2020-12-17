package com.gem.administradorgem.ui.Fragment_Tutores.Adapter;

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

public class AdapterTutor extends RecyclerView.Adapter<AdapterTutor.TutorViewHolder>{
    private Activity activity;
    private List<Tutor> tutorList;

    public AdapterTutor(Activity activity) {
        this.activity = activity;
        tutorList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_usuarios, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, final int position) {
        holder.txtName.setText(tutorList.get(position).getNombre());
        holder.lnContenedor.setOnClickListener(view -> {
            Intent i = new Intent(activity, ChatGEM.class);
            i.putExtra("matricula", tutorList.get(position).getId());
            i.putExtra("nombre", tutorList.get(position).getNombre());
            activity.startActivity(i);
        });
        holder.txtCantHijos.setText("Hijos: " + tutorList.get(position).getHijos().size());
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void addTutor(Tutor tutor){
        tutorList.add(tutor);
        notifyItemInserted(tutorList.size());
    }

    public void deleteTutor(){
        int n = getItemCount();

        tutorList.clear();
        notifyItemRangeRemoved(0,n);
    }

    static class TutorViewHolder extends RecyclerView.ViewHolder{
        protected TextView txtName;
        protected LinearLayout lnContenedor;
        protected TextView txtCantHijos;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            lnContenedor = itemView.findViewById(R.id.lnUsuario);
            txtCantHijos = itemView.findViewById(R.id.txtcanthijos);

        }
    }
}
