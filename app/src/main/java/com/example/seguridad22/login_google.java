package com.example.seguridad22;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class login_google extends AppCompatActivity {
    Button btnLogin, btnRegistrar;
    //creacion del metodo login


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);
        sendLogin();
       // btnRegistrar = (Button) registro.findViewById(R.id.btnregistrar);
    }


     //btnRegistrar.setOnClickListener(new View.OnClickListener() {
        //public void onClick(View view) {
          //  registrar_usuario();
      //  }
    //});

        //creacion del metodo y coneccion base de datos login
    private void sendLogin(){
        EditText email = findViewById(R.id.email_txt);
        EditText password = findViewById(R.id.password_txt);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", email.getText().toString());
        params.add("password", password.getText().toString());
        client.post(Utils.LOGIN_SERVICE, params, new JsonHttpResponseHandler(){
            public void onSuccess (int statusCode, Header[] headers, JSONObject response){
                    //AsyncHttpClient.log.w(LOG_TAG, "onSuccess(int, Header[], JSONObject)was not overriden, but call" );

                //validacion del token
                if (response.has("token")){
                    try {
                        Utils.TOKEN = response.getString("token");
                        Toast.makeText(login_google.this, "Usuario logueado con exito", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }  //final del metoodo



}
