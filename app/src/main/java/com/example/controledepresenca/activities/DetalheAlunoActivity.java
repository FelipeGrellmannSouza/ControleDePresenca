package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionBarPolicy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;
import com.squareup.picasso.Picasso;

public class DetalheAlunoActivity extends AppCompatActivity {
    private ImageView fotoAluno;
    private TextView txtNome;
    private TextView txtEmail;
    private TextView txtRGM;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhe_aluno);
        fotoAluno = findViewById(R.id.imageAluno);
        txtNome = findViewById(R.id.txtNomeAluno);
        txtEmail = findViewById(R.id.txtEmailAluno);
        txtRGM = findViewById(R.id.txtRgmAluno);

        usuarioDAO = new UsuarioDAO(this);

        Intent intent = getIntent();
        String alunoEmaIl = intent.getStringExtra("ALUNO_EMAIL");

        usuarioDAO.obterDadosUsuario(alunoEmaIl, new UsuarioDAO.OnUsuarioDataReceivedListener() {
            @Override
            public void onUsuarioDataReceived(Usuario aluno) {
                if(aluno != null){
                    preencherCampos(aluno);
                } else {
                    Toast.makeText(DetalheAlunoActivity.this, "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void preencherCampos(Usuario aluno){

        Picasso.get().load(aluno.getUrlFoto()).into(fotoAluno);

        txtNome.setText(aluno.getNome());
        txtEmail.setText(aluno.getEmail());
        txtRGM.setText(aluno.getRgm());
    }
}