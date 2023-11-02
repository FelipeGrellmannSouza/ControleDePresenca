package com.example.controledepresenca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {
    private UsuarioDAO usuarioDAO;
    Usuario usuario;
    FirebaseAuth auth;
    EditText edtEmail, edtSenha, edtNome, edtRgm;
    Button btnCadastrar;
    TextView txtRgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Incializar();
        recuperandoModoSelecionado();
    }

    private String recuperandoModoSelecionado(){
        Intent intent = getIntent();
        String modoSelecionado = intent.getStringExtra("modoSelecionado");
        System.out.println("modo selecionado: "+ modoSelecionado);
        if ("aluno".equals(modoSelecionado)){
          System.out.println("Comparação funcionando Corretamente");
          txtRgm.setVisibility(View.VISIBLE);
          edtRgm.setVisibility(View.VISIBLE);
        }
        return modoSelecionado;
    }

    //método para organização do código
    public void Incializar(){
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtNome = findViewById(R.id.edtNome);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edtRgm = findViewById(R.id.edtRgm);
        txtRgm = findViewById(R.id.txtRgm);

        //Setando RGM INVISIVEL
        txtRgm.setVisibility(View.INVISIBLE);
        edtRgm.setVisibility(View.INVISIBLE);
        usuarioDAO = new UsuarioDAO(this);
    }

    public void validarCampos(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String modo = recuperandoModoSelecionado();
        String nome = edtNome.getText().toString();
        String rgm = edtRgm.getText().toString();
        //Se modo = aluno
        if (modo.equals("aluno")){
            if (!nome.isEmpty()) {
                if (!rgm.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (!senha.isEmpty()) {
                            if (modo != null) {
                                //estaciando o objeto usuario
                                usuario = new Usuario();
                                usuario.setEmail(email);
                                usuario.setSenha(senha);
                                usuario.setNome(nome);
                                usuario.setRgm(rgm);
                                usuario.setModo(modo);
                                cadastrarUsuario(usuario);
                                homeAluno();
                            } else {Toast.makeText(this, "Selecione se Você é um Aluno ou um Professsor", Toast.LENGTH_SHORT).show();}
                        } else {Toast.makeText(this, "Preencha sua senha corretamente", Toast.LENGTH_SHORT).show();}
                    } else {Toast.makeText(this, "Preencha o campo email corratamente", Toast.LENGTH_SHORT).show();}
                } else {Toast.makeText(this, "Preencha o campo rgm corretamente", Toast.LENGTH_SHORT).show();}
            } else {Toast.makeText(this, "Preencha o campo nome corretamente", Toast.LENGTH_SHORT).show();}
        }
        //Se o modo = professor
        else if (modo.equals("professor")) {
            if (!nome.isEmpty()) {
                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        //De preferencia Criar objeto Professor
                        usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        usuario.setNome(nome);
                        usuario.setRgm(rgm);
                        usuario.setModo(modo);
                        cadastrarUsuario(usuario);
                    } else {Toast.makeText(this, "Preencha sua senha corretamente", Toast.LENGTH_SHORT).show();}
                } else {Toast.makeText(this, "Preencha o campo email corratamente", Toast.LENGTH_SHORT).show();}
            } else {Toast.makeText(this, "Preencha o campo nome corretamente", Toast.LENGTH_SHORT).show();}
        }
        //Caso ocorra um erro
        else {Toast.makeText(this, "ERRO AO CARREGAR O ALUNO", Toast.LENGTH_SHORT).show();}
    }

    public void cadastrarUsuario(Usuario usuario) {
       usuarioDAO.cadastrarUsuario(usuario, new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               // Este método será chamado quando o estado de autenticação mudar
               // Serve para lidar com diferentes estados de autenticação
               if (firebaseAuth.getCurrentUser() != null){
                   usuarioDAO.adicionarDadosUsuario(usuario);//método que adiciona os dados do usuario ao FireStore
               }
           }
       });
    }

    private void homeAluno(){
        Intent intent = new Intent(this, MainActivity.class);//mudar para home do aluno
        startActivity(intent);
    }

}