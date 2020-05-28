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

public class ListaConversas extends AppCompatActivity {

    String PrefixoURL = "http://192.168.15.5:8080";//PARTE QUE MUDA QUANDO EU USO O LACALHOST RUN (PARA Q PESSOAS DE FORA DA MINHA REDE POSSAM ACESSAR)
    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarMsgm&dispositivo=android";
    String URL = PrefixoURL + IdentificadorURL;

    AsyncHttpClient client;
    JSONObject json;

    Button Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conversas);

        String resultado = getIntent().getStringExtra("ListaConversas");
        final String login = getIntent().getStringExtra("Login");

        try {

            JSONObject response = new JSONObject(resultado);

            GradientDrawable Opcao_Conversa = new GradientDrawable();
            Opcao_Conversa.setColor(getResources().getColor(R.color.Conversa)); // Changes this drawbale to use a single color instead of a gradient
            Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
            Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});


            try {
                for (int i = 0; i < response.getJSONArray("CONVERSAS").length(); i++) {

                    final String amigo = response.getJSONArray("CONVERSAS").get(i).toString();
                    LinearLayout layout = (LinearLayout) findViewById(R.id.Listaconversas);
                    Chat = new Button(this);
                    Chat.setText(amigo);
                    Chat.setTransformationMethod(null);
                    Chat.setMaxWidth(600);
                    Chat.setPadding(10,10,10,10);
                    Chat.setTextSize(16);
                    Chat.setBackground(Opcao_Conversa);
                    Chat.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(16,10,16,10);
                    params.gravity=Gravity.CENTER;
                    Chat.setLayoutParams(params);
                    Chat.setMinimumWidth(600);

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

    public void VerChat(String a, String b) {
        final String remetente = a;
        final String destinatario = b;

        URL = URL + "&remetente=" + remetente + "&destinatario=" + destinatario;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), ChatUsuario.class);
                intent.putExtra("MensagensConversa", json.toString());
                intent.putExtra("Login", remetente);
                intent.putExtra("Destinatario", destinatario);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(ListaConversas.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }


}
