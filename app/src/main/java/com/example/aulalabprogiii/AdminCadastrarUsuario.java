package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminCadastrarUsuario extends AppCompatActivity {

    String PrefixoURL,nomeUSU;

    EditText nomeUsuario,emailUsuario,senhaUsuario,dataNascUsuario;
    Button BotaoCadastrar;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cadastrar_usuario);

        nomeUsuario = findViewById(R.id.NomeUsuarioCadastro);
        emailUsuario = findViewById(R.id.EmailCadastro);
        senhaUsuario = findViewById(R.id.SenhaCadastro);
        dataNascUsuario = findViewById(R.id.DataNascCadastro);
        BotaoCadastrar = findViewById(R.id.BotaoCadastrar);

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
    }

    public void CadastrarUsuario(View view) {

        String nomeUsu = nomeUsuario.getText().toString();
        String emailUsu = emailUsuario.getText().toString();
        String senhaUsu = senhaUsuario.getText().toString();
        String dataNascUsu = dataNascUsuario.getText().toString();

        if (nomeUsu.matches("")||emailUsu.matches("")||senhaUsu.matches("")||dataNascUsu.matches("")) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        BotaoCadastrar.setClickable(false);

        String URLCadastrar = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=cadastrarUsuario&dispositivo=android";

        params = new RequestParams();
        params.put("login",nomeUSU);
        params.put("nome",nomeUsu);
        params.put("email",emailUsu);
        params.put("senha",senhaUsu);
        params.put("data",dataNascUsu);

        client = new AsyncHttpClient();
        client.post(URLCadastrar,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminCadastrarUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                intent.putExtra("ListaUsuarios", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoCadastrar.setClickable(true);
                Toast.makeText(AdminCadastrarUsuario.this, "Erro! Usuário já está cadastrado!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuarios&dispositivo=android" + "&login=" + nomeUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                intent.putExtra("ListaUsuarios", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminCadastrarUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
