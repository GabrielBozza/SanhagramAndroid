package com.example.aulalabprogiii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    List<CheckBox> usuariosGrupoCheck = new ArrayList<CheckBox>();
    String usuariosGrupoString="";
    EditText nomeNovoGrupo;
    Button NomeConversa;
    CheckBox cb;

    RequestParams params;
    AsyncHttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_criar_grupo);

        nomeNovoGrupo = (EditText)findViewById(R.id.NomeNovoGrupo);
        String resultado = getIntent().getStringExtra("ListaUsuarios");

        try {

            JSONObject response = new JSONObject(resultado);

            try {

                int raio= 35;
                GradientDrawable CabecalhoChat = new GradientDrawable();
                CabecalhoChat.setColor(getResources().getColor(R.color.transparent));
                CabecalhoChat.setShape(GradientDrawable.RECTANGLE);
                CabecalhoChat.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, raio, raio});

                LinearLayout layoutCabecalho = (LinearLayout) findViewById(R.id.Cabecalho);
                NomeConversa = new Button(this);
                NomeConversa.setText("Criar Grupo");
                NomeConversa.setTransformationMethod(null);
                NomeConversa.setWidth(550);
                NomeConversa.setPadding(44,16,10,16);
                NomeConversa.setTextSize(22);
                NomeConversa.setBackground(CabecalhoChat);
                NomeConversa.setGravity(Gravity.LEFT);
                NomeConversa.setClickable(false);

                layoutCabecalho.addView(NomeConversa);

                for(int i=0;i<response.getJSONArray("USUARIOS").length();i++) {

                    GradientDrawable Bolha_direita = new GradientDrawable();
                    Bolha_direita.setColor(getResources().getColor(R.color.colorBolhaDireita));
                    Bolha_direita.setShape(GradientDrawable.RECTANGLE);
                    Bolha_direita.setCornerRadii(new float[]{raio, raio, raio, raio, raio, raio, raio, raio});

                    String nomeUsuario = response.getJSONArray("USUARIOS").getJSONObject(i).get("nome").toString();

                    LinearLayout layout = (LinearLayout) findViewById(R.id.UsuariosNovoGrupo);

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

        String URL = getIntent().getStringExtra("PrefixoURL") +  "/SanhagramServletsJSP/UsuarioControlador?acao=listarGrupos&dispositivo=android"
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

                Toast.makeText(AdminCriarGrupo.this, "Erro!", Toast.LENGTH_LONG).show();

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

        String URLCriarGrupo = getIntent().getStringExtra("PrefixoURL") + "/SanhagramServletsJSP/UsuarioControlador?acao=criarGrupo&dispositivo=android";

        params = new RequestParams();
        params.put("login",getIntent().getStringExtra("Login"));
        params.put("nomeGrupo",nomeGrupo);
        params.put("listaNovoGrupo",usuariosGrupoString);

        client = new AsyncHttpClient();
        client.post(URLCriarGrupo,params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AdminCriarGrupo.this, "Grupo criado com sucesso!", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(AdminCriarGrupo.this, "Erro!", Toast.LENGTH_LONG).show();
                return;

            }
        });



    }
}
