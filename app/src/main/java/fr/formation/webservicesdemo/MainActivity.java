package fr.formation.webservicesdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etCodePays;
    TextView tvnom, tvPays;
    ListView lvPays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodePays = findViewById(R.id.etCodePays);
        tvPays = findViewById(R.id.tvPays);
        lvPays = findViewById(R.id.lvPays);

    }

    public void searchOne(View view) {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        String codePay = etCodePays.getText().toString();
        HttpClient httpClient = new HttpClient();
        httpClient.setAdr("https://demo@services.groupkt.com/country/get/iso2code/" + codePay);
        httpClient.setMethod("GET");
        httpClient.start();
        try {
            httpClient.join();
            String response = httpClient.getResponse();
            JSONObject json = new JSONObject(response);
            JSONObject restResponse = json.getJSONObject("RestResponse");
            JSONObject result = restResponse.getJSONObject("result");
            String nom = result.getString("name");
            tvPays.setText(nom);
            im.hideSoftInputFromWindow(view.getWindowToken(),0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void searchAll(View view) throws JSONException {

        try {
            HttpClient httpClient = new HttpClient();
            httpClient.setAdr("http://alainpre.free.fr/cours/android/ressources/json/pays.json");
            httpClient.setMethod("GET");
            httpClient.start();
            httpClient.join();
            String reponse = httpClient.getResponse();

            //liste : tableau pour stocker les valeurs a afficher dans la liste
            //simpte_list_item_1 : format par defaut pours les elements de liste
            //adapter : composant pour lier tout ça
            ArrayList<String> liste = new ArrayList<String>();
            String response = httpClient.getResponse();

            JSONObject json = new JSONObject(response);
            JSONObject restResponse = json.getJSONObject("RestResponse");
            JSONArray result = restResponse.getJSONArray("result");
            for (int i=0; i<result.length(); i++){
                JSONObject pays = result.getJSONObject(i);
                String nom = pays.getString("name");
                liste.add(nom);
            }

            //Remplir cet ArrayList a partir du Json
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, liste);
            lvPays.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}