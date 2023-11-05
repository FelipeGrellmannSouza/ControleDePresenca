package com.example.controledepresenca.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AlunoActivity extends AppCompatActivity {

    private static final int CODIGO_REQUISICAO_CAMERA = 1;
    Button btnScanner;
    TextView txtTexto;
    UsuarioDAO usuarioDAO;

    private boolean qrCode1Lido = false;
    private boolean qrCode2Lido = false;
    private boolean fotoTirada = false;


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

    //define a logica de leitura dos qrcodes
    private void processarQRCode(String conteudo){
        if (!qrCode1Lido) {
            if (conteudo.equals("pre1")) {
                Toast.makeText(this, "QR Code 1 lido com sucesso", Toast.LENGTH_SHORT).show();
                qrCode1Lido = true;
                tirarFoto();
            } else {
                Toast.makeText(this, "QR Code Inválido", Toast.LENGTH_SHORT).show();
            }
        } else if (qrCode1Lido && !qrCode2Lido) {
            if (conteudo.equals("pre2")) {
                Toast.makeText(this, "Qr Code 2 lido com sucesso", Toast.LENGTH_SHORT).show();
                qrCode2Lido = true;
                usuarioDAO.definirPresente("pre1", "pre2");
                Intent intent = new Intent(this, AlunoActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "QR Code Inválido", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String conteudo = intentResult.getContents();
            if (conteudo != null) {
                processarQRCode(conteudo);
            }else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == CODIGO_REQUISICAO_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null){
                Bitmap fotoUsuario = (Bitmap) extras.get("data");

                if(fotoUsuario != null){
                    usuarioDAO.uploadFoto(fotoUsuario, usuarioDAO.getIdUsuairo(), new UsuarioDAO.OnUploadCompleteListener() {
                        @Override
                        public void onUploadComplete(String imageUrl) {
                            Toast.makeText(AlunoActivity.this, "Foto enviada com sucesso", Toast.LENGTH_SHORT).show();
                            fotoTirada = true;
                        }

                        @Override
                        public void onUploadFailed(Exception e) {
                            Toast.makeText(AlunoActivity.this, "Erro ao enviar a imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }





    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CODIGO_REQUISICAO_CAMERA);
        }
    }







}