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

public class AdminListaConversas extends AppCompatActivity {

    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarMsgm&dispositivo=android";
    String URL;

    AsyncHttpClient client;
    JSONObject json;

    Button Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_conversas);

        String resultado = getIntent().getStringExtra("ListaConversas");
        final String login = getIntent().getStringExtra("Login");

        try {

            JSONObject response = new JSONObject(resultado);

            GradientDrawable Titulo = new GradientDrawable();
            Titulo.setColor(getResources().getColor(R.color.transparent)); // Changes this drawbale to use a single color instead of a gradient
            Titulo.setShape(GradientDrawable.RECTANGLE);
            Titulo.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

            LinearLayout layout = (LinearLayout) findViewById(R.id.Listaconversas);
            Chat = new Button(this);
            Chat.setText("Conversas Admin");
            Chat.setTransformationMethod(null);
            Chat.setWidth(850);
            Chat.setPadding(10,10,10,10);
            Chat.setTextSize(23);
            Chat.setBackground(Titulo);
            Chat.setGravity(Gravity.CENTER);
            Chat.setClickable(false);
            layout.addView(Chat);


            try {
                for (int i = 0; i < response.getJSONArray("CONVERSAS").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa)); // Changes this drawbale to use a single color instead of a gradient
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String amigo = response.getJSONArray("CONVERSAS").get(i).toString();
                    Chat = new Button(this);
                    Chat.setText(amigo);
                    Chat.setTransformationMethod(null);
                    Chat.setWidth(850);
                    Chat.setPadding(10, 10, 10, 10);
                    Chat.setTextSize(20);
                    Chat.setBackground(Opcao_Conversa);
                    Chat.setGravity(Gravity.LEFT);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(16, 10, 16, 10);
                    params.gravity = Gravity.CENTER;
                    Chat.setLayoutParams(params);

                    Chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VerChat(login, amigo);
                        }
                    });

                    layout.addView(Chat);
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
        Toast.makeText(AdminListaConversas.this,"Para realizar logout clique no botão sair!",Toast.LENGTH_LONG).show();
    }

    public void VerChat(String a, String b) {
        final String remetente = a;
        final String destinatario = b;

        URL = getIntent().getStringExtra("PrefixoURL") + IdentificadorURL + "&remetente=" + remetente + "&destinatario=" + destinatario;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", remetente);
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                intent.putExtra("Destinatario", destinatario);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void ListarUsuarios(View view) {

        URL = getIntent().getStringExtra("PrefixoURL") +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuarios&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login");

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

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

                Toast.makeText(AdminListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void ListarGrupos(View view) {

        URL = getIntent().getStringExtra("PrefixoURL") +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android"
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

                Toast.makeText(AdminListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void CriarGrupo(View view) {

        URL = getIntent().getStringExtra("PrefixoURL") +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarUsuarios&dispositivo=android"
                + "&login=" + getIntent().getStringExtra("Login");

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminCriarGrupo.class);
                intent.putExtra("ListaUsuarios", json.toString());
                intent.putExtra("Login", getIntent().getStringExtra("Login"));
                intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(AdminListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void abrirTelaEnviarMensagemDireto(View view){

        Intent intent = new Intent( this, EnviarMensagemDireto.class);
        intent.putExtra("Login", getIntent().getStringExtra("Login"));
        intent.putExtra("PrefixoURL", getIntent().getStringExtra("PrefixoURL"));
        startActivity(intent);

    }

    public void Sair(View view){

        Intent intent = new Intent( this, TelaLogin.class);
        startActivity(intent);

    }

}
