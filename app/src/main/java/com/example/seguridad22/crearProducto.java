package com.example.seguridad22;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class crearProducto extends AppCompatActivity {
    ArrayList<String> type;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);
        setSpinner();
        // permisos camara
        img =(ImageView)findViewById(R.id.imageView);

        if (ContextCompat.checkSelfPermission(crearProducto.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(crearProducto.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(crearProducto.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    }

    //Método para crear un nombre único de cada fotografia
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Método para tomar foto y crear el archivo
    static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //Método para mostrar vista previa en un imageview de la foto tomada
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }
    //
    //
    //
    //
    //
    //
    // metodos para enviar y regitrAR
    private void sendProducto() {

        EditText namespro = findViewById(R.id.name_producto_txt3);
        EditText pricepro= findViewById(R.id.precio_prod_txt);
        EditText descriptions = findViewById(R.id.descripcion_producto_txt2);
        EditText phones = findViewById(R.id.cantidad_prod_txt);
        Spinner categorys = findViewById(R.id.cat_productot_xt);

        //llenado de tablas

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", namespro.getText().toString());
        params.add("price", pricepro.getText().toString());
        params.add("description", descriptions.getText().toString());
        params.add("category", type.get(categorys.getSelectedItemPosition()));

        //botoon de enviado, registro de datos a login_google

        client.post(Utils.REGISTER_PRODUCT, params, new JsonHttpResponseHandler(){
            public void  onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse ) {

            }
            public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
                if (response.has("roles")) {
                    Toast.makeText(crearProducto.this, "ARTICULO REGISTRADO", Toast.LENGTH_LONG).show();
                   // Intent login =new Intent(registro.this, login_google.class);
                    // startActivity(login);

                }
            }

        });
    }


    private void setSpinner() {
        type = new ArrayList<>();
        type.add("Moda");
        type.add("Zapatos");
        type.add("Accesorios");
        type.add("Electronica");
        type.add("Inmobiliaria");
        type.add("Coches");
        type.add("Hogar y jardin");
        type.add("Otros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,type);
        Spinner spinner = findViewById(R.id.cat_productot_xt);
        spinner.setAdapter(adapter);
        //spinner.getSelectedItemPosition();
    }

    public void enviarProducto(View view) {
        Toast.makeText(crearProducto.this, "REGISTRANDO ESPERE", Toast.LENGTH_LONG).show();
        sendProducto();


    }


}
