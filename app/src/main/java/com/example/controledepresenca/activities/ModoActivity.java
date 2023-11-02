package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.controledepresenca.R;

public class ModoActivity extends AppCompatActivity {

    Button btnProfessor, btnAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo);

        btnProfessor = findViewById(R.id.btnProfessor);
        btnAluno = findViewById(R.id.btnAluno);
    }

    public void aluno(View view){
        String tipoSelecionado = "aluno";
        Intent intent = new Intent(ModoActivity.this, CadastroActivity.class);
        intent.putExtra("modoSelecionado", tipoSelecionado);
        startActivity(intent);
    }

    public void professor(View view){
        String tipoSelecionado = "professor";
        Intent intent = new Intent(ModoActivity.this, CadastroActivity.class);
        intent.putExtra("modoSelecionado", tipoSelecionado);
        startActivity(intent);
    }
}