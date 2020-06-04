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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdminCriarGrupo extends AppCompatActivity {

    List<CheckBox> usuariosGrupoCheck = new ArrayList<>();
    String usuariosGrupoString="";
    String PrefixoURL,nomeUSU,chaveUSU;
    EditText nomeNovoGrupo;
    Button NomeConversa, BotaoCriar;
    CheckBox cb;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_criar_grupo);

        nomeNovoGrupo = findViewById(R.id.NomeNovoGrupo);
        BotaoCriar = findViewById(R.id.BotaoEnviar);
        String resultado = getIntent().getStringExtra("ListaUsuarios");

        SharedPreferences prefs = this.getSharedPreferences("USUARIO_AUTENTICADO", Context.MODE_PRIVATE);
        PrefixoURL = prefs.getString("PREFIXO_URL", "");
        nomeUSU = prefs.getString("LOGIN", "");
        chaveUSU = prefs.getString("CHAVE_USUARIO", "");

        try {

            JSONObject response = new JSONObject(resultado);

            try {

                int raio= 35;
                GradientDrawable CabecalhoChat = new GradientDrawable();
                CabecalhoChat.setColor(getResources().getColor(R.color.transparent));
                CabecalhoChat.setShape(GradientDrawable.RECTANGLE);
                CabecalhoChat.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, raio, raio});

                LinearLayout layoutCabecalho = findViewById(R.id.Cabecalho);
                NomeConversa = new Button(this);
                NomeConversa.setText("Criar Grupo");
                NomeConversa.setTransformationMethod(null);
                NomeConversa.setWidth(550);
                NomeConversa.setPadding(44,16,10,16);
                NomeConversa.setTextSize(22);
                NomeConversa.setBackground(CabecalhoChat);
                NomeConversa.setGravity(Gravity.START);
                NomeConversa.setClickable(false);

                layoutCabecalho.addView(NomeConversa);

                for(int i=0;i<response.getJSONArray("USUARIOS").length();i++) {

                    String nomeUsuario = response.getJSONArray("USUARIOS").getJSONObject(i).get("nome").toString();

                    LinearLayout layout = findViewById(R.id.UsuariosNovoGrupo);

                    cb = new CheckBox(getApplicationContext());
                    cb.setText(nomeUsuario);
                    cb.setTextSize(20);
                    cb.setPadding(16,16,10,16);
                    layout.addView(cb);
                    usuariosGrupoCheck.add(cb);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

    }

    @Override
    public void onBackPressed() {

        String URL = PrefixoURL +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android"
                + "&login=" + nomeUSU+ "&chaveUSU="+chaveUSU;

        client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarGrupos.class);
                intent.putExtra("ListaGrupos", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AdminCriarGrupo.this, "Erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void CriarGrupo(View view){

        String nomeGrupo = nomeNovoGrupo.getText().toString();

        for (CheckBox item : usuariosGrupoCheck){
            if(item.isChecked())
                usuariosGrupoString+=(item.getText().toString()+"|");
        }

        if (nomeGrupo.matches("")||usuariosGrupoString.matches("")) {
            Toast.makeText(this, "O Grupo não pode estar vazio e deve ter um nome!", Toast.LENGTH_SHORT).show();
            return;
        }

        BotaoCriar.setClickable(false);

        String URLCriarGrupo = PrefixoURL + "/SanhagramServletsJSP/UsuarioControlador?acao=criarGrupo&dispositivo=android";

        params = new RequestParams();
        params.put("login",nomeUSU);
        params.put("nomeGrupo",nomeGrupo);
        params.put("listaNovoGrupo",usuariosGrupoString);
        params.put("chaveUSU",chaveUSU);

        client = new AsyncHttpClient();
        client.post(URLCriarGrupo,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Toast.makeText(AdminCriarGrupo.this, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show();
                json = response;

                Intent intent = new Intent(getApplicationContext(), AdminListarGrupos.class);
                intent.putExtra("ListaGrupos", json.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                BotaoCriar.setClickable(true);
                Toast.makeText(AdminCriarGrupo.this, "Erro - Um grupo com o nome escolhido já existe, escolha outro nome!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
