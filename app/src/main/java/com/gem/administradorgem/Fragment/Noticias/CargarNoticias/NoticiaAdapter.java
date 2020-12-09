package com.gem.administradorgem.Fragment.Noticias.CargarNoticias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gem.administradorgem.Fragment.Noticias.ItemNoticia;
import com.gem.administradorgem.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {
    protected Activity activity;
    private List<ItemNoticia> noticias;

    public NoticiaAdapter(Activity activity) {
        this.activity = activity;
        noticias = new ArrayList<>();
    }

    public void addNoticia(ItemNoticia noticia){
        noticias.add(0,noticia);
        notifyItemInserted(0);

    }

    public void delete(){
        noticias.clear();
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticiaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, final int position) {
        holder.setInfo(noticias.get(position), activity.getBaseContext());
        holder.lnContenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrearDialog(noticias.get(position).getDescripcion(),
                        noticias.get(position).getUrlAlmacenamiento(),noticias.get(position).getTitulo())
                        .show();
            }
        });

        holder.txtHora.setText(noticias.get(position).getFecha());

    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitulo;
        protected CircleImageView imgNoticia;
        protected TextView txtHora;
        protected LinearLayout lnContenedor;

        public NoticiaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloNoticia);
            imgNoticia = itemView.findViewById(R.id.imgNoticia);
            txtHora = itemView.findViewById(R.id.txtHoraNoticia);
            lnContenedor = itemView.findViewById(R.id.lnContenedor);

        }

        void setInfo(ItemNoticia noticia, Context context) {
            txtTitulo.setText(noticia.getTitulo());
            Glide.with(context).load(Uri.parse(noticia.getUrlAlmacenamiento())).into(imgNoticia);
        }

    }


    private AlertDialog CrearDialog(String descripcion, String urlImagen, String titulo){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_noticias,null);

        ImageView imagen = view.findViewById(R.id.imgNoticia);
        Glide.with(view).load(urlImagen).into(imagen);

        String web = "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" + "<meta charset=\"utf-8\">" + "</head>" +
                "<body>" +
                "<h3>" + titulo.toString() +"</h3>" +
                "<p style=\"text-align:justify;\">" + agregarSaltos(descripcion) + "<br><br><br><br></p>" +
                "</body>" +
                "</html>";
        WebView mWebView = (WebView) view.findViewById(R.id.webNoticia);
        mWebView.loadData(web, "text/html", "utf-8");

        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }

    private String agregarSaltos(String descripcion){
        String nuevo="";
        nuevo = descripcion.replace("\n","<br>");
        return nuevo;
    }


}