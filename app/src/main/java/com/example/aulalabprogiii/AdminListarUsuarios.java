package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminListarUsuarios extends AppCompatActivity {

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";
    String PrefixoURL,nomeUSU,chaveUSU;

    AsyncHttpClient client;
    JSONObject json;

    Button BotaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listar_usuarios);

        String resultado = getIntent().getStringExtra("ListaUsuarios");

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        try {

            JSONObject response = new JSONObject(resultado);

            try {
                GradientDrawable BotaoCadastro = new GradientDrawable();
                BotaoCadastro.setColor(getResources().getColor(R.color.colorAccent));
                BotaoCadastro.setShape(GradientDrawable.RECTANGLE);
                BotaoCadastro.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                GradientDrawable Titulo = new GradientDrawable();
                Titulo.setColor(getResources().getColor(R.color.transparent));
                Titulo.setShape(GradientDrawable.RECTANGLE);
                Titulo.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                final LinearLayout layout = findViewById(R.id.ListaUsuariosLinearLayout);

                BotaoUsuario = new Button(this);
                BotaoUsuario.setText("Cadastrar Usuário");
                BotaoUsuario.setTransformationMethod(null);
                BotaoUsuario.setWidth(750);
                BotaoUsuario.setPadding(10, 16, 10, 16);
                BotaoUsuario.setTextSize(18);
                BotaoUsuario.setTextColor(getResources().getColor(R.color.white));
                BotaoUsuario.setBackground(BotaoCadastro);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(16, 32, 16, 16);
                params.gravity = Gravity.CENTER;
                BotaoUsuario.setLayoutParams(params);
                BotaoUsuario.setGravity(Gravity.CENTER);

                BotaoUsuario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<layout.getChildCount();i++){
                            View child=layout.getChildAt(i);
                            child.setClickable(false);
                            child.setFocusableInTouchMode(false);
                        }
                        abrirTelaCadastroUsuario(v);
                    }
                });

                layout.addView(BotaoUsuario);

                BotaoUsuario = new Button(this);
                BotaoUsuario.setText("Lista de Usuários");
                BotaoUsuario.setTransformationMethod(null);
                BotaoUsuario.setWidth(950);
                BotaoUsuario.setPadding(10, 10, 10, 10);
                BotaoUsuario.setTextSize(23);
                BotaoUsuario.setBackground(Titulo);
                BotaoUsuario.setGravity(Gravity.CENTER);
                BotaoUsuario.setClickable(false);
                layout.addView(BotaoUsuario);

                for (int i = 0; i < response.getJSONArray("USUARIOS").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa));
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String nomeUsuario = response.getJSONArray("USUARIOS").getJSONObject(i).get("nome").toString();

                    BotaoUsuario = new Button(this);
                    BotaoUsuario.setText(nomeUsuario);
                    BotaoUsuario.setTransformationMethod(null);
                    BotaoUsuario.setWidth(950);
                    BotaoUsuario.setPadding(10, 10, 10, 10);
                    BotaoUsuario.setTextSize(20);
                    BotaoUsuario.setBackground(Opcao_Conversa);
                    BotaoUsuario.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramss.setMargins(16, 10, 16, 10);
                    paramss.gravity = Gravity.CENTER;
                    BotaoUsuario.setLayoutParams(paramss);

                    BotaoUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<layout.getChildCount();i++){
                                View child=layout.getChildAt(i);
                                child.setClickable(false);
                                child.setFocusableInTouchMode(false);
                            }
                            VerCadastroUsuario(nomeUsuario);
                        }
                    });

                    layout.addView(BotaoUsuario);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

    }

    @Override
    public void onBackPressed() {

        URL = PrefixoURL+ IdentificadorURL + "&login=" + nomeUSU+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListaConversas.class);
                intent.putExtra("ListaConversas", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminListarUsuarios.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void VerCadastroUsuario(String a) {

        final String nomeUsuario = a;

        URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=alterarCadastro&dispositivo=android"
                + "&login=" + nomeUSU + "&nomeUsuario=" + nomeUsuario+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminAlterarUsuario.class);
                intent.putExtra("UsuarioAlterar", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminListarUsuarios.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void abrirTelaCadastroUsuario(View view){

        Intent intent = new Intent( this, AdminCadastrarUsuario.class);
        startActivity(intent);
    }

}