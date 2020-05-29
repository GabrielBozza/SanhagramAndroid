package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminAlterarUsuario extends AppCompatActivity {

    TextView nomeUsu;
    EditText emailUsu,senhaUsu,datanascUsu;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alterar_usuario);

        nomeUsu = (TextView)findViewById(R.id.NomeUsuarioCadastro);
        emailUsu = (EditText)findViewById(R.id.EmailCadastro);
        senhaUsu = (EditText)findViewById(R.id.SenhaCadastro);
        datanascUsu = (EditText)findViewById(R.id.DataNascCadastro);

        String resultado = getIntent().getStringExtra("UsuarioAlterar");
        final String login = getIntent().getStringExtra("Login");

        try {
            JSONObject response = new JSONObject(resultado);
            System.out.println(resultado);
            nomeUsu.setText(response.getJSONArray("USUARIOALTERAR").getJSONObject(0).get("nome").toString());
            emailUsu.setText(response.getJSONArray("USUARIOALTERAR").getJSONObject(0).get("email").toString());
            senhaUsu.setText(response.getJSONArray("USUARIOALTERAR").getJSONObject(0).get("senha").toString());
            datanascUsu.setText(response.getJSONArray("USUARIOALTERAR").getJSONObject(0).get("datanasc").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ExcluirUsuario(View view){

        String nomeUsuarioExcluir = nomeUsu.getText().toString();

        String URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=excluirUsuario&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login") + "&nomeusuario="+ nomeUsuarioExcluir;

        if(nomeUsuarioExcluir.equals("admin")){ //NÃO PODE DELETAR O ADMIN - SUICÍDIO
            Toast.makeText(AdminAlterarUsuario.this, "Não se mate!!!! Não deixo!", Toast.LENGTH_LONG).show();
            return;
        }

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if (getIntent().getStringExtra("Login").equals("admin")) {
                    Toast.makeText(AdminAlterarUsuario.this, "Usuário removido com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                    intent.putExtra("Login", getIntent().getStringExtra("Login"));
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaUsuarios", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void AlterarCadastro(View view){

        String NomeUsu = nomeUsu.getText().toString();
        String EmailUsu = emailUsu.getText().toString();
        String SenhaUsu = senhaUsu.getText().toString();
        String DataNascUsu = datanascUsu.getText().toString();

        if (NomeUsu.matches("")||EmailUsu.matches("")||SenhaUsu.matches("")||DataNascUsu.matches("")) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        String URLAlterarCadastro = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=alterarUsuario&dispositivo=android";

        params = new RequestParams();
        params.put("login",getIntent().getStringExtra("Login"));
        params.put("nome",NomeUsu);
        params.put("email",EmailUsu);
        params.put("senha",SenhaUsu);
        params.put("data",DataNascUsu);

        client = new AsyncHttpClient();
        client.post(URLAlterarCadastro,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminAlterarUsuario.this, "Cadastro alterado com sucesso!", Toast.LENGTH_LONG).show();

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

                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_LONG).show();
                return;

            }
        });
    }

    @Override
    public void onBackPressed() {

        final String login = getIntent().getStringExtra("Login");

        String URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuarios&dispositivo=android" + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;
                if (getIntent().getStringExtra("Login").equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                    intent.putExtra("Login", login);
                    intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                    intent.putExtra("ListaUsuarios", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
