package com.example.seguridad22;

import android.content.Intent;
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

public class registro extends AppCompatActivity {
    ArrayList<String> type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setSpinner();

    }

    private void sendData() {

        EditText names = findViewById(R.id.name_txt);
        EditText  addresss= findViewById(R.id.addres);
        EditText passwords = findViewById(R.id.password);
        EditText emails = findViewById(R.id.email);
        EditText phones = findViewById(R.id.telefono_txt);
        Spinner types = findViewById(R.id.tipo_txt);

        //llenado de tablas

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", names.getText().toString());
        params.add("addres", addresss.getText().toString());
        params.add("password", passwords.getText().toString());
        params.add("email", emails.getText().toString());
        params.add("phone", phones.getText().toString());
        params.add("type", type.get(types.getSelectedItemPosition()));

        //botoon de enviado, registro de datos a login_google

        client.post(Utils.REGISTER_SERVICE, params, new JsonHttpResponseHandler(){
            public void  onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse ) {

            }
             public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
                 if (response.has("roles")) {
                     Toast.makeText(registro.this, "usuario registrado con exito", Toast.LENGTH_LONG).show();
                     Intent login =new Intent(registro.this, login_google.class);
                     startActivity(login);

                 }
             }

        });
    }

    // funcion seleccion de tipo

    public void sendMensaje(){
        Toast.makeText(registro.this, "registrando usuario espere", Toast.LENGTH_LONG).show();

    }

    private void setSpinner() {
       type = new ArrayList<>();
       type.add("Comprador");
       type.add("Vendedor");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,type);
       Spinner spinner = findViewById(R.id.tipo_txt);
        spinner.setAdapter(adapter);
      //spinner.getSelectedItemPosition();
    }

    public void enviar(View view) {
        Toast.makeText(registro.this, "registrando usuario espere", Toast.LENGTH_LONG).show();
       sendData();
        sendMensaje();
    }
}
