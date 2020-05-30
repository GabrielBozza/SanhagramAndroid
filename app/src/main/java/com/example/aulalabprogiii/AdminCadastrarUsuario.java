package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class AdminCadastrarUsuario extends AppCompatActivity {

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";

    EditText nomeUsuario,emailUsuario,senhaUsuario,dataNascUsuario;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cadastrar_usuario);

        nomeUsuario = (EditText)findViewById(R.id.NomeUsuarioCadastro);
        emailUsuario = (EditText)findViewById(R.id.EmailCadastro);
        senhaUsuario = (EditText)findViewById(R.id.SenhaCadastro);
        dataNascUsuario = (EditText)findViewById(R.id.DataNascCadastro);

        Button BotaoCadastrar = (Button)findViewById(R.id.BotaoCadastrar);
    }

    public void CadastrarUsuario(View view) throws UnsupportedEncodingException {

        String nomeUsu = nomeUsuario.getText().toString();
        String emailUsu = emailUsuario.getText().toString();
        String senhaUsu = senhaUsuario.getText().toString();
        String dataNascUsu = dataNascUsuario.getText().toString();

        if (nomeUsu.matches("")||emailUsu.matches("")||senhaUsu.matches("")||dataNascUsu.matches("")) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLCadastrar = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=cadastrarUsuario&dispositivo=android";

        params = new RequestParams();
        params.put("login",getIntent().getStringExtra("Login"));
        params.put("nome",nomeUsu);
        params.put("email",emailUsu);
        params.put("senha",senhaUsu);
        params.put("data",dataNascUsu);

        client = new AsyncHttpClient();
        client.post(URLCadastrar,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminCadastrarUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                intent.putExtra("ListaUsuarios", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminCadastrarUsuario.this, "Erro! Usuário já está cadastrado!", Toast.LENGTH_SHORT).show();
                return;

            }
        });

    }

    @Override
    public void onBackPressed() {

        final String login = getIntent().getStringExtra("Login");

        URL = getIntent().getStringExtra("PrefixoURL") + IdentificadorURL + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if (getIntent().getStringExtra("Login").equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ListaConversas.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaConversas", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminCadastrarUsuario.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
