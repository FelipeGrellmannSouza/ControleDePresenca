package com.example.controledepresenca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;
import com.example.controledepresenca.util.ConfigDb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private UsuarioDAO usuarioDAO;
    Usuario usuario;
    FirebaseAuth auth;
    EditText edtEmail, edtSenha, edtNome, edtRgm;
    Button btnCadastrar;
    RadioGroup radioGroup;
    RadioButton rdbAluno, rdbProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Incializar();

    }
    //método para organização do código
    public void Incializar(){
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtNome = findViewById(R.id.edtNome);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        radioGroup = findViewById(R.id.radioGroup);
        rdbAluno = findViewById(R.id.rdbAluno);
        rdbProfessor = findViewById(R.id.rdbProfessor);
        edtRgm = findViewById(R.id.edtRgm);

        usuarioDAO = new UsuarioDAO(this);
    }

    public void validarCampos(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String modo = modoSelecionando();
        String nome = edtNome.getText().toString();
        String rgm = edtRgm.getText().toString();

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
                            if(modo == "Aluno"){
                                homeAluno();
                            }
                        } else {
                            Toast.makeText(this, "Selecione se Você é um Aluno ou um Professsor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Preencha sua senha corretamente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Preencha o campo email corratamente", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Preencha o campo rgm corretamente", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha o campo nome corretamente", Toast.LENGTH_SHORT).show();
        }
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





    private String modoSelecionando(){
        int modoSelecionadoId = radioGroup.getCheckedRadioButtonId();

        if (modoSelecionadoId == R.id.rdbAluno){
            return "Aluno";
        }
        if (modoSelecionadoId== R.id.rdbProfessor){
            return "Professor";
        }
        else {
            return null;
        }

    }


    private void homeAluno(){
        Intent intent = new Intent(this, MainActivity.class);//mudar para home do aluno
        startActivity(intent);
    }

}