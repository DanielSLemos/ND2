package com.example.appwebservicetempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText edtCidade;
    TextView txtDados;
    Button btnBuscar;
    SensorManager mSensores;
    Sensor sAcelerometro;
    int sacudidasX=0;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCidade = findViewById(R.id.edtCidade);
        txtDados = findViewById(R.id.txtDados);
        btnBuscar = findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrevisao();
            }
        });

        mSensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        sAcelerometro = mSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensores.registerListener(
                new Acelerometro(),
                sAcelerometro,
                SensorManager.SENSOR_DELAY_UI
        );

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

    }

    class Acelerometro implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double x = sensorEvent.values[0];
            if (x < -15 || x > 15) {
                sacudidasX++;
            }
            if(sacudidasX>10){
                sacudidasX = 0;
                capturar();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    private void capturar(){
        Intent iSTT = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        iSTT.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(iSTT, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String textoReconhecido = resultado.get(0);

                edtCidade.setText(textoReconhecido);
                getPrevisao();
                narrar(txtDados.getText().toString());
            }
        }
    }

    private void narrar(String texto){
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void getPrevisao(){
        Tempo tempo = null;
        String dados = "";

        try {
            tempo = new HttpTempo(edtCidade.getText().toString()).execute().get();

            dados += "Estado atual: " + tempo.weather.get(0).description + "\n";
            dados += "Temperatura: " + String.format("%.1f", tempo.main.temperatura - 273.15) + " graus célcius\n";
            dados += "Máxima prevista: " + String.format("%.1f", tempo.main.maxima - 273.15) + " graus célcius\n";
            dados += "Mínima prevista: " + String.format("%.1f", tempo.main.minima - 273.15) + " graus célcius\n";
            dados += "Umidade relativa: " + tempo.main.umidade + "%\n";
            dados += "Velocidade do vento: " +  String.format("%.1f", tempo.wind.speed * 1.852) + "km/h";

            txtDados.setText(dados);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}