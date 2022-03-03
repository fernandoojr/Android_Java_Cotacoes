package com.cursoandroid.consultamoedas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cursoandroid.consultamoedas.model.Moeda;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Moeda> moedas = new ArrayList<>();
    private String metodo = "";
    private Spinner spinner;
    private TextView txtResultado;
    private Button btnConversão;
    private TextInputEditText txtValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        txtResultado = findViewById(R.id.txtResultado);
        btnConversão = findViewById(R.id.btnConversao);
        txtValor = findViewById(R.id.txtValor);

        carregarOpcoes();

        btnConversão.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                converter();
            }
        });
    }

    public void converter(){
        Double valor = Double.parseDouble(txtValor.getText().toString());
        Moeda moeda = (Moeda) spinner.getSelectedItem();
        Double valorMoeda = Double.parseDouble(moeda.getBid());
        if (moeda.getCode().equals("ETH")){
            valorMoeda = valorMoeda * 10;
        }else if(moeda.getCode().equals("BTC")){
            valorMoeda = valorMoeda*1000;
        }
        Double total = valor*valorMoeda;

        DecimalFormat decimalFormatMoeda = new DecimalFormat("0.00##");
        DecimalFormat decimalFormatConversao = new DecimalFormat("0.00");

        txtResultado.setText(moeda.getCode()+" está cotado atualmente em R$"+decimalFormatMoeda.format(valorMoeda)+"\n\nO valor informado em "
                +moeda.getCode()+ " corresponde a aproximadamente R$"+decimalFormatConversao.format(total));
    }
    public void carregarOpcoes(){
        MyTask task = new MyTask();
        String urlApi = "https://economia.awesomeapi.com.br/json/all";
        metodo = "todos";
        task.execute(urlApi);
    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //InputStreamReader lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //Objeto utilizado para leitura dos caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String linha = "";
                buffer = new StringBuffer();
                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(metodo.equals("todos")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String aux = (String) keys.next();
                        JSONObject jsonObject1 = jsonObject.getJSONObject(aux);
                        String name = jsonObject1.getString("name");
                        String code = jsonObject1.getString("code");
                        String bid = jsonObject1.getString("bid");
                        Moeda moeda = new Moeda(code, name, bid);
                        moedas.add(moeda);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, moedas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    }
}
