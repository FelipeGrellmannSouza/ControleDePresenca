package com.example.controledepresenca.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;
import com.example.controledepresenca.util.ConfigDb;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtSenha;
    Button btnLogar;

    private UsuarioDAO usuarioDAO;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializandoComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioDAO.iniciarUsuarioLogado();
    }

    private void inicializandoComponentes(){
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnLogar =findViewById(R.id.btnLogar);

        auth = ConfigDb.FBAuntenticacao();
        usuarioDAO = new UsuarioDAO(this);
    }

    public void validarLogin(View view){
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (!email.isEmpty()){
            if (!senha.isEmpty()){
                //Estanciando Nome e senha do usuario
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                usuarioDAO.logarUsuario(usuario);//método onde ocorre o login e verificação de modo
            }else {Toast.makeText(this, "Digite sua Senha", Toast.LENGTH_SHORT).show();}
        }else {Toast.makeText(this, "Digite seu Email", Toast.LENGTH_SHORT).show();}
    }




}