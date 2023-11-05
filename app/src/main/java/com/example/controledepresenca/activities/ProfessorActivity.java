package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;

import java.util.List;

public class ProfessorActivity extends AppCompatActivity {
    UsuarioDAO usuarioDAO;
    ListView listaAlunosPresentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        usuarioDAO = new UsuarioDAO(this);
        inicializarComponentes();

        //obter alunos presentes
        usuarioDAO.obterAlunosPresentes(new UsuarioDAO.OnUsuariosDataReceivedListener() {
            @Override
            public void onUsuariosDataReceived(List<String> nomesAlunos) {
                //Colocando os Alunos Presentes na ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfessorActivity.this, android.R.layout.simple_list_item_1, nomesAlunos);
                listaAlunosPresentes.setAdapter(adapter);
            }
        });
    }

    private void inicializarComponentes(){
        listaAlunosPresentes = findViewById(R.id.listAlunosPresentes);
    }


    public void iniciarAula(View view){
        usuarioDAO.definirFalta(new UsuarioDAO.OnUsuariosDataReceivedListener() {
            @Override
            public void onUsuariosDataReceived(List<String> nomesAlunos) {
                /*//atualiza a lista de alunos presentes na ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfessorActivity.this, android.R.layout.simple_list_item_1, idsAlunos);
                listaAlunosPresentes.setAdapter(adapter);*/

                Intent intent = new Intent(ProfessorActivity.this, AulaActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public void deslogarProfessor(View view){
        usuarioDAO.deslogar();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}