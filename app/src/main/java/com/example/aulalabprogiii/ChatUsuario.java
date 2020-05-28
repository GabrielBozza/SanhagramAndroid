package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

public class ChatUsuario extends AppCompatActivity {

    Button BotaoEnviar, Texto_Mensagem;
    EditText Mensagem;
    ScrollView mScrollView;

    String PrefixoURL = "http://192.168.15.5:8080";//PARTE QUE MUDA QUANDO EU USO O LACALHOST RUN (PARA Q PESSOAS DE FORA DA MINHA REDE POSSAM ACESSAR)
    String IdentificadorURL = "/SanhagramServletsJSP/UsuarioControlador?acao=listarConversas&dispositivo=android";
    String URL = PrefixoURL+IdentificadorURL;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_usuario);

        Mensagem = (EditText)findViewById(R.id.Mensagem);
        BotaoEnviar = (Button)findViewById(R.id.BotaoEnviar);
        mScrollView = (ScrollView)findViewById(R.id.chatScrollView);

        String resultado = getIntent().getStringExtra("MensagensConversa");
        String login = getIntent().getStringExtra("Login");

        GradientDrawable Botao_Enviar = new GradientDrawable();
        Botao_Enviar.setColor(getResources().getColor(R.color.colorAccent));
        Botao_Enviar.setShape(GradientDrawable.RECTANGLE);
        Botao_Enviar.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});
        BotaoEnviar.setBackground(Botao_Enviar);


        try {

            JSONObject response = new JSONObject(resultado);

            try {
                for(int i=0;i<response.getJSONArray("MENSAGENS").length();i++) {

                    int raio =35;
                    GradientDrawable Bolha_esquerda = new GradientDrawable();
                    Bolha_esquerda.setColor(getResources().getColor(R.color.colorBolhaEsquerda));
                    Bolha_esquerda.setShape(GradientDrawable.RECTANGLE);
                    Bolha_esquerda.setCornerRadii(new float[]{5, 5, raio, raio, raio, raio, raio, raio});

                    GradientDrawable Bolha_AvisoGrupo = new GradientDrawable();
                    Bolha_AvisoGrupo.setColor(getResources().getColor(R.color.colorBolha_AvisoGrupo));
                    Bolha_AvisoGrupo.setShape(GradientDrawable.RECTANGLE);
                    Bolha_AvisoGrupo.setCornerRadii(new float[]{5, 5, raio, raio, raio, raio, raio, raio});

                    GradientDrawable Bolha_direita = new GradientDrawable();
                    Bolha_direita.setColor(getResources().getColor(R.color.colorBolhaDireita));
                    Bolha_direita.setShape(GradientDrawable.RECTANGLE);
                    Bolha_direita.setCornerRadii(new float[]{raio, raio, 5, 5, raio, raio, raio, raio});

                    String remetente = response.getJSONArray("MENSAGENS").getJSONObject(i).get("remetente").toString();
                    String destinatario = response.getJSONArray("MENSAGENS").getJSONObject(i).get("destinatario").toString();
                    String texto = response.getJSONArray("MENSAGENS").getJSONObject(i).get("texto_mensagem").toString();
                    String hora_envio = response.getJSONArray("MENSAGENS").getJSONObject(i).get("data_envio").toString().substring(11,16);
                    String bolha_mensagem_esquerda = " "+hora_envio+"\n "+remetente+"\n "+texto;
                    String bolha_mensagem_direita = hora_envio+" \n "+texto+" ";

                    SpannableString stringBolhaMsgmEsquerda = new SpannableString(bolha_mensagem_esquerda);
                    stringBolhaMsgmEsquerda.setSpan(new AbsoluteSizeSpan(12, true), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBolhaMsgmEsquerda.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorChat)), 8, 8+remetente.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString stringBolhaAvisoGrupo = new SpannableString(bolha_mensagem_esquerda);
                    stringBolhaAvisoGrupo.setSpan(new AbsoluteSizeSpan(12, true), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBolhaAvisoGrupo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorChatAvisoGrupo)), 8, 8+remetente.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SpannableString stringBolhaMsgmDireita = new SpannableString(bolha_mensagem_direita);
                    stringBolhaMsgmDireita.setSpan(new AbsoluteSizeSpan(12, true), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    LinearLayout layout = (LinearLayout) findViewById(R.id.ConversaUsuario);
                    Texto_Mensagem = new Button(this);

                    Texto_Mensagem.setTransformationMethod(null);
                    Texto_Mensagem.setTextColor(getResources().getColor(R.color.white));

                    if(remetente.equals(login)) {
                        Texto_Mensagem.setBackground(Bolha_direita);
                        Texto_Mensagem.setText(stringBolhaMsgmDireita);
                        Texto_Mensagem.setGravity(Gravity.RIGHT);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.gravity=Gravity.RIGHT;
                        params.setMargins(16,10,8,10);
                        Texto_Mensagem.setLayoutParams(params);

                    }
                    else if (remetente.equals(destinatario)){
                        Texto_Mensagem.setBackground(Bolha_AvisoGrupo);
                        Texto_Mensagem.setText(stringBolhaAvisoGrupo);
                        Texto_Mensagem.setGravity(Gravity.LEFT);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8,10,16,10);
                        Texto_Mensagem.setLayoutParams(params);
                    }
                    else {
                        Texto_Mensagem.setBackground(Bolha_esquerda);
                        Texto_Mensagem.setText(stringBolhaMsgmEsquerda);
                        Texto_Mensagem.setGravity(Gravity.LEFT);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8,10,16,10);
                        Texto_Mensagem.setLayoutParams(params);
                    }
                    Texto_Mensagem.setPadding(16,16,16,16);
                    Texto_Mensagem.setTextSize(15);
                    Texto_Mensagem.setClickable(false);

                    layout.addView(Texto_Mensagem);
                }

                GradientDrawable Espaco_vazio = new GradientDrawable();
                Espaco_vazio.setColor(getResources().getColor(R.color.transparent));
                Espaco_vazio.setShape(GradientDrawable.RECTANGLE);

                LinearLayout layout = (LinearLayout) findViewById(R.id.ConversaUsuario);
                Texto_Mensagem = new Button(this);
                Texto_Mensagem.setText("\n\n\n");
                Texto_Mensagem.setBackground(Espaco_vazio);
                layout.addView(Texto_Mensagem);//ADICIONA ESPACO VAZIO APOS A UTLIMA MENSAGEM

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

        mScrollView.post(new Runnable() {//VAI P FINAL DA CONVERSA

            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    @Override
    public void onBackPressed() {

        final String login = getIntent().getStringExtra("Login");

        URL = URL + "&login=" + login;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent( getApplicationContext(), ListaConversas.class);
                intent.putExtra("Login",login);
                intent.putExtra("ListaConversas",json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void EnviarMensagem(View view) throws UnsupportedEncodingException {

        String texto_mensagem = Mensagem.getText().toString();
        final String remetente = getIntent().getStringExtra("Login");
        final String destinatario = getIntent().getStringExtra("Destinatario");

        if (texto_mensagem.matches("")) {
            Toast.makeText(this, "Escreva alguma coisa!", Toast.LENGTH_SHORT).show();
            return;
        }

        //texto_mensagem = URLEncoder.encode(texto_mensagem, "UTF-8");----TESTAR

        String URLenviar = "http://192.168.15.5:8080/SanhagramServletsJSP/UsuarioControlador?acao=enviarMsgm&dispositivo=android";

        params = new RequestParams();
        params.put("remetente",remetente);
        params.put("destinatario",destinatario);
        params.put("texto_mensagem",texto_mensagem);

        client = new AsyncHttpClient();
        client.post(URLenviar,params, new JsonHttpResponseHandler() {

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

                Toast.makeText(ChatUsuario.this, "Erro!", Toast.LENGTH_LONG).show();
                return;

            }
        });

    }

}
