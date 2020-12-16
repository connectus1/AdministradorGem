package com.gem.administradorgem.Fragment.Noticias;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gem.administradorgem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FirebaseNoticia {

    private DatabaseReference reference;
    private StorageReference mStorageRef;
    private Activity activity;

    private AlertDialog dialog;

    public FirebaseNoticia(Activity activity) {
        reference = FirebaseDatabase.getInstance("https://gem360.firebaseio.com/").getReference();
        mStorageRef = FirebaseStorage.getInstance("gs://gem360/").getReference();

        this.activity = activity;
        this.dialog = dialog();
    }

    public void destroy() {
        dialog = null;
        reference = null;
        mStorageRef = null;
    }

    private AlertDialog dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_cargando, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public void subirNoticia(ItemNoticia noticia, Uri uri) {
        dialog.show();
        subirImagen(noticia, uri);
    }

    private void subirImagen(final ItemNoticia itemNoticia, Uri uri) {
        mStorageRef.child(BottomSheetNoticias.nivel + "/"
                + BottomSheetNoticias.grupo + "/" + BottomSheetNoticias.grado + "/" + itemNoticia.getTitulo())
                .putFile(uri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
            itemNoticia.setUrl(task.getResult().toString());

            itemNoticia.setNivel(BottomSheetNoticias.nivel);
            itemNoticia.setGrado(BottomSheetNoticias.grado);
            itemNoticia.setGrupo(BottomSheetNoticias.grupo);

            reference.child("noticias").push().setValue(itemNoticia);

            dialog.dismiss();
            destroy();

            activity.finish();

        }).addOnFailureListener(e -> {
            Toast.makeText(activity, "Ha ocurrido un error al cargar la noticia", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }));
    }

}
