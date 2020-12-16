package com.gem.administradorgem.Fragment.Noticias.SubirNoticias;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gem.administradorgem.Fragment.Noticias.BottomSheetNoticias;
import com.gem.administradorgem.Fragment.Noticias.FirebaseNoticia;
import com.gem.administradorgem.Fragment.Noticias.ItemNoticia;
import com.gem.administradorgem.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SubirNoticia extends AppCompatActivity {

    private boolean cambioImagen;

    private ImageButton btnImgNoticia;
    private ImageView imgNoticia;

    public static Uri uriFile;

    private TextInputLayout txtTitulo;
    private EditText txtNoticia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_noticia);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initComponents();

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

    public void setBtnEnviar(View v) {
        if (cambioImagen) {
            ItemNoticia noticia = generarNoticia();
            if (noticia != null) {
                BottomSheetNoticias sheet = new BottomSheetNoticias(noticia,
                        uriFile, new FirebaseNoticia(SubirNoticia.this));
                sheet.show(getSupportFragmentManager(), "noticia");
            }


        } else
            Toast.makeText(this, "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show();
    }

    //********************************************
    //Construye, genera y devuelve un objeto ItemNoticia
    //*****************************************
    private ItemNoticia generarNoticia() {
        ItemNoticia noticia;
        if ((!TextUtils.isEmpty(txtTitulo.getEditText().getText())) &&
                (!TextUtils.isEmpty(txtNoticia.getText()))) {
            noticia = new ItemNoticia();

            noticia.setTitulo(txtTitulo.getEditText().getText().toString());
            noticia.setDescripcion(txtNoticia.getText().toString());
            noticia.setFecha(DateFormat.format("MMMM d, yyyy ", new Date().getTime()).toString() + " " + obtenerHora());

            return noticia;
        } else {
            Toast.makeText(this, "Titulo o Cuerpo vacío", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    //********************************************
    //Regresa una cadena string con la hora en la que se subio la noticia
    //*****************************************
    private String obtenerHora() {
        Calendar calendario = new GregorianCalendar();
        return calendario.get(Calendar.HOUR_OF_DAY) + ":" +
                calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND);
    }

    //********************************************
    //Este metodo inicializa los componentes
    //*****************************************
    private void initComponents() {
        btnImgNoticia = findViewById(R.id.btnImagenNoticia);
        imgNoticia = findViewById(R.id.imgNoticia);

        txtTitulo = findViewById(R.id.txtTituloNoticia);
        txtNoticia = findViewById(R.id.txtDescripcionNoticia);
    }


    @Override
    public void onDestroy() {
        deleteComponents();
        super.onDestroy();
    }

    //********************************************
    //Este metodo elimina las referencias de los objetos
    //*****************************************
    private void deleteComponents() {
        btnImgNoticia.setImageDrawable(null);
        btnImgNoticia = null;
        imgNoticia.setImageDrawable(null);
        imgNoticia = null;

        cambioImagen = false;
        System.gc();
    }

    //********************************************
    //Metodo que abre la galeria del usuario
    //*****************************************
    @SuppressLint("IntentReset")
    public void abrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/");
        startActivityForResult(gallery.createChooser(gallery, "Seleccione la Aplicacion"), 100);
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

    //********************************************
    //Carga la uri de la imagen en el botton image
    //*****************************************
    public void cargarImagen() {
        btnImgNoticia.setEnabled(false);
        btnImgNoticia.setVisibility(View.INVISIBLE);

        Glide.with(this).load(uriFile).into(imgNoticia);
    }


}
