package com.example.seguridad22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class crearProducto extends AppCompatActivity {
    ArrayList<String> type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);
        setSpinner();
    }

    private void sendProducto() {

        EditText namespro = findViewById(R.id.name_producto_txt);
        EditText pricepro= findViewById(R.id.price_txt);
       // EditText descriptions = findViewById(R.id.description_txt3);
        EditText phones = findViewById(R.id.price_txt);
        Spinner categorys = findViewById(R.id.categorypro_txt);

        //llenado de tablas

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", namespro.getText().toString());
        params.add("price", pricepro.getText().toString());
      //  params.add("description", descriptions.getText().toString());
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
        Spinner spinner = findViewById(R.id.categorypro_txt);
        spinner.setAdapter(adapter);
        //spinner.getSelectedItemPosition();
    }

    public void enviarProducto(View view) {
        Toast.makeText(crearProducto.this, "REGISTRANDO ESPERE", Toast.LENGTH_LONG).show();
        sendProducto();


    }
}
