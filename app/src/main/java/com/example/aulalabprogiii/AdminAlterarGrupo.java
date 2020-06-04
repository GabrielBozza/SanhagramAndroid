package com.example.aulalabprogiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminAlterarGrupo extends AppCompatActivity {

    String PrefixoURL,nomeUSU,chaveUSU;

    AsyncHttpClient client;
    JSONObject json;

    Button BotaoGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_alterar_grupo);

        String resultado = getIntent().getStringExtra("UsuariosGrupo");

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        try {

            JSONObject response = new JSONObject(resultado);

            GradientDrawable TituloUsuariosGrupo = new GradientDrawable();
            TituloUsuariosGrupo.setColor(getResources().getColor(R.color.transparent));
            TituloUsuariosGrupo.setShape(GradientDrawable.RECTANGLE);
            TituloUsuariosGrupo.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

            final LinearLayout layout = findViewById(R.id.LinearLayoutUsuariosGrupo);

            BotaoGrupo = new Button(this);
            BotaoGrupo.setText("Usuários do "+getIntent().getStringExtra("NomeGrupo"));
            BotaoGrupo.setTransformationMethod(null);
            BotaoGrupo.setClickable(false);
            BotaoGrupo.setPadding(10, 16, 10, 16);
            BotaoGrupo.setTextSize(23);
            BotaoGrupo.setBackground(TituloUsuariosGrupo);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 10, 16, 10);
            params.gravity = Gravity.CENTER;
            BotaoGrupo.setLayoutParams(params);
            BotaoGrupo.setGravity(Gravity.CENTER);
            layout.addView(BotaoGrupo);

            final LinearLayout layoutoutros = findViewById(R.id.LinearLayoutUsuariosForaGrupo);

            BotaoGrupo = new Button(this);
            BotaoGrupo.setText("Usuários fora do Grupo");
            BotaoGrupo.setTransformationMethod(null);
            BotaoGrupo.setTextSize(23);
            BotaoGrupo.setClickable(false);
            BotaoGrupo.setPadding(10, 16, 10, 16);
            BotaoGrupo.setBackground(TituloUsuariosGrupo);
            params.setMargins(16, 10, 16, 10);
            params.gravity = Gravity.CENTER;
            BotaoGrupo.setLayoutParams(params);
            BotaoGrupo.setGravity(Gravity.CENTER);
            layoutoutros.addView(BotaoGrupo);


            try {

                for (int i = 0; i < response.getJSONArray("USUARIOSDOGRUPO").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.colorVermelhoClaro));
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String nomeUsuarioGrupo = response.getJSONArray("USUARIOSDOGRUPO").get(i).toString();

                    SpannableString UsuGrupo= new SpannableString(" - "+nomeUsuarioGrupo);
                    UsuGrupo.setSpan(new AbsoluteSizeSpan(27, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    UsuGrupo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorVermelho)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    BotaoGrupo = new Button(this);
                    BotaoGrupo.setText(UsuGrupo);
                    BotaoGrupo.setId(i+1);
                    BotaoGrupo.setTransformationMethod(null);
                    BotaoGrupo.setWidth(650);
                    BotaoGrupo.setPadding(10, 10, 10, 10);
                    BotaoGrupo.setTextSize(20);
                    BotaoGrupo.setBackground(Opcao_Conversa);
                    BotaoGrupo.setGravity(Gravity.START);
                    params.setMargins(16, 10, 16, 10);
                    params.gravity = Gravity.CENTER;
                    BotaoGrupo.setLayoutParams(params);

                    BotaoGrupo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=1;i<layout.getChildCount();i++){
                                View child=layout.getChildAt(i);
                                child.setClickable(false);
                                child.setFocusableInTouchMode(false);
                            }
                            RemoverDoGrupo(nomeUsuarioGrupo);
                        }
                    });

                    layout.addView(BotaoGrupo);
                }

                for (int i = 0; i < response.getJSONArray("USUARIOSFORADOGRUPO").length(); i++) {

                    GradientDrawable Opcao_Conversa = new GradientDrawable();
                    Opcao_Conversa.setColor(getResources().getColor(R.color.colorAzulClaro));
                    Opcao_Conversa.setShape(GradientDrawable.RECTANGLE);
                    Opcao_Conversa.setCornerRadii(new float[]{25, 25, 25, 25, 25, 25, 25, 25});

                    final String nomeUsuarioForaGrupo = response.getJSONArray("USUARIOSFORADOGRUPO").get(i).toString();

                    SpannableString UsuForaGrupo= new SpannableString(" + "+nomeUsuarioForaGrupo);
                    UsuForaGrupo.setSpan(new AbsoluteSizeSpan(27, true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    UsuForaGrupo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBolhaEsquerda)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    BotaoGrupo = new Button(this);
                    BotaoGrupo.setText( UsuForaGrupo);
                    BotaoGrupo.setTransformationMethod(null);
                    BotaoGrupo.setWidth(650);
                    BotaoGrupo.setId(i+50);
                    BotaoGrupo.setPadding(10, 10, 10, 10);
                    BotaoGrupo.setTextSize(20);
                    BotaoGrupo.setBackground(Opcao_Conversa);
                    BotaoGrupo.setGravity(Gravity.START);
                    params.setMargins(16, 10, 16, 10);
                    params.gravity = Gravity.CENTER;
                    BotaoGrupo.setLayoutParams(params);

                    BotaoGrupo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            for(int i=1;i<layoutoutros.getChildCount();i++){
                                View child=layoutoutros.getChildAt(i);
                                child.setClickable(false);
                                child.setFocusableInTouchMode(false);
                            }

                            AdicionarAoGrupo(nomeUsuarioForaGrupo);
                        }
                    });

                    layoutoutros.addView(BotaoGrupo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

    }

 public void RemoverDoGrupo(String a){

     String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=removerDoGrupo&dispositivo=android"
             + "&login=" + nomeUSU + "&nomeGrupo=" + getIntent().getStringExtra("NomeGrupo")
             + "&nomeUsuario=" + a + "&chaveUSU="+chaveUSU;

     client = new AsyncHttpClient();
     client.get(URL, new JsonHttpResponseHandler() {

         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
             super.onSuccess(statusCode, headers, response);
             Toast.makeText(AdminAlterarGrupo.this, "Usuário removido do grupo!", Toast.LENGTH_SHORT).show();

             json = response;

             if (nomeUSU.equals("admin")) {
                 Intent intent = new Intent(getApplicationContext(), AdminAlterarGrupo.class);
                 intent.putExtra("UsuariosGrupo", json.toString());
                 intent.putExtra("NomeGrupo", getIntent().getStringExtra("NomeGrupo"));
                 startActivity(intent);
             }
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             super.onFailure(statusCode, headers, responseString, throwable);
             Toast.makeText(AdminAlterarGrupo.this, "Erro!", Toast.LENGTH_SHORT).show();
         }
     });
 }

 public void AdicionarAoGrupo(String a){

     String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=adicionarAoGrupo&dispositivo=android"
             + "&login=" + nomeUSU + "&nomeGrupo=" + getIntent().getStringExtra("NomeGrupo")
             + "&nomeUsuario=" + a+ "&chaveUSU="+chaveUSU;

     client = new AsyncHttpClient();
     client.get(URL, new JsonHttpResponseHandler() {

         @Override
         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
             super.onSuccess(statusCode, headers, response);
             Toast.makeText(AdminAlterarGrupo.this, "Usuário adicionado ao grupo!", Toast.LENGTH_SHORT).show();

             json = response;

             Intent intent = new Intent(getApplicationContext(), AdminAlterarGrupo.class);
             intent.putExtra("UsuariosGrupo", json.toString());
             intent.putExtra("NomeGrupo", getIntent().getStringExtra("NomeGrupo"));
             startActivity(intent);
         }

         @Override
         public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             super.onFailure(statusCode, headers, responseString, throwable);
             Toast.makeText(AdminAlterarGrupo.this, "Erro!", Toast.LENGTH_SHORT).show();
         }
     });

 }
    @Override
    public void onBackPressed() {

        String URL = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android"
                + "&login=" + nomeUSU+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                if (nomeUSU.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminListarGrupos.class);
                    intent.putExtra("ListaGrupos", json.toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminAlterarGrupo.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
