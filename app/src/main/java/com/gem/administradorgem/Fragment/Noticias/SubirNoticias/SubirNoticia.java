package com.gem.administradorgem.Fragment.Noticias.SubirNoticias;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

//import com.conalep.admin.MainActivity;


public class SubirNoticia extends AppCompatActivity {

    private ImageButton btnImgNoticia;
    private ImageView imgNoticia;

    public static Uri uriFile;

    private static boolean cambioImagen;

    private TextInputLayout txtTitulo;
    private EditText txtNoticia;
    private AlertDialog alert;

    private DatabaseReference reference;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_noticia);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initComponents();
        alert = dialogoPrimeraVez();

        imgNoticia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SubirNoticia.this);
                builder.setTitle("Noticia");
                builder.setMessage("Esta a punto de eliminar la imagen seleccionada ¿Esta seguro?");
                builder.setPositiveButton("Confirmar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgNoticia.setImageResource(R.drawable.ic_launcher_background);
                        btnImgNoticia.setEnabled(true);
                        btnImgNoticia.setVisibility(View.VISIBLE);
                        cambioImagen = false;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
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

    /**
     * Inicializa los componentes
     */
    private void initComponents() {
        btnImgNoticia = (ImageButton) findViewById(R.id.btnImagenNoticia);
        imgNoticia = (ImageView) findViewById(R.id.imgNoticia);

        txtTitulo = findViewById(R.id.txtTituloNoticia);
        txtNoticia = findViewById(R.id.txtDescripcionNoticia);

        int index = getSharedPreferences("ADMIN",Context.MODE_PRIVATE).getInt("index",0);
        String storageUrl[] = new String[]{
                /*Mante*/"gs://mante",
                /*Matamoros*/"gs://matamoros",
                /*Miguel Aleman*/"gs://miguel_aleman",
                /*Nuevo Laredo*/"gs://nuevo_laredo",
                /*Reynosa*/"gs://reynosa",
                /*Rio Bravo*/"gs://rio_bravo",
                /*Tampico*/"gs://tampico/",
                /*Victoria*/"gs://victoria_1",
                /*Cast*/"gs://cast_matamoros"
        };
        mStorageRef = FirebaseStorage.getInstance(storageUrl[index]).getReference();

    }

    /**Elimina la referencia de los componentes**/
    private void deleteComponents(){
        btnImgNoticia.setImageDrawable(null);
        btnImgNoticia = null;
        imgNoticia.setImageDrawable(null);
        imgNoticia = null;


        cambioImagen = false;
        System.gc();
    }

    /**Funciones para abrir galeria y cargar imagen**/
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

        private String url[] = new String[]{
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

        public SubirFirebase(View view) {
            this.view = view;


            databaseReference = FirebaseDatabase.getInstance(url[getSharedPreferences("ADMIN", Context.MODE_PRIVATE)
                    .getInt("index",0)])
                    .getReference("noticias/");
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
            mStorageRef.child("ImagenesEvento/" + "Mante/" + itemNoticia.getTitulo()).putFile(uriFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            itemNoticia.setUrl(task.getResult().toString());
                            databaseReference.push().setValue(itemNoticia);
                            alert.dismiss();
                            finishActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(view,"Error al cargar la noticia", Snackbar.LENGTH_LONG).show();
                        }
                    });
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

        return new String(hora + ":" + minutos + ":" + segundos);
    }

    private void finishActivity(){
        finish();
    }
}
