package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TelaLogin extends AppCompatActivity {

    EditText NomeUsuario,Senha;
    Button Login;

    String nome,senha,PrefixoURL,URL;
    //String PrefixoURL = "http://192.168.15.5:8080";//PARTE QUE MUDA QUANDO EU USO O LACALHOST RUN (PARA Q PESSOAS DE FORA DA MINHA REDE POSSAM ACESSAR)
    String IdentificadorURL = "/SanhagramServletsJSP/autenticador?dispositivo=android";


    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        PrefixoURL = getIntent().getStringExtra("PrefixoURL");
        URL = PrefixoURL+IdentificadorURL;

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
                        json = response;

                        if(nome.equals("admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                            intent.putExtra("Login", nome);//PARA MANTER REF AO USUARIO
                            intent.putExtra("PrefixoURL", PrefixoURL);//PECULIARIDADE DE USAR LOCALHOST.RUN--PREFIXO MUDA
                            intent.putExtra("ListaConversas", json.toString());
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                            intent.putExtra("Login", nome);//PARA MANTER REF AO USUARIO
                            intent.putExtra("PrefixoURL", PrefixoURL);//PECULIARIDADE DE USAR LOCALHOST.RUN--PREFIXO MUDA
                            intent.putExtra("ListaConversas", json.toString());
                            startActivity(intent);
                        }
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

    @Override
    public void onBackPressed() {
        Toast.makeText(TelaLogin.this,"Você não está logado!",Toast.LENGTH_LONG).show();
    }

}
