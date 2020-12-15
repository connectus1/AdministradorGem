package com.gem.administradorgem.Fragment.Noticias.SubirNoticias;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gem.administradorgem.Fragment.Noticias.ItemNoticia;
import com.gem.administradorgem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SubirNoticia extends AppCompatActivity {

    private static boolean cambioImagen;

    private ImageButton btnImgNoticia;
    private ImageView imgNoticia;

    public static Uri uriFile;

    private TextInputLayout txtTitulo;
    private EditText txtNoticia;
    private AlertDialog alert;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_noticia);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initComponents();
        alert = dialogoPrimeraVez();

        imgNoticia.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SubirNoticia.this);
            builder.setTitle("Noticia");
            builder.setMessage("Esta a punto de eliminar la imagen seleccionada ¿Esta seguro?");
            builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {
                imgNoticia.setImageResource(R.drawable.degradado);
                btnImgNoticia.setEnabled(true);
                btnImgNoticia.setVisibility(View.VISIBLE);
                cambioImagen = false;
            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        });

    }

    public void setBtnImgNoticia(View v){
        if (ContextCompat.checkSelfPermission(SubirNoticia.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            abrirGaleria();
        }
    }

    public void setBtnEnviar(View v){
        alert.show();
        SubirFirebase subirnotica = new SubirFirebase(v);
        subirnotica.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        deleteComponents();
        super.onDestroy();
    }

    //El metodo se usa para incializar los compoenentes
    private void initComponents() {
        btnImgNoticia = findViewById(R.id.btnImagenNoticia);
        imgNoticia = findViewById(R.id.imgNoticia);

        txtTitulo = findViewById(R.id.txtTituloNoticia);
        txtNoticia = findViewById(R.id.txtDescripcionNoticia);

        mStorageRef = FirebaseStorage.getInstance("gs://gem360/").getReference();

    }

    //Elimina todas la resferencias del objetos utilizados
    private void deleteComponents(){
        btnImgNoticia.setImageDrawable(null);
        btnImgNoticia = null;
        imgNoticia.setImageDrawable(null);
        imgNoticia = null;


        cambioImagen = false;
        System.gc();
    }

    //Metodo utilizado para abrir la galeria
    public void abrirGaleria(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/");
        startActivityForResult(gallery.createChooser(gallery,"Seleccione la Aplicacion"), 100);
    }

    public void cargarImagen(){
        /*Deshabilita el boton de imagen y lo hace invisible*/
        btnImgNoticia.setEnabled(false);
        btnImgNoticia.setVisibility(View.INVISIBLE);

        Glide.with(this).load(uriFile).into(imgNoticia);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uriFile = data.getData();
            cambioImagen = true;
            cargarImagen();
        }
    }

    private AlertDialog dialogoPrimeraVez() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SubirNoticia.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialogo_cargando, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    class SubirFirebase extends Thread {

        private DatabaseReference databaseReference;
        private View view;
        public SubirFirebase(View view) {
            this.view = view;
            databaseReference = FirebaseDatabase.getInstance("https://gem360.firebaseio.com/")
                    .getReference("noticias");
        }

        @Override
        public void run() {
            super.run();
            if (!TextUtils.isEmpty(txtTitulo.getEditText().getText())){
                if (!TextUtils.isEmpty(txtNoticia.getText())){

                    final String[] datos = obtenerDatos();
                    if (cambioImagen){
                        subirNoticia(datos);
                    }else {
                        alert.dismiss();
                        Snackbar.make(view,"Imagen Error", Snackbar.LENGTH_LONG).show();
                        Thread.interrupted();
                    }
                }else{
                    alert.dismiss();
                    Snackbar.make(view,"Datos Incompletos", Snackbar.LENGTH_LONG).show();
                    Thread.interrupted();
                }
            }else{
                alert.dismiss();
                Snackbar.make(view,"Datos Incompletos", Snackbar.LENGTH_LONG).show();
                Thread.interrupted();
            }
            Thread.interrupted();
        }


        private void subirNoticia(String datos[]) {
            ItemNoticia itemNoticia = new ItemNoticia();

            itemNoticia.setTitulo(datos[0]);
            itemNoticia.setDescripcion(datos[1]);

            itemNoticia.setFecha(obtenerFecha().toString() + " " + obtenerHora());

            subirImagen(itemNoticia);
        }

        private void subirImagen(final ItemNoticia itemNoticia) {
            mStorageRef.child("ImagenesEvento/" + itemNoticia.getTitulo()).putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                        itemNoticia.setUrl(task.getResult().toString());
                        databaseReference.push().setValue(itemNoticia);
                        alert.dismiss();
                        finishActivity();
                    }).addOnFailureListener(e -> Snackbar.make(view, "Error al cargar la noticia", Snackbar.LENGTH_LONG).show());
                }
            });

        }

        private String[] obtenerDatos() {
            String[] datos = new String[]{txtTitulo.getEditText().getText().toString(),txtNoticia.getText().toString()};
            return datos;
        }
    }
    private CharSequence obtenerFecha() {
        Date d = new Date();
        return DateFormat.format("MMMM d, yyyy ", d.getTime());
    }
    private String obtenerHora(){
        Calendar calendario = new GregorianCalendar();
        int hora =calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        int segundos = calendario.get(Calendar.SECOND);

        return hora + ":" + minutos + ":" + segundos;
    }

    private void finishActivity(){
        finish();
    }
}
