package com.example.seguridad22;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.seguridad22.Utilities.Methods;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class crearProducto extends AppCompatActivity implements View.OnClickListener{

    EditText   nombredes,descripcionEdit, precioEdit, stockEdit;
    Spinner estadoSpinner, categoriaSpinner;
    ImageView imageView;
    Button btnCamera, btnPublicar;

    String estado,categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);
        loadComponents();


        Methods.validarPermisos(this,this);

    }

    private void loadComponents() {
        descripcionEdit = findViewById(R.id.descripcionEdit);
        precioEdit = findViewById(R.id.precioEdit);
        stockEdit = findViewById(R.id.stockEdit);
        nombredes = findViewById(R.id.name_producto_txt3);

        estadoSpinner = findViewById(R.id.estadoSpinner);
        categoriaSpinner = findViewById(R.id.categoriaSpinner);

        estadoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView = findViewById(R.id.imageView);

        btnCamera = findViewById(R.id.btnCamera);
        btnPublicar = findViewById(R.id.btnPublicar);
        btnCamera.setOnClickListener(this);

        btnPublicar.setOnClickListener(this);

    }


    private void sendData() {

        if (descripcionEdit.getText().toString().isEmpty() || precioEdit.getText().toString().isEmpty()  || stockEdit.getText().toString().isEmpty()){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (path == null || path == ""){
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        File file = new File(path);
        try {
            params.put("foto", file,"image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        params.put("nombre", nombredes.getText());
        params.put("descripcion", descripcionEdit.getText());

        params.put("precio", precioEdit.getText());
        params.put("stock", stockEdit.getText());
        params.put("estado", estado);
        params.put("categoria", categoria);
        params.put("vendedor",Utils.ID_USER);



        client.post(Utils.REGISTER_PRODUCT,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                try {


                    if (resp.getString("message") != null) {
                        Toast.makeText(crearProducto.this, resp.getString("message"), Toast.LENGTH_LONG).show();
                        path = "";
                        descripcionEdit.getText().clear();
                        precioEdit.getText().clear();
                        stockEdit.getText().clear();

                        crearProducto.this.finish();

                    } else {
                        Toast.makeText(crearProducto.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(crearProducto.this, responseString, Toast.LENGTH_LONG).show();
                Log.d("message",responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(crearProducto.this, errorResponse.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPublicar){
            sendData();
        }
        if (v.getId() == R.id.btnCamera){
            Snackbar.make(v,"Message", Snackbar.LENGTH_LONG).show();
            cargarImagen();
        }
    }
    //DESDE AQUI VA LA PARTE DE LA FOTO
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    String path;

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(crearProducto.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }
    private void tomarFotografia() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Methods.FileAndPath fileAndPath= Methods.createFile(path);
        File file = fileAndPath.getFile();
        path = fileAndPath.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
            //BuildConfig.APPLICATION_ID + ".provider"
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, COD_CAMERA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_GALERIA:
                    Uri imgPath=data.getData();
                    imageView.setImageURI(imgPath);
                    path = Methods.getRealPathFromURI(this,imgPath);
                    Toast.makeText(crearProducto.this, path, Toast.LENGTH_SHORT).show();
                    break;
                case COD_CAMERA:
                    loadImageCamera();


                    break;
            }
        }
    }

    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(path);
        if(img != null) {
            imageView.setImageBitmap(img);

        }
    }

}
