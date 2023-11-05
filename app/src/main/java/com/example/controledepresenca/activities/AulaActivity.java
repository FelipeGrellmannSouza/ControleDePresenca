package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.controledepresenca.R;
import com.example.controledepresenca.model.GeradorQrCode;

public class AulaActivity extends AppCompatActivity {
    //QR CODE
    GeradorQrCode geradorQrCode;
    String QrCode1 = "pre1";
    String QrCode2 = "pre2";

    //Compenentes Visuais
    ImageView imgQRCode;
    TextView txtTitulo;
    Button btnQRCode2, btnFinalizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula);

        incializandoCompontes();
        gerarQRCode2();
    }

    private void incializandoCompontes(){
        geradorQrCode = new GeradorQrCode();

        imgQRCode = findViewById(R.id.imgQRcode);
        txtTitulo =findViewById(R.id.txtTituloQR);
        btnQRCode2 = findViewById(R.id.btnQRCode2);
        btnFinalizar = findViewById(R.id.btnFinalizarAula);

        imgQRCode.setImageBitmap(geradorQrCode.gerarQRCode(QrCode1));//Setando QRCode1
        btnFinalizar.setVisibility(View.INVISIBLE);//Setnando Finalizar como Invisivel
    }

    private void gerarQRCode2(){
        btnQRCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTitulo.setText("QR CODE 2");//setando novo titulo
                imgQRCode.setImageBitmap(geradorQrCode.gerarQRCode(QrCode2));//definindo o qrcode 2
                btnQRCode2.setVisibility(View.INVISIBLE);//setando o botão como invisivel
                btnFinalizar.setVisibility(View.VISIBLE);//setando o botão como visiavel
            }
        });
    }


}