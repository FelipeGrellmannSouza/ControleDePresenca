package com.example.controledepresenca.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AlunoActivity extends AppCompatActivity {

    Button btnScanner;
    TextView txtTexto;
    UsuarioDAO usuarioDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        usuarioDAO = new UsuarioDAO(this);

        btnScanner = findViewById(R.id.btnLerQrCode);
        txtTexto = findViewById(R.id.txtTextoQrCode);

        //método para chamar o QR code
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(AlunoActivity.this);

                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setPrompt("Leia o QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });
    }

    public void deslogarAluno(View view){
        usuarioDAO.deslogar();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String conteudo = intentResult.getContents();
            if (conteudo != null) {
                txtTexto.setText(intentResult.getContents());
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}