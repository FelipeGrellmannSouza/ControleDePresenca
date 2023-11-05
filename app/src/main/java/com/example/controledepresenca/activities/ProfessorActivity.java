package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;

public class ProfessorActivity extends AppCompatActivity {
    UsuarioDAO usuarioDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        usuarioDAO = new UsuarioDAO(this);

    }


    public void iniciarAula(View view){
        Intent intent = new Intent(this, AulaActivity.class);
        startActivity(intent);
        finish();
    }


    public void deslogarProfessor(View view){
        usuarioDAO.deslogar();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}