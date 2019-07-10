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
    private GoogleApiClient Client;

    private SignInButton signInButton;

    public static final int SIGN_IN_CODE = 11235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // lo necesario para la creacion de metodo login

        signInButton = (SignInButton) findViewById(R.id.sing_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(Client);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                goMainScreen();
            } else {
                Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //aqui video 35.18 min

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //creacion de el metodo login
    public void btnLogineemail(View view) {

        sendLogin();
    }

    private void sendLogin() {
        EditText email = findViewById(R.id.email_txt);
        EditText password = findViewById(R.id.password_txt);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());

        client.post(Utils.LOGIN_SERVICE, params, new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers,JSONObject response) {

                if (response.has("token")) {
                    try {
                        Utils.TOKEN = response.getString("token");
                        Toast.makeText(login_google.this, "Usuario logueado con exito",Toast.LENGTH_LONG);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

    }

    //boton registrsr
    public void btnRegistrar(View view) {


        Intent btnregistrar = new Intent(this, registro.class);
        startActivity(btnregistrar);

    }
    ///   ter ino de metodo login google


 }