package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminCriarGrupo extends AppCompatActivity {

    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_criar_grupo);

        EditText mensagem = (EditText)findViewById(R.id.Mensagemm);
        mensagem.setText(getIntent().getStringExtra("ListaUsuarios"));
    }


    @Override
    public void onBackPressed() {

        String URL = getIntent().getStringExtra("PrefixoURL") +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login");

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarGrupos.class);
                intent.putExtra("ListaGrupos", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminCriarGrupo.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }
}
