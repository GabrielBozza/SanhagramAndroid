package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AdminListarGrupos extends AppCompatActivity {

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";

    AsyncHttpClient client;
    JSONObject json;

    Button BotaoGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listar_grupos);

        String resultado = getIntent().getStringExtra("ListaGrupos");
        final String login = getIntent().getStringExtra("Login");

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

                LinearLayout layout = (LinearLayout) findViewById(R.id.ListaGruposLinearLayout);

                BotaoGrupo = new Button(this);
                BotaoGrupo.setText("Criar Grupo");
                BotaoGrupo.setTransformationMethod(null);
                BotaoGrupo.setWidth(450);
                BotaoGrupo.setPadding(10, 16, 10, 16);
                BotaoGrupo.setTextSize(16);
                BotaoGrupo.setTextColor(getResources().getColor(R.color.white));
                BotaoGrupo.setBackground(BotaoCadastro);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(16, 10, 16, 10);
                params.gravity = Gravity.CENTER;
                BotaoGrupo.setLayoutParams(params);
                BotaoGrupo.setGravity(Gravity.CENTER);
                layout.addView(BotaoGrupo);

                BotaoGrupo = new Button(this);
                BotaoGrupo.setText("Lista de Grupos");
                BotaoGrupo.setTransformationMethod(null);
                BotaoGrupo.setWidth(850);
                BotaoGrupo.setPadding(10, 10, 10, 10);
                BotaoGrupo.setTextSize(23);
                BotaoGrupo.setBackground(Titulo);
                BotaoGrupo.setGravity(Gravity.CENTER);
                BotaoGrupo.setClickable(false);
                layout.addView(BotaoGrupo);

                for (int i = 0; i < response.getJSONArray("GRUPOS").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa));
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String nomeGrupo = response.getJSONArray("GRUPOS").getJSONObject(i).get("nome").toString();

                    BotaoGrupo = new Button(this);
                    BotaoGrupo.setText(nomeGrupo);
                    BotaoGrupo.setTransformationMethod(null);
                    BotaoGrupo.setWidth(850);
                    BotaoGrupo.setPadding(10, 10, 10, 10);
                    BotaoGrupo.setTextSize(20);
                    BotaoGrupo.setBackground(Opcao_Conversa);
                    BotaoGrupo.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    paramss.setMargins(16, 10, 16, 10);
                    paramss.gravity = Gravity.CENTER;
                    BotaoGrupo.setLayoutParams(paramss);

                    BotaoGrupo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VerUsuariosGrupo(nomeGrupo);
                        }
                    });

                    layout.addView(BotaoGrupo);
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

                Toast.makeText(AdminListarGrupos.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void VerUsuariosGrupo(String a) {

        final String nomeGrupo = a;

        URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuariosDoGrupo&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login") + "&nomeGrupo=" + nomeGrupo;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminAlterarGrupo.class);
                intent.putExtra("UsuariosGrupo", json.toString());
                intent.putExtra("NomeGrupo", nomeGrupo);
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminListarGrupos.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

}