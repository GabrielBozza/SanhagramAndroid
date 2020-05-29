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

public class AdminListarUsuarios extends AppCompatActivity {

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = "";

    AsyncHttpClient client;
    JSONObject json;

    Button BotaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listar_usuarios);

        String resultado = getIntent().getStringExtra("ListaUsuarios");
        final String login = getIntent().getStringExtra("Login");

        try {

            JSONObject response = new JSONObject(resultado);

            try {
                GradientDrawable Titulo = new GradientDrawable();
                Titulo.setColor(getResources().getColor(R.color.transparent));
                Titulo.setShape(GradientDrawable.RECTANGLE);
                Titulo.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                LinearLayout layout = (LinearLayout) findViewById(R.id.ListaUsuariosLinearLayout);
                BotaoUsuario = new Button(this);
                BotaoUsuario.setText("Lista de Usuários");
                BotaoUsuario.setTransformationMethod(null);
                BotaoUsuario.setWidth(850);
                BotaoUsuario.setPadding(10, 10, 10, 10);
                BotaoUsuario.setTextSize(23);
                BotaoUsuario.setBackground(Titulo);
                BotaoUsuario.setGravity(Gravity.CENTER);
                BotaoUsuario.setClickable(false);
                layout.addView(BotaoUsuario);

                for (int i = 0; i < response.getJSONArray("USUARIOS").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa)); // Changes this drawbale to use a single color instead of a gradient
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String nomeUsuario = response.getJSONArray("USUARIOS").getJSONObject(i).get("nome").toString();

                    BotaoUsuario = new Button(this);
                    BotaoUsuario.setText(nomeUsuario);
                    BotaoUsuario.setTransformationMethod(null);
                    BotaoUsuario.setWidth(850);
                    BotaoUsuario.setPadding(10, 10, 10, 10);
                    BotaoUsuario.setTextSize(20);
                    BotaoUsuario.setBackground(Opcao_Conversa);
                    BotaoUsuario.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(16, 10, 16, 10);
                    params.gravity = Gravity.CENTER;
                    BotaoUsuario.setLayoutParams(params);

                    BotaoUsuario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

                Toast.makeText(AdminListarUsuarios.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void VerCadastroUsuario(String a) {

        final String nomeUsuario = a;

        URL = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=alterarCadastro&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login") + "&nomeUsuario=" + nomeUsuario;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminAlterarUsuario.class);
                intent.putExtra("UsuarioAlterar", json.toString());
                System.out.println(json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminListarUsuarios.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

}