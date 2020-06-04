package com.example.aulalabprogiii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    String PrefixoURL,nomeUSU,chaveUSU;
    TextView nomeUsu;
    EditText emailUsu,senhaUsu,datanascUsu;
    Button BotaoSalvar;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alterar_usuario);

        nomeUsu = findViewById(R.id.NomeUsuarioCadastro);
        emailUsu = findViewById(R.id.EmailCadastro);
        senhaUsu = findViewById(R.id.SenhaCadastro);
        datanascUsu = findViewById(R.id.DataNascCadastro);
        BotaoSalvar = findViewById(R.id.BotaoCadastrar);

        String resultado = getIntent().getStringExtra("UsuarioAlterar");

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        try {

            JSONObject response = new JSONObject(resultado);

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

        String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=excluirUsuario&dispositivo=android"
                + "&login=" + nomeUSU + "&nomeusuario="+ nomeUsuarioExcluir+ "&chaveUSU="+chaveUSU;

        if(nomeUsuarioExcluir.equals("admin")){ //NÃO PODE DELETAR O ADMIN - SUICÍDIO
            Toast.makeText(AdminAlterarUsuario.this, "Não se mate!!!! Não deixo!", Toast.LENGTH_LONG).show();
            return;
        }

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminAlterarUsuario.this, "Usuário removido com sucesso!", Toast.LENGTH_SHORT).show();

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                intent.putExtra("ListaUsuarios", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
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
        BotaoSalvar.setClickable(false);

        String URLAlterarCadastro = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=alterarUsuario&dispositivo=android";

        params = new RequestParams();
        params.put("login",nomeUSU);
        params.put("nome",NomeUsu);
        params.put("email",EmailUsu);
        params.put("senha",SenhaUsu);
        params.put("data",DataNascUsu);
        params.put("chaveUSU",chaveUSU);

        client = new AsyncHttpClient();
        client.post(URLAlterarCadastro,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminAlterarUsuario.this, "Cadastro alterado com sucesso!", Toast.LENGTH_SHORT).show();

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarUsuarios.class);
                intent.putExtra("ListaUsuarios", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoSalvar.setClickable(true);
                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuarios&dispositivo=android"
                + "&login=" + nomeUSU+ "&chaveUSU="+chaveUSU;

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
                Toast.makeText(AdminAlterarUsuario.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ConfirmacaoExcluir(final View view){

        String nomeUsuarioExcluir = nomeUsu.getText().toString();

        if(nomeUsuarioExcluir.equals("admin")){ //NÃO PODE DELETAR O ADMIN - SUICÍDIO
            Toast.makeText(AdminAlterarUsuario.this, "Não se mate!!!! Não deixo!", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminAlterarUsuario.this);
        builder.setCancelable(true);
        builder.setTitle("Excluir Usuário");
        builder.setMessage("Você deseja excluir o usuário?");
        builder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExcluirUsuario(view);
                    }
                });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
