package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TelaLogin extends AppCompatActivity {

    EditText NomeUsuario,Senha;
    Button Login;
    String nome,senha;
    String URL = "http://192.168.15.5:8080/SanhagramServletsJSP/autenticador?dispositivo=android";
    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        NomeUsuario = (EditText)findViewById(R.id.NomeUsuario);
        Senha = (EditText)findViewById(R.id.Senha);
        Login = (Button)findViewById(R.id.BotaoLogin);

        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                nome = NomeUsuario.getText().toString();
                senha = Senha.getText().toString();

                params = new RequestParams();
                params.put("nome",nome);
                params.put("senha",senha);

                client = new AsyncHttpClient();
                client.post(URL,params,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        super.onSuccess(statusCode, headers,response);
                        Toast.makeText(TelaLogin.this,"Login bem sucedido!"+response,Toast.LENGTH_LONG).show();
                        json = response;
                        String ListaConversas = "";
                        try {
                            ListaConversas = response.getJSONArray("AMIGOS").get(0).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent( getApplicationContext(), TelaCadastro.class);
                        intent.putExtra("Login",nome);
                        intent.putExtra("ListaConversas",ListaConversas);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                        super.onFailure(statusCode,headers,responseString,throwable);
                        Toast.makeText(TelaLogin.this,"Erro-Login e/ou senha incorretos!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
            }
        });
    }

}
