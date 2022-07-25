package com.daniel.appum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText txtPeso;
    EditText txtAltura;
    TextView txtIMC;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPeso = findViewById(R.id.txtPeso);
        txtAltura = findViewById(R.id.txtAltura);
        txtIMC = findViewById(R.id.txtIMC);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalcularIMC();
            }
        });

    }

    private void CalcularIMC() {
        double Imc;
        double Peso = Double.parseDouble(txtPeso.getText().toString());
        double Altura = Double.parseDouble(txtAltura.getText().toString());
        Imc =  Peso / (Math.pow(Altura,2));

        txtIMC.setText(String.format("%f",Imc));
    }
}