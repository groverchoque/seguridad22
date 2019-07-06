package com.example.seguridad22;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

//registro google implements GoogleApiClient.OnConnectionFailedListener
public class login_google extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btnLogin, btnRegistrar;
    //creacion del metodo login
    //login google
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SING_IN_GOOGLE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);
        sendLogin();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton = (SignInButton) findViewById(R.id.sing_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SING_IN_GOOGLE);

            }
        });
       // btnRegistrar = (Button) registro.findViewById(R.id.btnregistrar);
    }
    //registro google
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SING_IN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSingInResult(result);

        }
    }

    private void handleSingInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            goMainScreen();
        }else {
            Toast.makeText(this,R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }

    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    // FIN DEL LOGIN GOOGLE


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
